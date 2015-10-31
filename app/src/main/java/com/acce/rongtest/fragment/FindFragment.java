package com.acce.rongtest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;


public class FindFragment extends Fragment {

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
