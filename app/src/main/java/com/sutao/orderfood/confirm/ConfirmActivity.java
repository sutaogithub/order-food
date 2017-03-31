package com.sutao.orderfood.confirm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SendCallback;
import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.JsonUtils;
import com.sutao.orderfood.utils.ToastUtils;

import java.util.List;

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
                showLoadingDialog();
                updateSelling();
                LeanCloundHelper.pushMessage(JsonUtils.getUserOrderJson(Global.getFoodOrders(),"地方"),new SendCallback(){

                    @Override
                    public void done(AVException e) {
                        dismissDialog();
                        if (e== null) {
                            ToastUtils.makeShortToast(ConfirmActivity.this,"下单成功");
                            Global.deleteAllOrder();
                            finish();
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void updateSelling() {
        List<FoodOrder> list = Global.getFoodOrders();
        for (FoodOrder food : list) {
            AVQuery<AVObject> avQuery = new AVQuery<>("Food");
            avQuery.whereEqualTo("owner", Global.getRestaurant());
            avQuery.whereEqualTo("name",food.getName());
            avQuery.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (AVObject obj : list) {
                            obj.put("selling",obj.getInt("selling")+1);
                            obj.saveInBackground();
                        }
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initView() {
        mRefreshView = findView(R.id.list_order);
        mBtn = findView(R.id.btn_order);
        if (LeanCloundHelper.isSeller()) {
            mBtn.setVisibility(View.GONE);
        }

        mAdapter = new ConfirmAdapter(this);
        RecyclerView list = mRefreshView.getRefreshableView();
        list.setAdapter(mAdapter);
        mRefreshView.setPullRefreshEnabled(false);
    }
}
