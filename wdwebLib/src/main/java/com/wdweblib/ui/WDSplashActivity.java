package com.wdweblib.ui;

import android.content.Intent;
import android.os.CountDownTimer;

import com.wdweblib.BaseActivity;
import com.wdweblib.R;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-28 10:45
 */
public class WDSplashActivity extends BaseActivity {

    @Override
    protected int attachLayout() {
        return R.layout.wd_activity_splash;
    }

    @Override
    protected void initView() {
        new CountDownTimer(1000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WDSplashActivity.this,
                        WDMainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
