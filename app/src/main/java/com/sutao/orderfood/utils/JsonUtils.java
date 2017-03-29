package com.sutao.orderfood.utils;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.bean.FoodOrder;
import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by Administrator on 2017/3/25.
 */
public class JsonUtils {

    public static String getJson(List<FoodOrder> orders,String position) {
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
            obj.put("order",builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }

    public static List<FoodOrder> parseJson(String str) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FoodOrder>>(){}.getType();
        List<FoodOrder> orders = gson.fromJson(str, listType);
        return orders;
    }
}
