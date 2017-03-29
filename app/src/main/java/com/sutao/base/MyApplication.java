package com.sutao.base;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.sutao.orderfood.global.Global;
import com.sutao.orderfood.login.LoginActivity;
import com.sutao.orderfood.utils.ToastUtils;

/**
 * Created by Administrator on 2017/3/23.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        AVOSCloud.initialize(this,"VYHzdPds4WiegvEINiJ74r8D-gzGzoHsz","eevpEqBr8rQgvQzKU7kNbtFf");
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    // 关联  installationId 到用户表等操作……
                    Global.setInstallId(installationId);
                } else {
                    ToastUtils.makeShortToast(getApplicationContext(),e.toString());
                }
            }
        });
        PushService.setDefaultPushCallback(this, LoginActivity.class);
    }
}
