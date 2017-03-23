package com.sutao.orderfood.detail;

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

import com.sutao.base.BaseActivity;
import com.sutao.orderfood.utils.FoodSise;
import com.sutao.orderfood.R;
import com.sutao.orderfood.utils.UIUtils;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2017/3/13
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String VIEW_NAME_FOOD_IMAGE = "1";
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
    private View[] mSizesView;

    private int mNum;
    private int mPrice;
    private int mSize;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

        mSizesView = new View[]{findView(R.id.btn_small), findView(R.id.btn_middle), findView(R.id.btn_big)};

        mAllPriceTxt.setVisibility(View.GONE);
        mOrder.setVisibility(View.GONE);

        mShoppingCar.setOnClickListener(this);
        mOrder.setOnClickListener(this);
        mAddBtn.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
        mAddShopCarBtn.setOnClickListener(this);
        for (View view : mSizesView) {
            view.setOnClickListener(this);
        }

        ViewCompat.setTransitionName(mFoodImg, VIEW_NAME_FOOD_IMAGE);
    }

    private void initData() {
        mFoodName.setText("鸡扒饭");
        mFoodSell.setText(getString(R.string.food_selling, "90", "20%"));
        mPrice = 21;
        mFoodPrice.setText("21");
        mFoodDescTxt.setText("牛逼的鸡扒饭");
        mAllPriceTxt.setText(getString(R.string.food_all_price, "20"));


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add_shopping:
                showAddSubBtn();
                break;
            case R.id.txt_order:

                break;
            case R.id.btn_add:
                addNum();
                break;
            case R.id.btn_sub:
                subNum();
                break;
            case R.id.img_shopping_car:
                break;
            case R.id.btn_small:
                select(FoodSise.SMALL);
                break;
            case R.id.btn_middle:
                select(FoodSise.MIDDLE);
                break;
            case R.id.btn_big:
                select(FoodSise.BIG);
                break;

        }
    }

    private void showAddSubBtn() {
        mNum = 1;
        mFoodNumTxt.setText(mNum + "");
        mAllPriceTxt.setText(getString(R.string.food_all_price,mNum * mPrice + ""));
        mAddShopCarBtn.setVisibility(View.GONE);
        mOrder.setVisibility(View.VISIBLE);
        mAllPriceTxt.setVisibility(View.VISIBLE);
    }

    private void select(int size) {
        for (View view : mSizesView) {
            view.setSelected(false);
        }
        switch (size) {
            case FoodSise.SMALL:
                mSizesView[0].setSelected(true);
                break;
            case FoodSise.MIDDLE:
                mSizesView[1].setSelected(true);
                break;
            case FoodSise.BIG:
                mSizesView[2].setSelected(true);
                break;
        }
        mSize = size;
    }

    private void subNum() {
        mNum--;
        mFoodNumTxt.setText(mNum + "");
        if (mNum <= 0) {
            mAddShopCarBtn.setVisibility(View.VISIBLE);
            mOrder.setVisibility(View.GONE);
            mAllPriceTxt.setVisibility(View.GONE);
        }
    }

    private void addNum() {
        mNum++;
        mFoodNumTxt.setText(mNum + "");
        mAllPriceTxt.setText(getString(R.string.food_all_price,mNum * mPrice + ""));


        animAdd();
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

