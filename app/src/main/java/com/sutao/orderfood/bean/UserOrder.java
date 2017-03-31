package com.sutao.orderfood.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/26.
 */
public class UserOrder {

    private String name;
    private String position;
    private List<FoodOrder> foodOrders;
    private long timestamp;

    public UserOrder(String name, String position, List<FoodOrder> foodOrders,long timestamp) {
        this.name = name;
        this.foodOrders = foodOrders;
        this.position = position;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FoodOrder> getFoodOrders() {
        return foodOrders;
    }

    public void setFoodOrders(List<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
