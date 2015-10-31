package com.acce.rongtest;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acce.rongtest.modles.UserData;
import com.acce.rongtest.provider.AcceExtendProvider;
import com.acce.rongtest.utils.Md5Util;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.PushNotificationManager;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by acce-3 on 2015/10/29.
 */
public class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,RongIM.UserInfoProvider, RongIM.GroupInfoProvider, RongIM.ConversationBehaviorListener,
        RongIMClient.ConnectionStatusListener, RongIM.LocationProvider, RongIMClient.OnReceivePushMessageListener, RongIM.ConversationListBehaviorListener,Handler.Callback {

    private static RongCloudEvent mRongCloudInstance;
    private RequestQueue queue;
    private Handler mHandler;
    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context){
        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
        mHandler = new Handler(this);
        queue= Volley.newRequestQueue(context);
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        RongIM.setConversationListBehaviorListener(this);
        //消息体内是否有 userinfo 这个属性
//        RongIM.getInstance().setMessageAttachedUserInfo(true);
//        RongIM.getInstance().getRongIMClient().setOnReceivePushMessageListener(this);//自定义 push 通知。
    }





    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public Group getGroupInfo(String s) {
        return null;
    }

    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {

    }
    /**
     * 自定义 push 通知。
     *
     * @param msg
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onReceivePushMessage(PushNotificationMessage msg) {

        PushNotificationManager.getInstance().onReceivePush(msg);

        Intent intent = new Intent();
        Uri uri;

        intent.setAction(Intent.ACTION_VIEW);

        Conversation.ConversationType conversationType = msg.getConversationType();

        uri = Uri.parse("rong://" + RongContext.getInstance().getPackageName()).buildUpon().appendPath("conversationlist").build();
        intent.setData(uri);

        Notification notification = null;

        PendingIntent pendingIntent = PendingIntent.getActivity(RongContext.getInstance(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT < 11) {
            notification=new Notification.Builder(RongContext.getInstance()).setContentIntent(pendingIntent).setContentTitle(msg.getPushTitle()).setContentText(msg.getPushContent()).build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND;
        } else {

            notification = new Notification.Builder(RongContext.getInstance())
                    .setLargeIcon(getAppIcon())
                    .setSmallIcon(R.drawable.yuexi_buy)
                    .setTicker("自定义 notification")
                    .setContentTitle("自定义 title")
                    .setContentText("这是 Content:" + msg.getObjectName())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL).build();

        }

        NotificationManager nm = (NotificationManager) RongContext.getInstance().getSystemService(RongContext.getInstance().NOTIFICATION_SERVICE);

        nm.notify(0, notification);

        return true;
    }

    @Override
    public UserInfo getUserInfo(String s) {
        final Map<String,String> params=new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
        SharedPreferences sp=mContext.getSharedPreferences("user",Context.MODE_PRIVATE);
        String sValue = null;
        try {
            UserData userData=new UserData();
            userData.setUserName(sp.getString("userId",""));
            sValue = mapper.writeValueAsString(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("json", sValue);

        HttpURLConnection connection=null;
        try{
            URL url=new URL("http://mm.accedeal.com/acce-server/api/user/getUserOfPro.html");
            connection=(HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(1000 * 5);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream outputStream=connection.getOutputStream();
            String postStr="json="+sValue;
            outputStream.write(postStr.getBytes());
            outputStream.flush();
            outputStream.close();

            //判断是否正常响应数据
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = new DataInputStream(connection.getInputStream());
                byte[] data = new byte[1024];
                int length = is.read(data);
                String response = new String(data, 0, length);
                Log.i("user", response);
                JSONObject jsonObject = new JSONObject(response);
                String portraitUri = jsonObject.optString("portraitUri");
                String nickName = jsonObject.optString("nickName");
                String userId = jsonObject.optString("userName");
                Uri uri = Uri.parse(portraitUri);
                UserInfo userInfo = new UserInfo(userId, nickName, uri);
                return userInfo;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean onReceived(Message message, int i) {
        return false;


    }

    @Override
    public Message onSend(Message message) {
        return null;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
        return false;
    }

    @Override
    public boolean handleMessage(android.os.Message msg) {
        return false;
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /*
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {

        RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.getInstance().setSendMessageListener(this);//设置发出消息接收监听器.
        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(this);//设置连接状态监听器。

//        扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new CameraInputProvider(RongContext.getInstance()),//相机
                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
                new AcceExtendProvider(RongContext.getInstance()),//acce订单
        };


        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.DISCUSSION, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.GROUP, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CUSTOMER_SERVICE, provider);
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.CHATROOM, provider);
//        RongIM.getInstance().setPrimaryInputProvider(new InputTestProvider((RongContext) mContext));

    }

    private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) RongContext.getInstance().getApplicationInfo().loadIcon(RongContext.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }
}
