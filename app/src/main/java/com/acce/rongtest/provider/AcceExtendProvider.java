package com.acce.rongtest.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.acce.rongtest.activity.AcceOrderActivity;
import com.acce.rongtest.message.AcceOrderMessage;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;

/**
 * Created by acce-3 on 2015/10/28.
 */
public class AcceExtendProvider extends InputProvider.ExtendProvider  {
    private int REQUEST_ACCE=10;
    public AcceExtendProvider(RongContext context){
        super(context);
    }

    @Override
    public Drawable obtainPluginDrawable(Context context) {
        return context.getResources().getDrawable(android.R.drawable.ic_input_add);
    }

    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return "创建订单";
    }

    @Override
    public void onPluginClick(View view) {
        Log.i("--conversationType--", getCurrentConversation().getConversationType().getName());
        Log.i("-----targetId------", getCurrentConversation().getTargetId());
        Intent it =new Intent(getContext(), AcceOrderActivity.class);
        startActivityForResult(it, REQUEST_ACCE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK){
            String orderNo=data.getStringExtra("orderNo");
            String orderName=data.getStringExtra("orderName");
            AcceOrderMessage orderMessage = new AcceOrderMessage(orderNo,orderName);
            /**
             * 发送消息。
             *
             * @param type        会话类型。
             * @param targetId    目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
             * @param content     消息内容。
             * @param pushContent push 时提示内容，为空时提示文本内容。
             * @param callback    发送消息的回调。
             * @return
             */
            RongIM.getInstance().getRongIMClient().sendMessage(getCurrentConversation().getConversationType(),getCurrentConversation().getTargetId(),orderMessage, "您收到一份订单", "", new RongIMClient.SendMessageCallback() {
                @Override
                public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                }

                @Override
                public void onSuccess(Integer integer) {

                }
            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
