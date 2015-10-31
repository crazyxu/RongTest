package com.acce.rongtest.provider;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.widget.provider.IContainerItemProvider;

/**
 * Created by acce-3 on 2015/10/31.
 */
@ConversationProviderTag(conversationType = "private", portraitPosition = 1)
public class AccePrivateConversationProvider implements IContainerItemProvider.ConversationProvider {


    @Override
    public String getTitle(String s) {
        return "来自："+s;
    }

    @Override
    public Uri getPortraitUri(String s) {
        if (TextUtils.isEmpty(s)){
            return Uri.parse(s);
        }
        return null;
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, int i, Parcelable parcelable) {

    }
}
