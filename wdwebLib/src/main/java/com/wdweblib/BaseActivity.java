package com.wdweblib;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.fragmentation.fragmentation.SupportActivity;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-26 17:11
 */
public abstract class BaseActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(attachLayout());
        initView();
    }

    protected abstract int attachLayout();

    protected abstract void initView();
}
