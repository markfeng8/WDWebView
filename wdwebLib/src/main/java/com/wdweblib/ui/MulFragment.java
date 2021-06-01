package com.wdweblib.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fragmentation.fragmentation.SupportFragment;
import com.google.android.material.tabs.TabLayout;
import com.wdweblib.Constants;
import com.wdweblib.R;
import com.wdweblib.adapter.MulItemFragmentAdapter;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-27 13:23
 */
public class MulFragment extends SupportFragment {

    private TabLayout mTab;
    private ViewPager mViewPager;

    private String mUrl;

    public static MulFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.FLAG_JUMP_URL, url);

        MulFragment fragment = new MulFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(Constants.FLAG_JUMP_URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wd_fragment_mul, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mTab = (TabLayout) view.findViewById(R.id.tab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());

        mViewPager.setAdapter(new MulItemFragmentAdapter(getChildFragmentManager(),
                "test1",
                "test2"));
        mTab.setupWithViewPager(mViewPager);
    }
}
