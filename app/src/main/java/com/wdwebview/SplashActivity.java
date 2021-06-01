package com.wdwebview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;

import com.wdweblib.ui.WDMainActivity;
import com.wdweblib.ui.WDSplashActivity;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 12:09
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wd_activity_splash);
        new CountDownTimer(1000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
