package com.acce.rongtest.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by acce-3 on 2015/10/29.
 */
public class MethodUtils {
    /**
     * 获取当前进程名称
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
