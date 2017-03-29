package com.sutao.orderfood.buyermain;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshBase;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.global.Global;

import java.util.List;

public class BuyerMainActivity extends BaseActivity {

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
    }

    private void initData() {
        AVQuery<AVObject> avQuery = new AVQuery<>("Food");
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("owner", Global.getRestaurant());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mAdapter.setData(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
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

}
