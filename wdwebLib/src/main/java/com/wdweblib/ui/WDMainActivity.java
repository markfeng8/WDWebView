package com.wdweblib.ui;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdweblib.BaseActivity;
import com.wdweblib.Constants;
import com.wdweblib.R;
import com.wdweblib.bean.BottomBarBean;
import com.wdweblib.bean.JSMessage;
import com.wdweblib.bean.TabListBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-26 17:11
 */
public abstract class WDMainActivity extends BaseActivity implements WDMainFragment.TabChangeImp {

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private List<TabListBean> mTabList;
    private String mSelectColor;
    private String mUnselectColor;
    private Gson mGson;

    @Override
    protected int attachLayout() {
        return R.layout.wd_activity_wd_main;
    }

    @Override
    protected void initView() {
        mGson = new Gson();
        EventBus.getDefault().register(this);
        mTabList = tabConfig();
        if (mTabList == null) {
            throw new NullPointerException("请继承WDMainActivity，并且重写tabConfig方法，实现底部导航栏的配置！");
        }
        loadRootFragment(R.id.fl_container,
                WDMainFragment.newInstance(mTabList, selectedTextColor(), unselectedTextColor()));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void jsMsgEvent(JSMessage msg) {
        //当前fragment是栈顶fragment的时候才响应
        if (getTopFragment() == this) {
            Log.i("jsMsgEvent", msg.toString());


            BottomBarBean bean = mGson.fromJson(msg.getMessage(), BottomBarBean.class);
            if (bean != null
                    && Constants.TYPE_TABCONFIG.equals(bean.getType())) {
                List<TabListBean> tabList = bean.getParams().getTabList();
                String selectColor = bean.getParams().getSelectedTextColor();
                String unselectColor = bean.getParams().getUnselectedTextColor();

                loadRootFragment(R.id.fl_container,
                        WDMainFragment.newInstance(tabList, selectColor, unselectColor));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected abstract ArrayList<TabListBean> tabConfig();

    protected abstract String selectedTextColor();

    protected abstract String unselectedTextColor();

}
