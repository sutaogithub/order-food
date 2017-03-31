package com.sutao.orderfood.buyermain;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.sutao.base.ActivityController;
import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshBase;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.utils.ToastUtils;

import java.util.List;

public class BuyerMainActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshRecycleView mRefreshList;
    private BuyerMainAdapter mAdapter;
    private TextView mSellerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTopBarShow(false);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
                mAdapter.notifyDataSetChanged();
                mRefreshList.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
        findView(R.id.img_scan).setOnClickListener(this);
        findView(R.id.img_shop_car).setOnClickListener(this);
    }

    private void initData() {
        showLoadingDialog();
        AVQuery<AVObject> avQuery = new AVQuery<>("Food");
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("owner", Global.getRestaurant());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                dismissDialog();
                if (e == null) {
                    mAdapter.setData(list);
                } else {
                    e.printStackTrace();
                }
            }
        });

        AVQuery<AVObject> topSeller = new AVQuery<>("Food");
        topSeller.orderByDescending("createdAt");
        topSeller.whereEqualTo("owner", Global.getRestaurant());
        topSeller.whereEqualTo("type", Classify.TOP_SELL);
        topSeller.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                dismissDialog();
                if (e == null) {
                    mAdapter.setTopSellData(list);
                } else {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initData();
    }

    private void initView() {
        mRefreshList = (PullToRefreshRecycleView) findViewById(R.id.list_content);
        mRefreshList.setPullLoadEnabled(false);
        RecyclerView listView = mRefreshList.getRefreshableView();

        mSellerName = findView(R.id.txt_name);
        mSellerName.setText(Global.getSellerInfo().getSellerName());

        mAdapter = new BuyerMainAdapter(this);

        listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id) {
            case R.id.img_scan:
                ActivityController.showCaptureActivity(this);
                break;
            case R.id.img_shop_car:
                if (Global.getFoodOrders().size()>0) {
                    ActivityController.showConfirmActivity(this);
                } else {
                    ToastUtils.makeShortToast(this,"购物车为空");
                }
                break;
            case R.id.btn_logout:
                AVUser.logOut();
                ActivityController.showLoginActivity(this);
                finish();
                break;
        }
    }
}
