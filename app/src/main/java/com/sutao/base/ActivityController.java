package com.sutao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.sutao.orderfood.detail.DetailActivity;
import com.sutao.orderfood.foodlist.FoodListActivity;
import com.sutao.orderfood.main.MainActivity;
import com.sutao.orderfood.upload.UploadActivity;

/**
 * Created by Administrator on 2017/3/22.
 */
public class ActivityController {


    public static void showFoodListActivity(Context context, int type) {
        Intent intent = new Intent(context, FoodListActivity.class);
        intent.putExtra(FoodListActivity.TYPE_KEY, type);
        context.startActivity(intent);

    }

    public static void showDetailActivity(Activity context, View sharedView) {
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                sharedView,
                DetailActivity.VIEW_NAME_FOOD_IMAGE);
        Intent intent = new Intent(context,DetailActivity.class);
        ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
    }

    public static void showMainActivity(Context context) {
        Intent intent =new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showUploadActivity(Context context) {
        Intent intent =new Intent(context, UploadActivity.class);
        context.startActivity(intent);
    }

}
