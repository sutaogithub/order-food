package com.sutao.orderfood.leanclound;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.avos.avoscloud.SignUpCallback;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/23.
 */
public class LeanCloundHelper {

    public static final String KEY_IS_SELLER = "KEY_IS_SELLER";
    public static final String KEY_SELLER_NAME = "KEY_SELLER_NAME";


    public static final String ACTION_PUSH = "com.orderfood.push";

    public static void signupBuyer(String userName, String password,SignUpCallback callback) {
        AVUser avUser = new AVUser();
        avUser.setUsername(userName);
        avUser.setPassword(password);
        avUser.put(KEY_IS_SELLER, false);
        avUser.signUpInBackground(callback);
    }

    public static void signupSeller(String userName, String password, String sellerName,SignUpCallback callback) {
        AVUser avUser = new AVUser();
        avUser.setUsername(userName);
        avUser.setPassword(password);
        avUser.put(KEY_IS_SELLER, true);
        avUser.put(KEY_SELLER_NAME, sellerName);
        avUser.signUpInBackground(callback);
    }

    public static String getSellerName() {
        return (String) AVUser.getCurrentUser().get(KEY_SELLER_NAME);
    }

    public static void setSellerName(String name) {
        if (TextUtils.isEmpty(name)){
            return;
        }
        AVUser.getCurrentUser().remove(KEY_SELLER_NAME);
        AVUser.getCurrentUser().put(KEY_SELLER_NAME,name);
        AVUser.getCurrentUser().signUpInBackground(null);
    }


    public static void saveFood(Food food, SaveCallback callback) {
        AVObject obj = new AVObject("Food");
        obj.put("name",food.getName());
        obj.put("desc",food.getDesc());
        obj.put("selling",food.getSelling());
        obj.put("price",food.getPrice());
        obj.put("type",food.getType());
        obj.put("shortDesc", food.getShortDesc());
        obj.put("owner",AVUser.getCurrentUser().getUsername());
        obj.put("img",new AVFile("foodImg",food.getBitmapByte()));
        obj.saveInBackground(callback);
    }

    public static void saveOrder(String orderJson, SaveCallback callback) {
        AVObject obj = new AVObject("Order");
        obj.put("order",orderJson);
        obj.put("owner",Global.getRestaurant());
        obj.saveInBackground(callback);
    }

    public static void pushMessage(String message,SendCallback callback) {
        AVPush push = new AVPush();
        AVQuery pushQuery = AVInstallation.getQuery();
        pushQuery.whereEqualTo("installationId", Global.getSellerInfo().getInstallId());
        push.setQuery(pushQuery);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", ACTION_PUSH);
        jsonObject.put("json", message);
        push.setData(jsonObject);
        push.sendInBackground(callback);
        saveOrder(message,null);
    }

    public static boolean isSeller() {
        return AVUser.getCurrentUser().getBoolean(KEY_IS_SELLER);
    }

}
