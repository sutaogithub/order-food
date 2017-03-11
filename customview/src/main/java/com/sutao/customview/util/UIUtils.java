package com.sutao.customview.util;

import android.content.Context;

/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 简单的功能介绍
 * @date 2017/3/9
 */
public class UIUtils {

    public static float getDp(Context context, int dp) {
        return context.getResources().getDisplayMetrics().density*dp;
    }


}
