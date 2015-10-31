package com.acce.rongtest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acce.rongtest.AcceContext;
import com.acce.rongtest.R;
import com.acce.rongtest.RongCloudEvent;
import com.acce.rongtest.TestApp;
import com.acce.rongtest.modles.UserData;
import com.acce.rongtest.utils.Md5Util;
import com.acce.rongtest.utils.MethodUtils;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilEmail;
    private TextInputLayout tilPwd;
    private Button btnLogin;
    private RequestQueue  queue;
    private ProgressDialog loading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (!initSp()){
            queue= Volley.newRequestQueue(this);
            initView();
        }else{
            connect(AcceContext.getInstance().getDefPreferences().getString("token",""));
            try {
                JSONObject jsonObject=new JSONObject(AcceContext.getInstance().getDefPreferences().getString("curUserInfo",""));
                saveUserDataToContext(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    void initView(){
        tilEmail=(TextInputLayout)super.findViewById(R.id.til_email);
        tilPwd=(TextInputLayout)super.findViewById(R.id.til_password);
        btnLogin=(Button)super.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilEmail.getEditText().getText().toString();
                String pwd = tilPwd.getEditText().getText().toString();
                if (TextUtils.isEmpty(email)) {
                    tilEmail.setErrorEnabled(true);
                    tilEmail.setError("邮箱或手机号不能为空");
                }
                if (TextUtils.isEmpty(pwd)) {
                    tilPwd.setErrorEnabled(true);
                    tilPwd.setError("密码不能为空");
                }
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
                    login(email, pwd);
                }
            }
        });
    }

    /**
     * 登录
     * @param email
     * @param pwd
     */
    void login(String email,String pwd){
        String url="http://mm.accedeal.com/acce-server/api/user/getUserOfPro.html";
        final Map<String,String> params=new HashMap<>();
        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);

        String sValue = null;
        try {
            UserData userData=new UserData();
            userData.setPassword(Md5Util.getMD5Str(pwd));
            userData.setUserName(email);
            sValue = mapper.writeValueAsString(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        params.put("json",sValue);
        StringRequest loginRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (loading!=null)
                    loading.dismiss();
                Log.i("--json--",s);
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if ("OK".equalsIgnoreCase(jsonObject.optString("SUCCESS"))){
                        connect(jsonObject.optString("token"));
                        saveUserData(jsonObject);
                    }else{
                        Toast.makeText(LoginActivity.this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("--json--","request error");
                if (loading!=null)
                    loading.dismiss();
                Toast.makeText(LoginActivity.this,"请求失败，请重试！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        queue.add(loginRequest);
        loading=ProgressDialog.show(this,"","正在登录，请稍等...");
    }

    /**
     *初始化，判断本地是否有token
     */
    boolean initSp(){
        String token = AcceContext.getInstance().getDefPreferences().getString("token", "");
        if (!TextUtils.isEmpty(token)){
            return true;
        }else{
            return false;
        }
    }


    /**
     * 存储数据
     * @param jsonObject
     */
    void saveUserData(JSONObject jsonObject){
        saveUserDataToContext(jsonObject);
        //保存token和原始user数据到本地
        SharedPreferences.Editor editor=AcceContext.getInstance().getDefPreferences().edit();
        editor.putString("token",jsonObject.optString("token"));
        editor.putString("curUserInfo",jsonObject.toString());
        editor.commit();
    }

    void saveUserDataToContext(JSONObject jsonObject){
        String userId=jsonObject.optString("userId");
        String userName=jsonObject.optString("userName");
        String portraitUri=jsonObject.optString("portraitUri");
        UserInfo userInfo=new UserInfo(userId,userName, Uri.parse(portraitUri));
        AcceContext.getInstance().setCurrentUserInfo(userInfo);
    }

    /**
     * 连接融云
     */
    void connect(String token){
        if (getApplicationInfo().packageName.equals(MethodUtils.getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userId 当前 token
                 */
                @Override
                public void onSuccess(String userId) {

                    Log.d("LoginActivity", "--onSuccess" + userId);
                    RongCloudEvent.getInstance().setOtherListener();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }


}

