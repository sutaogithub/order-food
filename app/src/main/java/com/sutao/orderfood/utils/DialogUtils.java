package com.sutao.orderfood.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.sutao.orderfood.R;

/**
 * Created by Administrator on 2017/3/24.
 */
public class DialogUtils {

    public static Dialog getLoadingDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final ScaleAnimation anim1 = new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim11 = new ScaleAnimation(1.5f,1f,1.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim2 = new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim22 = new ScaleAnimation(1.5f,1f,1.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim3 = new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim33 = new ScaleAnimation(1.5f,1f,1.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim4 = new ScaleAnimation(1,1.5f,1,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation anim44 = new ScaleAnimation(1.5f,1f,1.5f,1f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        anim1.setDuration(400);
        anim11.setDuration(400);
        anim1.setFillAfter(true);
        anim11.setFillAfter(true);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.findViewById(R.id.view_dot2).startAnimation(anim2);
                dialog.findViewById(R.id.view_dot1).startAnimation(anim11);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim2.setDuration(400);
        anim22.setDuration(400);
        anim2.setFillAfter(true);
        anim22.setFillAfter(true);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.findViewById(R.id.view_dot3).startAnimation(anim3);
                dialog.findViewById(R.id.view_dot2).startAnimation(anim22);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim3.setDuration(400);
        anim33.setDuration(400);
        anim3.setFillAfter(true);
        anim33.setFillAfter(true);
        anim3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.findViewById(R.id.view_dot4).startAnimation(anim4);
                dialog.findViewById(R.id.view_dot3).startAnimation(anim33);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim4.setDuration(400);
        anim44.setDuration(400);
        anim4.setFillAfter(true);
        anim44.setFillAfter(true);
        anim4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dialog.findViewById(R.id.view_dot1).startAnimation(anim1);
                dialog.findViewById(R.id.view_dot4).startAnimation(anim44);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        dialog.findViewById(R.id.view_dot1).startAnimation(anim1);
        return dialog;
    }

    public interface DialogBtnClickListener {
        void onOkClick(Dialog dailog);
        void onCancelBtn(Dialog dailog);
    }




}
