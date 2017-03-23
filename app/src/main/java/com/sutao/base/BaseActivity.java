package com.sutao.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.sutao.orderfood.R;


/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2017/3/11
 */
public class BaseActivity extends AppCompatActivity {

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
    }

    protected void showTopBar(boolean isShow) {
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
}
