package com.acce.rongtest.db.control;

import android.content.Context;

import com.acce.rongtest.db.DBManager;
import com.acce.rongtest.db.WhereEntity;
import com.acce.rongtest.db.entities.UserInfoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acce-3 on 2015/11/2.
 */
public class UserInfoControl {
    private Context mContext;
    private DBManager mDBManager;

    public UserInfoControl(Context context) {
        this.mContext = context;
        mDBManager = new DBManager(context);
    }

    public UserInfoData getUserInfoById(String userId){
        UserInfoData result = null;
        List<WhereEntity> arrEntity = new ArrayList<>();
        //userId对应字段为userName（暂时处理）
        WhereEntity entity = new WhereEntity("username","=",userId);
        arrEntity.add(entity);
        List<UserInfoData> list = mDBManager.findRecord(UserInfoData.class,arrEntity);
        if(list!=null && list.size()>0){
            result = list.get(0);
        }

        return result;
    }

    public void saveUserInfo(UserInfoData userInfoData){
        mDBManager.insertRecord(userInfoData);
    }
}
