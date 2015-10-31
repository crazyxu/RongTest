package com.acce.rongtest.message;

import android.os.Parcel;

import com.sea_monster.common.ParcelUtils;

import org.json.JSONObject;

import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by acce-3 on 2015/10/28.
 */
@MessageTag(value = "AC:OrderMsg",flag= MessageTag.ISCOUNTED| MessageTag.ISPERSISTED)
public class AcceOrderMessage extends MessageContent {
    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderName() {
        return orderName;
    }


    private String orderNo;
    private String orderName;
    public AcceOrderMessage(String orderNo, String orderName){
        this.orderName=orderName;
        this.orderNo=orderNo;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("orderName",orderName);
            jsonObject.put("orderNo",orderNo);
            return jsonObject.toString().getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AcceOrderMessage(byte[] data){
        try {
            String jsonStr=new String(data,"UTF-8");
            JSONObject jsonObject=new JSONObject(jsonStr);
            orderName=jsonObject.optString("orderName");
            orderNo=jsonObject.optString("orderNo");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //给消息赋值。
    public AcceOrderMessage(Parcel in) {
        orderName= ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        orderNo= ParcelUtils.readFromParcel(in);//该类为工具类，消息属性
        //这里可继续增加你消息的属性
    }

    /**
     * 读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。
     */
    public static final Creator<AcceOrderMessage> CREATOR = new Creator<AcceOrderMessage>() {

        @Override
        public AcceOrderMessage createFromParcel(Parcel source) {
            return new AcceOrderMessage(source);
        }

        @Override
        public AcceOrderMessage[] newArray(int size) {
            return new AcceOrderMessage[size];
        }
    };

    /**
     * 描述了包含在 Parcelable 对象排列信息中的特殊对象的类型。
     *
     * @return 一个标志位，表明Parcelable对象特殊对象类型集合的排列。
     */
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, orderName);
        ParcelUtils.writeToParcel(dest, orderNo);

    }
}
