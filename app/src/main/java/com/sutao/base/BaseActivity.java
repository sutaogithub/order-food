package com.sutao.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sutao.orderfood.R;
import com.sutao.orderfood.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2017/3/11
 */
public class BaseActivity extends AppCompatActivity {

    protected Dialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        initView();
    }

    private void initView() {
        findView(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findView(R.id.txt_title)).setText(getTitle().toString());
    }

    protected void setTopBarShow(boolean isShow) {
        if (isShow) {
            findView(R.id.rlayout_top_bar).setVisibility(View.VISIBLE);
        } else {
            findView(R.id.rlayout_top_bar).setVisibility(View.GONE);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        FrameLayout flayout = findView(R.id.flayout_container);
        flayout.removeAllViews();
        flayout.addView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected void showLoadingDialog() {
        mDialog = DialogUtils.getLoadingDialog(this);
        mDialog.show();
    }

    protected void dismissDialog() {
        if (mDialog!=null){
            mDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
