package com.acce.rongtest.listener;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.acce.rongtest.activity.UserActivity;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by acce-3 on 2015/10/31.
 */
public class AcceConversationBehaviorListener implements RongIM.ConversationBehaviorListener  {
    //头像点击事件
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (userInfo.getUserId().equals(RongIM.getInstance().getRongIMClient().getCurrentUserId())){
            Intent it=new Intent(context, UserActivity.class);
            context.startActivity(it);
        }
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
}
