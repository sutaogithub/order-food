package com.sutao.orderfood.confirm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.JsonUtils;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ConfirmActivity extends BaseActivity{

    private PullToRefreshRecycleView mRefreshView;
    private ConfirmAdapter mAdapter;
    private Button mBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confess);
        initView();
        initEvent();
        initData();
    }

    private void initData() {


        mAdapter.setDatas(Global.getFoodOrders());
    }

    private void initEvent() {
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeanCloundHelper.pushMessage(ConfirmActivity.this, JsonUtils.getJson(Global.getFoodOrders(),"地方"));
            }
        });
    }

    private void initView() {
        mRefreshView = findView(R.id.list_order);
        mBtn = findView(R.id.btn_order);

        mAdapter = new ConfirmAdapter(this);
        RecyclerView list = mRefreshView.getRefreshableView();
        list.setAdapter(mAdapter);
        mRefreshView.setPullRefreshEnabled(false);

    }


}
