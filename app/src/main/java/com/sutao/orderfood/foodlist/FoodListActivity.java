package com.sutao.orderfood.foodlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshBase;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */
public class FoodListActivity extends BaseActivity{


    public static final String TYPE_KEY ="TYPE";

    private PullToRefreshRecycleView mRefreshList;
    private FoodListAdapter mAdapter;
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getIntent().getIntExtra(TYPE_KEY, Classify.MAIN_FOOD);
        setContentView(R.layout.activity_food_list);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mAdapter.setDeleteClickListener(new FoodListAdapter.DeleteClickListener() {
            @Override
            public void onClick(final int posistion, AVObject obj) {
                showLoadingDialog();
                obj.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(AVException e) {
                        dismissDialog();
                        if (e != null) {
                            ToastUtils.makeShortToast(FoodListActivity.this,e.toString());
                        } else {
                            mAdapter.delete(posistion);
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        mRefreshList = findView(R.id.list_food);
        mRefreshList.setPullLoadEnabled(false);
        RecyclerView listView = mRefreshList.getRefreshableView();
        listView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new FoodListAdapter(this);
        listView.setAdapter(mAdapter);


        mRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                initData();
                mRefreshList.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {

            }
        });
    }

    private void initData() {
        AVQuery<AVObject> avQuery = new AVQuery<>("Food");
        avQuery.whereEqualTo("owner", Global.getRestaurant());
        avQuery.whereEqualTo("type",mType);
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


}
