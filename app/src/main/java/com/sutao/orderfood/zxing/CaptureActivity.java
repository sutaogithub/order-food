/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sutao.orderfood.zxing;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.avos.avoscloud.AVUser;
import com.example.zxing.R;
import com.example.zxing.camera.CameraManager;
import com.example.zxing.others.AmbientLightManager;
import com.example.zxing.others.BeepManager;
import com.example.zxing.others.CaptureActivityHandler;
import com.example.zxing.others.DecodeFormatManager;
import com.example.zxing.others.FinishListener;
import com.example.zxing.others.InactivityTimer;
import com.example.zxing.others.OnDecodeInterface;
import com.example.zxing.others.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.sutao.base.ActivityController;
import com.sutao.orderfood.bean.SellerInfo;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.utils.CodeDecodeUtils;
import com.sutao.orderfood.utils.ToastUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements SurfaceHolder.Callback ,OnDecodeInterface{


    private static final String TAG = "CaptureActivity";

    private Collection<BarcodeFormat> mDecodeFormats;
    private Map<DecodeHintType, ?> mDecodeHints;

    private CameraManager mCameraManager;
    private CaptureActivityHandler handler;


    private InactivityTimer mInactivityTimer;
    private BeepManager mBeepManager;
    private AmbientLightManager mAmbientLightManager;

    private boolean hasSurface;
    private String mCharacterSet;

    private SurfaceView mSurfaceView;
    private ViewfinderView mViewfinderView;

    public ViewfinderView getViewfinderView() {
        return mViewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getmCameraManager() {
        return mCameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);
        hasSurface = false;
        mInactivityTimer = new InactivityTimer(this);
        mBeepManager = new BeepManager(this);
        mAmbientLightManager = new AmbientLightManager(this);
        mViewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        mSurfaceView = (SurfaceView) findViewById(R.id.preview_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        mCameraManager = new CameraManager(getApplication());

        handler = null;
        setRequestedOrientation(getCurrentOrientation());


        mBeepManager.updatePrefs();
        mAmbientLightManager.start(mCameraManager);

        mInactivityTimer.onResume();

        mDecodeFormats = DecodeFormatManager.QR_CODE_FORMATS;
        mDecodeHints = null;
        mCharacterSet = null;
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
        //解决在息屏之后扫描匡不显示的问题
        mViewfinderView.invalidate();

    }


    private int getCurrentOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_90:
                    return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        } else {
            switch (rotation) {
                case Surface.ROTATION_0:
                case Surface.ROTATION_270:
                    return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                default:
                    return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
    }


    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        mInactivityTimer.onPause();
        mAmbientLightManager.stop();
        mBeepManager.close();
        mCameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mInactivityTimer.shutdown();
        if (AVUser.getCurrentUser()!=null){
            AVUser.logOut();
        }
        super.onDestroy();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                setResult(RESULT_CANCELED);
//                finish();
//                break;
//            case KeyEvent.KEYCODE_FOCUS:
//            case KeyEvent.KEYCODE_CAMERA:
//                // Handle these events so they don't launch the Camera app
//                return true;
//            // Use volume up/down to turn on light
//            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                mCameraManager.setTorch(false);
//                return true;
//            case KeyEvent.KEYCODE_VOLUME_UP:
//                mCameraManager.setTorch(true);
//                return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        mInactivityTimer.onActivity();
        String text = rawResult.getText();
        SellerInfo info = CodeDecodeUtils.parseQrcode(text);
        if (info == null) {
            showCannotParase(text);
        } else {
            Global.setSellerInfo(info);
            ActivityController.showBuyerMainActivity(this);
            finish();
        }


    }

    private void showCannotParase(String text) {
        ToastUtils.makeShortToast(this, text);
    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (mCameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            mCameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, mDecodeFormats, mDecodeHints, mCharacterSet, mCameraManager,this);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.cvt1_qrcode_camera_framework_bug));
        builder.setPositiveButton(R.string.cvt1_qrcode_button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }



}
