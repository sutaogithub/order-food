package com.sutao.orderfood.bean;

/**
 * Created by Administrator on 2017/3/24.
 */
public enum  Size {

    SMALL("小份"),MID("中份"),BIG("大份");

    private String str;

    Size(String str){
        this.str=str;
    }
    public String getStr(){
        return str;
    }

}
