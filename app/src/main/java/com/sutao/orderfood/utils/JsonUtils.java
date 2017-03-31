package com.sutao.orderfood.utils;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.bean.UserOrder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Administrator on 2017/3/25.
 */
public class JsonUtils {

    public static String getUserOrderJson(List<FoodOrder> orders, String position) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (FoodOrder food : orders) {
            builder.append("{");
            builder.append("\"name\":\"");
            builder.append(food.getName());
            builder.append("\",");
            builder.append("\"num\":\"");
            builder.append(food.getNum());
            builder.append("\"},");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("]");
        JSONObject obj = new JSONObject();
        try {
            obj.put("user", AVUser.getCurrentUser().getUsername());
            obj.put("position",position);
            obj.put("timestamp",System.currentTimeMillis());
            obj.put("order",builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static UserOrder parseUserOderJson(String jsonStr) {
        try {
            JSONObject json = new JSONObject(jsonStr);
            String name = json.getString("user");
            String position = json.getString("position");
            long timestamp = json.getLong("timestamp");
            List<FoodOrder> foods = JsonUtils.parseJson(json.getString("order"));
            UserOrder order = new UserOrder(name,position,foods,timestamp);
            return order;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<FoodOrder> parseJson(String str) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FoodOrder>>(){}.getType();
        List<FoodOrder> orders = gson.fromJson(str, listType);
        return orders;
    }
}
