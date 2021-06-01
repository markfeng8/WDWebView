package com.wdweblib.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wdweblib.ui.MulItemFragment;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class MulItemFragmentAdapter extends FragmentPagerAdapter {

    private String[] mTitles;

    public MulItemFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return MulItemFragment.newInstance("http://10.2.98.113:8889/");
        }else {
            return MulItemFragment.newInstance("https://www.csdn.net/");
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
