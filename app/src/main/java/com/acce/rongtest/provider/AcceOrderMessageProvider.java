package com.acce.rongtest.provider;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acce.rongtest.R;
import com.acce.rongtest.message.AcceOrderMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by acce-3 on 2015/10/28.
 */
@ProviderTag(messageContent=AcceOrderMessage.class)
public class AcceOrderMessageProvider extends IContainerItemProvider.MessageProvider<AcceOrderMessage> {
    class ViewHolder{
        TextView tvOrderName;
        TextView tvOrderNo;
        LinearLayout llOrder;
    }

    @Override
    public void bindView(View view, int i, AcceOrderMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llOrder.setBackgroundResource(R.drawable.bubble_blue_right);
        } else {
            holder.llOrder.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.tvOrderName.setText(content.getOrderName());
        holder.tvOrderNo.setText(content.getOrderNo());
        //AndroidEmoji.ensure((Spannable) holder.tvOrderName.getText());//显示消息中的 Emoji 表情。
        //AndroidEmoji.ensure((Spannable) holder.tvOrderNo.getText());//显示消息中的 Emoji 表情。
    }

    @Override
    public Spannable getContentSummary(AcceOrderMessage acceOrderMessage) {
        return new SpannableString("一个订单消息");
    }

    @Override
    public void onItemClick(View view, int i, AcceOrderMessage acceOrderMessage, UIMessage uiMessage) {
        Toast.makeText(view.getContext(),"click...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View view, int i, AcceOrderMessage acceOrderMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_acce_order_message,null);
        ViewHolder holder=new ViewHolder();
        holder.tvOrderName=(TextView)view.findViewById(R.id.tv_acce_message_item_order_name);
        holder.tvOrderNo=(TextView)view.findViewById(R.id.tv_acce_message_item_order_no);
        holder.llOrder=(LinearLayout)view.findViewById(R.id.ll_acce_message_order);
        view.setTag(holder);
        return view;
    }
}
