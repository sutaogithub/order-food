package com.sutao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;


import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.confirm.ConfirmActivity;
import com.sutao.orderfood.detail.DetailActivity;
import com.sutao.orderfood.foodlist.FoodListActivity;
import com.sutao.orderfood.buyermain.BuyerMainActivity;
import com.sutao.orderfood.login.LoginActivity;
import com.sutao.orderfood.signup.SignUpActivity;
import com.sutao.orderfood.upload.UploadActivity;
import com.sutao.orderfood.sellermain.SellerMainActivity;
import com.sutao.orderfood.zxing.CaptureActivity;

/**
 * Created by Administrator on 2017/3/22.
 */
public class ActivityController {


    public static void showFoodListActivity(Context context, int type) {
        Intent intent = new Intent(context, FoodListActivity.class);
        intent.putExtra(FoodListActivity.TYPE_KEY, type);
        context.startActivity(intent);
    }

    public static void showDetailActivity(Activity context, View sharedView,Food food) {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                sharedView,
                DetailActivity.VIEW_NAME_FOOD_IMAGE);
        Intent intent = new Intent(context,DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_FOOD,food);
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
    }

    public static void showBuyerMainActivity(Context context) {
        Intent intent =new Intent(context, BuyerMainActivity.class);
        context.startActivity(intent);
    }

    public static void showUploadActivity(Context context) {
        Intent intent =new Intent(context, UploadActivity.class);
        context.startActivity(intent);
    }

    public static void showSellerMainActivity(Context context) {
        Intent intent =new Intent(context, SellerMainActivity.class);
        context.startActivity(intent);
    }

    public static void showSignUpActivity(Context context) {
        Intent intent =new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    public static void showConfirmActivity(Context context) {
        Intent intent = new Intent(context, ConfirmActivity.class);
        context.startActivity(intent);
    }

    public static void showCaptureActivity(Context context) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivity(intent);
    }

    public static void showLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }


}
