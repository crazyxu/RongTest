package com.acce.rongtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.acce.rongtest.net.LruBitmapCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import io.rong.imlib.model.UserInfo;

/**
 * Created by acce-3 on 2015/10/31.
 */
public class AcceContext {
    private static AcceContext mAcceContext;
    private RequestQueue queue;
    private ImageLoader imageLoader;

    public RequestQueue getRequestQueue(){
        if (queue==null)
            queue= Volley.newRequestQueue(mContext);
        return queue;
    }

    public SharedPreferences getDefPreferences() {
        return defPreferences;
    }
    public ImageLoader getImageLoader(){
        getRequestQueue();
        if (imageLoader==null){
            imageLoader=new ImageLoader(queue,new LruBitmapCache());
        }
        return imageLoader;
    }


    private SharedPreferences defPreferences;
    public Context mContext;

    public UserInfo getCurrentUserInfo() {
        return currentUserInfo;
    }

    public void setCurrentUserInfo(UserInfo userInfo) {
        this.currentUserInfo = userInfo;
    }

    private UserInfo currentUserInfo;
    public static AcceContext getInstance(){
        if (mAcceContext==null)
            mAcceContext=new AcceContext();
        return mAcceContext;

    }

    private AcceContext(){}

    private AcceContext(Context context){
        mContext=context;
        defPreferences= PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static void init(Context context){
        mAcceContext=new AcceContext(context);
    }
}
