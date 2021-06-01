package com.wdweblib.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.fragmentation.fragmentation.SupportFragment;
import com.wdweblib.R;
import com.wdweblib.bean.TabListBean;
import com.wdweblib.widget.bottombar.BottomBar;
import com.wdweblib.widget.bottombar.BottomBarTab;

import java.util.ArrayList;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 16:59
 */
public class WDMainFragment extends SupportFragment {

    private BottomBar mBottomBar;
    private SupportFragment[] mFragments;

    private List<TabListBean> mTabList;
    private String mSelectColor;
    private String mUnselectColor;


    public static WDMainFragment newInstance(List<TabListBean> tabList,
                                             String selectColor,
                                             String unselectColor) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("tabConfig", (ArrayList<? extends Parcelable>) tabList);
        args.putString("selectColor", selectColor);
        args.putString("unselectColor", unselectColor);
        WDMainFragment fragment = new WDMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSelectColor = getArguments().getString("selectColor");
        mUnselectColor = getArguments().getString("unselectColor");
        mTabList = getArguments().getParcelableArrayList("tabConfig");

        mFragments = new SupportFragment[mTabList.size()];
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wd_fragment_main, container, false);
        initView(view);
        tabConfig(mTabList);
        return view;
    }


    private void tabConfig(List<TabListBean> list) {
        if (list == null) return;
        if (list.size() > 0) {
            TabListBean tabBean;
            for (int i = 0; i < list.size(); i++) {
                tabBean = list.get(i);
                if (tabBean.getTabPage().size() > 1) {
                    mFragments[i] = MulFragment.newInstance(
                            tabBean.getTabPage().get(0).getUrl());

                } else {
                    mFragments[i] = SingleFragment.newInstance(
                            tabBean.getTabPage().get(0).getUrl());
                }

                mBottomBar.addItem(new BottomBarTab(
                        _mActivity,
                        tabBean,
                        mSelectColor,
                        mUnselectColor));
            }

        }

        loadMultipleRootFragment(R.id.fl_tab_container, 0,
                mFragments);
    }

    private void initView(View view) {
        mBottomBar = view.findViewById(R.id.bottomBar);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}