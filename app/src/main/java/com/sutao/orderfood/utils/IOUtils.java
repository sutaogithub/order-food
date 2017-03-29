package com.sutao.orderfood.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2017/3/23.
 */
public class IOUtils {

    public static void closeQuitely(Closeable io) {
        try {
            io.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
