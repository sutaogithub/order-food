package com.sutao.orderfood.leanclound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sutao.orderfood.sellermain.JsonEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/26.
 */
public class PushReceiver extends BroadcastReceiver{

    private static final String TAG = "PushReceiver";
    
    
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getExtras() == null) {
                return;
            }
            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            String data = json.getString("json");
            EventBus.getDefault().post(new JsonEvent(data));
            Log.d(TAG, "JSONException: " + data);
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}
