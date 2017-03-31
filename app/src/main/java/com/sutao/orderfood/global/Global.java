package com.sutao.orderfood.global;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVUser;
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.bean.SellerInfo;
import com.sutao.orderfood.leanclound.LeanCloundHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/3/25.
 */
public class Global {

    //扫描的推送者的信息
    private static SellerInfo sellerInfo;

    //自己的推送id
    private static String installId;

    //食客已经点的菜品
    private static List<FoodOrder> foodOrders = new ArrayList<>();


    public static List<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public static void setFoodOrders(List<FoodOrder> foodOrders) {
        Global.foodOrders = foodOrders;
    }

    public static String getRestaurant() {

        return LeanCloundHelper.isSeller() ? AVUser.getCurrentUser().getUsername() :sellerInfo.getUserNmae();
    }



    public static void setSellerInfo(SellerInfo sellerInfo) {
        Global.sellerInfo = sellerInfo;
    }

    public static SellerInfo getSellerInfo() {
        return sellerInfo;
    }

    public static String getInstallId() {
        return installId;
    }

    public static void setInstallId(String installId) {
        Global.installId = installId;
    }

    public static void addOrder(String name) {
        for (FoodOrder data : foodOrders) {
            if (name.equals(data.getName())) {
                data.setNum(data.getNum()+1);
                return;
            }
        }
        foodOrders.add(new FoodOrder(name,1));
    }

    public static int getGlobalFoodNum(String name) {
        for (FoodOrder data : foodOrders) {
            if (name.equals(data.getName())) {
                return data.getNum();
            }
        }
        return 0;
    }
    public static void deleteOrder(String name) {
        Iterator<FoodOrder> iter = foodOrders.iterator();
        while (iter.hasNext()) {
            FoodOrder data = iter.next();
            if (name.equals(data.getName())) {
                data.setNum(data.getNum()-1);
                if (data.getNum() == 0) {
                    foodOrders.remove(data);
                }
                return;
            }
        }
    }

    public static void deleteAllOrder() {
        foodOrders.clear();
    }


}
