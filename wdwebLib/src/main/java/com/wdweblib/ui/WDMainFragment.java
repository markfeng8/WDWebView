package com.wdweblib.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fragmentation.fragmentation.SupportFragment;
import com.wdweblib.BaseFragment;
import com.wdweblib.R;
import com.wdweblib.bean.MulItemFragmentBean;
import com.wdweblib.bean.TabListBean;
import com.wdweblib.utils.StringUtils;
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

    private TabChangeImp tabChangeImp;

    public interface TabChangeImp {
        void tabChange(int position, int prePosition);
    }

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tabChangeImp = (TabChangeImp) activity;
    }

    private void tabConfig(List<TabListBean> list) {
        if (list == null) return;
        if (list.size() > 0) {
            TabListBean tabBean;
            for (int i = 0; i < list.size(); i++) {
                tabBean = list.get(i);
                if (tabBean.getTabPage().size() > 1) {
                    List<MulItemFragmentBean> mulItemList = tabBean.getMulItemList();
                    mFragments[i] = MulFragment.newInstance(
                            mulItemList);
                } else {
                    //支持两种类型：一种是使用SingleFragment加载url，
                    //一种是加载自己定义的fragment作为一级页面
                    String url = tabBean.getTabPage().get(0).getUrl();
                    if (StringUtils.isNotEmpty(url)
                            && url.startsWith("http")) {
                        mFragments[i] = SingleFragment.newInstance(url);
                    } else {
                        try {
                            Class clz = Class.forName(url);
                            SupportFragment frag = (SupportFragment) clz.newInstance();
                            mFragments[i] = frag;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (java.lang.InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
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
                if (tabChangeImp != null) {
                    tabChangeImp.tabChange(position, prePosition);
                }
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

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        //forward跳转的时候，设置了startForResult。会返回到这里，但是逻辑
        //统一在BaseFramengt里处理

        List<Fragment> list = getChildFragmentManager().getFragments();
        for (Fragment baseFragment : list) {
            if (baseFragment != null
                    && baseFragment instanceof SingleFragment) {
                if (baseFragment.isVisible()
                        && baseFragment instanceof BaseFragment) {
                    ((BaseFragment) baseFragment).onFragmentResult(requestCode, resultCode, data);
                }
            } else if (baseFragment != null
                    && baseFragment instanceof MulFragment) {
                ((MulFragment) baseFragment).onFragmentResult(requestCode, resultCode, data);
            }
        }

    }
}
