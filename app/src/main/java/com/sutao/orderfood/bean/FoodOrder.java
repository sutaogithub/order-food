package com.sutao.orderfood.bean;

/**
 * Created by Administrator on 2017/3/25.
 */
public class FoodOrder {


    private String name;
    private int num;



    public FoodOrder(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof FoodOrder) {
            FoodOrder other = (FoodOrder) o;
            return name.equals(other.getName());
        } else {
            return false;
        }
    }
}
