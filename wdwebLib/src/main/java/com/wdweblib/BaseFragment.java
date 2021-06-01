package com.wdweblib;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.fragmentation.fragmentation.SupportFragment;
import com.wdweblib.bean.JSMessage;
import com.wdweblib.interactive.JSSetHeader;
import com.wdweblib.web.WDWebView;
import com.wdweblib.widget.TitleBar;
import com.wdweblib.widget.WebLoadHintView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-27 14:01
 */
public class BaseFragment extends SupportFragment implements LoadView {

    @Nullable
    protected TitleBar mTitleBar;
    @Nullable
    protected WDWebView mWDWebView;
    @Nullable
    protected WebLoadHintView mWebLoadHintView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void loading() {
        if (mWebLoadHintView != null) {
            mWebLoadHintView.loading(_mActivity);
        }
    }

    @Override
    public void hide() {
        if (mWebLoadHintView != null) {
            mWebLoadHintView.hideLoad();
        }
    }

    @Override
    public void showError() {
        if (mWebLoadHintView != null) {
            mWebLoadHintView.showError(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWDWebView != null) {
                        mWDWebView.reload();
                    }
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jsMsgEvent(JSMessage msg) {
        //当前fragment是栈顶fragment的时候才响应
        if (getTopFragment() == this) {
            if (Constants.TYPE_SETHEADER.equals(msg.getType())) {
                JSSetHeader.getInstance().setHeader(
                        this,
                        mWDWebView,
                        mTitleBar,
                        msg.getMessage());
            } else if (Constants.TYPE_FORWARD.equals(msg.getType())) {

            } else if (Constants.TYPE_NATIVEPERMISSION.equals(msg.getType())) {

            }
//            if (getParentFragment() instanceof SingleFragment
//                    || getTopFragment() instanceof SingleFragment) {
////                start(SingleFragment.newInstance("https://www.baidu.com/"));
//
//            } else if (getParentFragment() instanceof MulFragment) {
//                ((MulFragment) getParentFragment())
//                        .start(MulItemFragment.newInstance("https://www.baidu.com/"));
//            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mWDWebView.callJs("onVisible", "");
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        mWDWebView.callJs("onInvisible", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWDWebView.callJs("onDestory", "");
    }
}
