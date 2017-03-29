package com.sutao.orderfood.utils;

import com.avos.avoscloud.AVUser;
import com.sutao.orderfood.bean.SellerInfo;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.leanclound.LeanCloundHelper;

/**
 * Created by Administrator on 2017/3/28.
 */
public class CodeDecodeUtils {

    public static String getQrCodeString() {
        return "##** "+ AVUser.getCurrentUser().getUsername()+" "+
                Global.getInstallId()+" "+
                LeanCloundHelper.getSellerName();
    }

    public static SellerInfo parseQrcode(String str) {
        if (!str.startsWith("##** ")) {
            return null;
        }
        String[] message = str.split(" ");
        SellerInfo info = new SellerInfo(message[1],message[2],message[3]);
        return info;
    }


}
