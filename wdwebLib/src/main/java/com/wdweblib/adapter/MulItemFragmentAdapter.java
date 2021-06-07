package com.wdweblib.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wdweblib.bean.MulItemFragmentBean;
import com.wdweblib.ui.MulItemFragment;

import java.util.List;

/**
 * Created by YoKeyword on 16/6/5.
 */
public class MulItemFragmentAdapter extends FragmentPagerAdapter {

    private List<MulItemFragmentBean> mList;

    public MulItemFragmentAdapter(FragmentManager fm, List<MulItemFragmentBean> list) {
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return MulItemFragment.newInstance(mList.get(position).getUrl());
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getTitle();
    }
}
