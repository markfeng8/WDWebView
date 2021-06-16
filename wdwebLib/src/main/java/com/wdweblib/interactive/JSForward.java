package com.wdweblib.interactive;

import android.content.Intent;
import android.os.Bundle;

import com.fragmentation.fragmentation.SupportActivity;
import com.google.gson.Gson;
import com.wdweblib.BaseFragment;
import com.wdweblib.Constants;
import com.wdweblib.bean.ForwardBean;
import com.wdweblib.ui.SingleFragment;
import com.wdweblib.ui.WDLoginActivity;
import com.wdweblib.ui.WDMainActivity;
import com.wdweblib.utils.StringUtils;
import com.wdweblib.web.WDWebView;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 09:30
 */
public class JSForward {

    private static Gson mGson;
    public static final int REQUESTCODE_FORWARD = 100;
    public static final int RESULTCODE_REFRESHURL_REFRESH = -2;
    public static final int RESULTCODE_REFRESHURL_RELOAD = -3;

    private JSForward() {
    }

    private static class JSForwardInstance {
        private static final JSForward INSTANCE = new JSForward();
    }

    public static JSForward getInstance() {
        return JSForwardInstance.INSTANCE;
    }

    public void doForward(SupportActivity _mActivity,
                          BaseFragment fragment,
                          WDWebView wdWebView,
                          String forwardJson) {
        if (mGson == null) {
            mGson = new Gson();
        }
        ForwardBean bean = mGson.fromJson(forwardJson, ForwardBean.class);
        String type = bean.getType();
        if ("H5".equals(type)) {
            gotoH5Page(_mActivity, fragment, wdWebView, bean);
        } else if ("Native".equals(type)) {
            gotoNativePage(_mActivity, fragment, wdWebView, bean);
        }
    }

    /**
     * 跳转至H5页面
     *
     * @param _mActivity
     * @param mBaseFragment
     * @param mWDWebView
     * @param bean
     */
    private void gotoH5Page(SupportActivity _mActivity,
                            BaseFragment mBaseFragment,
                            WDWebView mWDWebView,
                            ForwardBean bean) {

        //push/pop/present
        //跳转/回退/无层级关系(底部弹出),
        // 默认为push

        if ("push".equals(bean.getAnimate())) {
            _mActivity.extraTransaction()
                    .setTag(bean.getToPage())
                    .startForResult(
                            SingleFragment.newInstance(Constants.HOST + bean.getToPage()),
                            REQUESTCODE_FORWARD);
        } else if ("pop".equals(bean.getAnimate())) {
            //animate为pop时候toPage若为空字符串则直接返回；
            //若toPage非空跳转到指定界面，
            //refreshUrl非空且为*时以原url刷新页面，
            //若为url时加载当前url(重定向)
            if (!StringUtils.isNotEmpty(bean.getToPage())) {
                mBaseFragment.pop();
            } else if (StringUtils.isNotEmpty(bean.getToPage())) {
                mBaseFragment.extraTransaction()
                        .popTo(bean.getToPage(),
                                false);
            } else if ("*".equals(bean.getRefreshUrl())) {
                Bundle bundle = new Bundle();
                mBaseFragment.setFragmentResult(RESULTCODE_REFRESHURL_REFRESH, bundle);
                mBaseFragment.pop();
            } else if (StringUtils.isNotEmpty(bean.getRefreshUrl())) {
                Bundle bundle = new Bundle();
                bundle.putString("refreshUrl", bean.getRefreshUrl());
                mBaseFragment.setFragmentResult(RESULTCODE_REFRESHURL_RELOAD, bundle);
                mBaseFragment.pop();
            }
        } else if ("present".equals(bean.getAnimate())) {
            mWDWebView.reload();
        }
    }

    private void gotoNativePage(SupportActivity _mActivity,
                                BaseFragment mBaseFragment,
                                WDWebView mWDWebView,
                                ForwardBean bean) {

        if ("mainPage".equals(bean.getToPage())) {
            _mActivity.startActivity(new Intent(_mActivity, WDMainActivity.class));
            _mActivity.finish();
        } else if ("".equals(bean.getToPage())) {
            _mActivity.startActivity(new Intent(_mActivity, WDLoginActivity.class));
            _mActivity.finish();
        }

    }

}
