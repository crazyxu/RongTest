package com.acce.rongtest.activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.acce.rongtest.R;
import com.acce.rongtest.RongCloudEvent;
import java.util.Locale;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class ConversationActivity extends AppCompatActivity {
    /**
     *targetId
     */
    private String targetId;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String targetIds;
    /**
     * conversationType
     */
    private Conversation.ConversationType conversationType;
    private Toolbar bar;
    private UserInfo userInfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        bar=(Toolbar)super.findViewById(R.id.conversation_tool_bar);
        bar.setTitle("chat");
        setSupportActionBar(bar);
        /**
         * 从Rong SDK 发出的Intent中获取数据
         */
        getIntentDate(getIntent());

    }

    private void getIntentDate(Intent intent){
        targetId=intent.getData().getQueryParameter("targetId");
        targetIds=intent.getData().getQueryParameter("targetIds");
        conversationType=Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
        Log.i("ConversationType", conversationType.toString());
        new AsyncTask<String,String,String>(){

            @Override
            protected String doInBackground(String... params) {
                userInfo = RongCloudEvent.getInstance().getUserInfo(targetId);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                if (userInfo!=null)
                    bar.setTitle(userInfo.getName());
                super.onPostExecute(s);
            }
        }.execute();
        enterFragment(conversationType, targetId);

    }

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            ConversationActivity.this.finish();
        return super.onOptionsItemSelected(item);
    }
}
