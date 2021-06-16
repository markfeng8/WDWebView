package com.wdweblib.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fragmentation.fragmentation.SupportFragment;
import com.google.android.material.tabs.TabLayout;
import com.wdweblib.BaseFragment;
import com.wdweblib.Constants;
import com.wdweblib.R;
import com.wdweblib.adapter.MulItemFragmentAdapter;
import com.wdweblib.bean.JSMessage;
import com.wdweblib.bean.MulItemFragmentBean;
import com.wdweblib.interactive.JSSetHeader;
import com.wdweblib.web.WDWebView;
import com.wdweblib.widget.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-27 13:23
 */
public class MulFragment extends SupportFragment {

    private BaseFragment mBaseFragment;
    private WDWebView mWDWebView;
    private TitleBar mTitleBar;
    private TabLayout mTab;
    private ViewPager mViewPager;
    private MulItemFragmentAdapter adapter;

    private List<MulItemFragmentBean> mList;

    public static MulFragment newInstance(List<MulItemFragmentBean> list) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("titleList", (ArrayList<? extends Parcelable>) list);
        MulFragment fragment = new MulFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getArguments().getParcelableArrayList("titleList");
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.wd_fragment_mul, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mTab = view.findViewById(R.id.tab);
        mViewPager = view.findViewById(R.id.viewPager);
        mTitleBar = view.findViewById(R.id.bar_title);

        adapter = new MulItemFragmentAdapter(getChildFragmentManager(), mList);
        mViewPager.setAdapter(adapter);
        mTab.setupWithViewPager(mViewPager);
    }


    /**
     * mulitemfragment调用此方法，设置setHeader中
     * 需要传递的webview对象
     *
     * @param wdWebView
     */
    public void setWDWebView(WDWebView wdWebView) {
        this.mWDWebView = wdWebView;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jsMsgEvent(JSMessage msg) {
        //当前fragment是可见的fragment的时候才响应
        if (getCurVisibleFragment() == this) {
            if (Constants.TYPE_SETHEADER.equals(msg.getType())) {
                JSSetHeader.getInstance().setHeader(
                        mBaseFragment,
                        mWDWebView,
                        mTitleBar,
                        msg.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        MulItemFragment itemFragment = (MulItemFragment) adapter.getItem(mViewPager.getCurrentItem());
        itemFragment.onFragmentResult(requestCode, resultCode, data);
    }
}
