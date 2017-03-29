package com.example.zxing.others;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.zxing.camera.CameraManager;
import com.google.zxing.Result;

/**
 * Created by Administrator on 2017/3/28.
 */
public interface OnDecodeInterface {

    void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor);
    ViewfinderView getViewfinderView();

    CameraManager getmCameraManager();

    Handler getHandler();
}
