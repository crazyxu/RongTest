package com.acce.rongtest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-20
 * Time: 08:39
 * FIXME
 */
public class MyStatePagerAdapter extends FragmentStatePagerAdapter {
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private List<Fragment> listFra;
    private String[] arrTitle;
    public MyStatePagerAdapter(FragmentManager fm){
        super(fm);
    }
    public MyStatePagerAdapter(String[] arrTitle, List<Fragment> listFra, FragmentManager fm){
        super(fm);
        this.listFra=listFra;
        this.arrTitle=arrTitle;

    }
    @Override
    public Fragment getItem(int position) {
        return listFra.get(position);
    }

    @Override
    public int getCount() {
        if (listFra!=null)
            return listFra.size();
        else
            return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrTitle[position];
    }
}
