package com.acce.rongtest;

import android.app.Application;
import android.util.Log;

import com.acce.rongtest.message.AcceOrderMessage;
import com.acce.rongtest.provider.AcceOrderMessageProvider;
import com.acce.rongtest.utils.MethodUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.ipc.RongExceptionHandler;

/**
 * Created by acce-3 on 2015/10/29.
 */
public class TestApp extends Application {
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化Rong Cloud IMKit SDK
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if ("com.acce.rongtest".equals(MethodUtils.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(MethodUtils.getCurProcessName(getApplicationContext()))) {
            Log.i("init","-------------");
            RongIM.init(this);
            /**
             * 融云SDK事件监听处理
             *
             * 注册相关代码，只需要在主进程里做。
             */
            if ("com.acce.rongtest".equals(MethodUtils.getCurProcessName(getApplicationContext()))) {

                RongCloudEvent.init(this);

                Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));
                try {
                    //注册自定义消息和消息模板
                    RongIM.registerMessageType(AcceOrderMessage.class);
                    RongIM.registerMessageTemplate(new AcceOrderMessageProvider());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void setToken(String token){
        this.token=token;
    }
    public String getToken(){
        return token;
    }


}
