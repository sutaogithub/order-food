package com.sutao.orderfood.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/23.
 */
public class ToastUtils {

    public static void makeShortToast(Context context, String str) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
