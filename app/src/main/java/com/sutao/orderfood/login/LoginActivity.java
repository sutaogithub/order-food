package com.sutao.orderfood.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sutao.base.ActivityController;
import com.sutao.base.BaseActivity;
import com.sutao.orderfood.R;

/**
 * Created by Administrator on 2017/3/22.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        findView(R.id.buyer_in).setOnClickListener(this);
        findView(R.id.seller_in).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.buyer_in:
                ActivityController.showMainActivity(this);
                break;
            case R.id.seller_in:
                ActivityController.showUploadActivity(this);
                break;
        }
    }
}
