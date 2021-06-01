package com.wdwebview;

import android.app.Application;

import com.fragmentation.fragmentation_core.Fragmentation;
import com.fragmentation.fragmentation_core.helper.ExceptionHandler;
import com.wdweblib.WDWebSdk;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-27 13:46
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        WDWebSdk.getInstance().init(this);

    }
}
