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

package com.example.zxing.others;




import com.example.zxing.R;
import com.google.zxing.ResultPoint;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

  private static final long ANIMATION_DELAY = 80L;
  private static final int CURRENT_POINT_OPACITY = 0xA0;
  private static final int MAX_RESULT_POINTS = 20;
  private static final int POINT_SIZE = 6;

  private final Paint paint;
  private final int maskColor;
  private final int laserColor;
  private final int scanRectColor;
  private int scannerYPos;
  private List<ResultPoint> possibleResultPoints;
  private List<ResultPoint> lastPossibleResultPoints;


  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    laserColor = resources.getColor(R.color.viewfinder_laser);
    scanRectColor = resources.getColor(R.color.viewfinder_scan_rect);
    possibleResultPoints = new ArrayList<>(5);
    lastPossibleResultPoints = null;
  }



  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {

    int width = canvas.getWidth();
    int height = canvas.getHeight();

    Rect frame = new Rect(0, 0, width, height);
    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.reset();
    paint.setColor(maskColor);
    paint.setStyle(Paint.Style.FILL);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
    canvas.drawRect(0, frame.bottom + 1, width, height, paint);


    paint.setColor(scanRectColor);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawRect(frame, paint);

    // Draw a red "laser scanner" line through the middle to show decoding is active
    //暂时不画扫描线，交互未定
//    paint.reset();
//    paint.setColor(laserColor);
//
//    if (scannerYPos >= frame.top+60 && scannerYPos <= frame.bottom) {
//      scannerYPos += 3;
//    } else {
//      scannerYPos = frame.top+60;
//    }
//    Shader shader = new LinearGradient(frame.left + 2, scannerYPos - 60,
//            frame.left + 2, scannerYPos, laserColor& 0xffffff, laserColor , Shader.TileMode.MIRROR);
//    shader.setLocalMatrix();
//    paint.setShader(shader);
//    canvas.drawRect(frame.left + 2, scannerYPos - 60, frame.right - 1, scannerYPos, paint);


    // Request another update at the animation interval, but only repaint the laser line,
    // not the entire viewfinder mask.
    postInvalidateDelayed(ANIMATION_DELAY,
            frame.left - POINT_SIZE,
            frame.top - POINT_SIZE,
            frame.right + POINT_SIZE,
            frame.bottom + POINT_SIZE);
  }


  public void addPossibleResultPoint(ResultPoint point) {
    List<ResultPoint> points = possibleResultPoints;
    synchronized (points) {
      points.add(point);
      int size = points.size();
      if (size > MAX_RESULT_POINTS) {
        // trim it
        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
      }
    }
  }

}
