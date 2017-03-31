package com.sutao.orderfood.sellermain;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.bean.UserOrder;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.BitmapUtils;
import com.sutao.orderfood.utils.CodeDecodeUtils;
import com.sutao.orderfood.utils.JsonUtils;
import com.sutao.orderfood.utils.QrcodeUtils;
import com.sutao.orderfood.utils.ToastUtils;
import com.tencent.qc.stat.common.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */
public class SellerMainActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshRecycleView mRefreshList;
    private SellerMainAdapter mAdapter;
    private TextView mSellerName;
    private Dialog mEditDialog;
    private Dialog mQrCodeDialog;
    private Bitmap mQrcode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTopBarShow(false);
        setContentView(R.layout.activity_seller_main);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        mSellerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        mAdapter.setListener(new SellerMainAdapter.OnItemClickListener() {
            @Override
            public void onDetailClick(int position, UserOrder order) {
                Global.setFoodOrders(order.getFoodOrders());
                ActivityController.showConfirmActivity(SellerMainActivity.this);
            }

            @Override
            public void onDeleteClick(int position, UserOrder order) {
                mAdapter.delete(position);

            }
        });
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



    private void showEditDialog() {
        if (mEditDialog == null) {
            mEditDialog = new Dialog(this,R.style.OrderFoodDialog);
            mEditDialog.setContentView(R.layout.dialog_edit);
            mEditDialog.setCancelable(true);
            mEditDialog.setCanceledOnTouchOutside(true);
            final EditText editText = (EditText) mEditDialog.findViewById(R.id.edit_name);
            Button okbtn = (Button) mEditDialog.findViewById(R.id.btn_ok);
            Button cancelBtn = (Button) mEditDialog.findViewById(R.id.btn_cancel);
            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = editText.getText().toString();
                    if (!TextUtils.isEmpty(name)) {
                        LeanCloundHelper.setSellerName(name);
                        mSellerName.setText(name);
                    }
                    mEditDialog.dismiss();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditDialog.dismiss();
                }
            });
        }
        mEditDialog.show();
    }

    private void initData() {
        mSellerName.setText(LeanCloundHelper.getSellerName());
        mQrcode = QrcodeUtils.createQRCode(CodeDecodeUtils.getQrCodeString());

        AVQuery<AVObject> avQuery = new AVQuery<>("Order");
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo("owner", Global.getRestaurant());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mAdapter.setData(list);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initView() {
        mRefreshList = (PullToRefreshRecycleView) findViewById(R.id.list_order);
        RecyclerView listView = mRefreshList.getRefreshableView();
        listView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SellerMainAdapter(this);
        listView.setAdapter(mAdapter);

        mSellerName = findView(R.id.txt_name);

        findView(R.id.img_upload).setOnClickListener(this);
        findView(R.id.img_qrcode).setOnClickListener(this);
        findView(R.id.btn_logout).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id) {
            case R.id.img_upload:
                ActivityController.showUploadActivity(this);
                break;
            case R.id.img_qrcode:
                showQrcodeDialog();
                break;
            case R.id.btn_logout:
                AVUser.logOut();
                ActivityController.showLoginActivity(this);
                finish();
                break;
        }
    }

    private void showQrcodeDialog() {
        if (mQrCodeDialog == null) {
            mQrCodeDialog = new Dialog(this,R.style.OrderFoodDialog);
            mQrCodeDialog.getWindow().setContentView(R.layout.dialog_qrcode);

            mQrCodeDialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BitmapUtils.saveBitmap("/sdcard/qrcode.jpg",mQrcode);
                    ToastUtils.makeShortToast(SellerMainActivity.this,"已经保存到/sdcard/qrcode.jpg");
                    mQrCodeDialog.dismiss();
                }
            });
            ImageView img = (ImageView) mQrCodeDialog.findViewById(R.id.img_qrcode);
            img.setImageBitmap(mQrcode);
        }
        mQrCodeDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(JsonEvent event) {
        String jsonStr = event.getJsonStr();
        AVObject obj = new AVObject("Order");
        obj.put("order",jsonStr);
        obj.put("owner",Global.getRestaurant());
        mAdapter.addData(obj);
    }

}
