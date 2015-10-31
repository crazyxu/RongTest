package com.acce.rongtest.activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.acce.rongtest.R;
import com.acce.rongtest.RongCloudEvent;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

public class ConversationActivity extends AppCompatActivity {
    /**
     *targetId
     */
    private String targetId;
    /**
     * conversationType
     */
    private String targetIds;
    private Conversation.ConversationType conversationType;
    private Toolbar bar;

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
        Log.i("ConversationType",conversationType.toString());
        bar.setTitle(RongCloudEvent.getInstance().getUserInfo(targetId).getName());
        enterFragment(conversationType, targetId);

    }

    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

}
