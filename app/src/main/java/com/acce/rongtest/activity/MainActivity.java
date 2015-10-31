package com.acce.rongtest.activity;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.acce.rongtest.R;
import com.acce.rongtest.adapter.MyStatePagerAdapter;
import com.acce.rongtest.fragment.ContactFragment;
import com.acce.rongtest.fragment.FindFragment;
import com.acce.rongtest.fragment.UserFragment;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

public class MainActivity extends AppCompatActivity {
    private String[] arrTitle=new String[]{"消息","通讯录","发现","我"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyStatePagerAdapter pagerAdapter;
    private List<Fragment> listFra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    void initView(){
        tabLayout=(TabLayout)super.findViewById(R.id.main_tab_layout);
        viewPager=(ViewPager)super.findViewById(R.id.main_viewpager);

        listFra=new ArrayList<>();
        //会话列表
        ConversationListFragment fragment =new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();

        fragment.setUri(uri);

        //消息
        listFra.add(fragment);
        listFra.add(ContactFragment.newInstance());
        //发现
        listFra.add(FindFragment.newInstance());
        //个人
        listFra.add(UserFragment.newInstance());
        //需要包含v13包
        pagerAdapter=new MyStatePagerAdapter(arrTitle,listFra,getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_add_friend){
            //添加好友
        }
        return super.onOptionsItemSelected(item);
    }
}
