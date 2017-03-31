package com.sutao.orderfood.detail;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sutao.base.ActivityController;
import com.sutao.base.BaseActivity;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.bean.Size;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.utils.ToastUtils;
import com.sutao.orderfood.utils.UIUtils;

import javax.microedition.khronos.opengles.GL;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2017/3/13
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String VIEW_NAME_FOOD_IMAGE = "1";
    public static final String KEY_FOOD = "key_food";

    private RelativeLayout mRootLayout;
    private ImageView mFoodImg;
    private ImageView mShoppingCar;
    private TextView mFoodName;
    private TextView mFoodSell;
    private TextView mFoodPrice;
    private TextView mFoodNumTxt;
    private TextView mFoodDescTxt;
    private TextView mAllPriceTxt;
    private TextView mOrder;
    private Button mAddBtn;
    private Button mSubBtn;
    private Button mAddShopCarBtn;

    private int mNum;
    private int mSize;

    private Food mFood;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTopBarShow(false);
        setContentView(R.layout.activity_detail);

        mFood = getIntent().getParcelableExtra(KEY_FOOD);
        initView();
        initData();
    }


    private void initView() {
        mRootLayout = findView(R.id.rlayout_root);
        mShoppingCar = findView(R.id.img_shopping_car);
        mFoodImg = findView(R.id.img_food);
        mFoodName = findView(R.id.txt_food_name);
        mFoodSell = findView(R.id.txt_selling);
        mFoodPrice = findView(R.id.txt_price);
        mFoodNumTxt = findView(R.id.txt_num);
        mFoodDescTxt = findView(R.id.txt_desc);
        mOrder = findView(R.id.txt_order);
        mAllPriceTxt = findView(R.id.txt_all_price);
        mAddBtn = findView(R.id.btn_add);
        mSubBtn = findView(R.id.btn_sub);
        mAddShopCarBtn = findView(R.id.btn_add_shopping);

        mAllPriceTxt.setVisibility(View.GONE);
        mOrder.setVisibility(View.GONE);

        mShoppingCar.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
        mAddShopCarBtn.setOnClickListener(this);


        ViewCompat.setTransitionName(mFoodImg, VIEW_NAME_FOOD_IMAGE);
    }

    private void initData() {
        Glide.with(this).load(mFood.getImgUrl()).into(mFoodImg);
        mFoodName.setText(mFood.getName());
        mFoodSell.setText(getString(R.string.food_selling, mFood.getSelling()+""));
        mFoodPrice.setText(mFood.getPrice()+"");
        mFoodDescTxt.setText(mFood.getDesc());

        mNum = Global.getGlobalFoodNum(mFood.getName());
        updateView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNum = Global.getGlobalFoodNum(mFood.getName());
        updateView();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_shopping:
                addNum();
                break;
            case R.id.txt_order:
                ActivityController.showConfirmActivity(this);
                break;
            case R.id.btn_add:
                addNum();
                break;
            case R.id.btn_sub:
                subNum();
                break;
            case R.id.img_shopping_car:
                break;
        }
    }


    private void subNum() {

        Global.deleteOrder(mFood.getName());
        mNum--;
        updateView();
    }


    private void addNum() {
        mNum++;
        Global.addOrder(mFood.getName());
        updateView();
        animAdd();
    }

    private void updateView() {
        mFoodNumTxt.setText(mNum + "");
        mAllPriceTxt.setText(getString(R.string.food_all_price,mNum * mFood.getPrice() + ""));

        if (mNum <= 0) {
            mAddShopCarBtn.setVisibility(View.VISIBLE);
            mOrder.setVisibility(View.GONE);
            mAllPriceTxt.setVisibility(View.GONE);
        } else{
            mAddShopCarBtn.setVisibility(View.GONE);
            mOrder.setVisibility(View.VISIBLE);
            mAllPriceTxt.setVisibility(View.VISIBLE);
        }
    }


    private void animAdd() {
        final TextView view = new TextView(this);
        view.setBackgroundResource(R.drawable.orange_circle);
        view.setText("1");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAddBtn.getWidth(), mAddBtn.getHeight());
        mRootLayout.addView(view, params);

        int[] location = new int[2];
        mAddBtn.getLocationInWindow(location);
        view.setX(location[0]);
        view.setY(location[1]- UIUtils.getStatusBarHeight(this));

        TranslateAnimation xTransLate = new TranslateAnimation(0,mShoppingCar.getX()-view.getX(),0,0);
        TranslateAnimation yTransLate = new TranslateAnimation(0,0,0,mShoppingCar.getY()-view.getY());
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(xTransLate);
        set.addAnimation(yTransLate);
        xTransLate.setInterpolator(new LinearInterpolator());
        yTransLate.setInterpolator(new AccelerateInterpolator());
        set.setDuration(500);
        set.setFillAfter(true);
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRootLayout.removeView(view);
                    }
                },100);
                ScaleAnimation scaleBig = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleBig.setDuration(200);
                mShoppingCar.startAnimation(scaleBig);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}

