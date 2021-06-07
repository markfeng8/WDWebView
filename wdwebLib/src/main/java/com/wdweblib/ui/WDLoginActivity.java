package com.wdweblib.ui;

import com.wdweblib.BaseActivity;
import com.wdweblib.R;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 17:31
 */
public class WDLoginActivity extends BaseActivity {

    @Override
    protected int attachLayout() {
        return R.layout.wd_activity_login;
    }

    @Override
    protected void initView() {
        loadRootFragment(
                R.id.fl_container_login,
                WDLoginFragment.newInstance("http://dict.youdao.com/search"));
    }
}
