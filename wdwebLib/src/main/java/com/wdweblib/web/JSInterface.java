package com.wdweblib.web;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.wdweblib.Constants;
import com.wdweblib.bean.JSMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-24 16:40
 */
public class JSInterface extends Object {

    private Context mContext;
    private WDWebView mWDWebView;

    public JSInterface(Context context, WDWebView wdWebView) {
        mContext = context;
        mWDWebView = wdWebView;
    }

    @JavascriptInterface
    public void forward(String data) {
        Log.i("JSInterface", "forward==" + data);
        JSMessage msg = new JSMessage(Constants.TYPE_FORWARD, data);
        EventBus.getDefault().post(msg);
        Toast.makeText(mContext, "forward：" + data, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void setHeader(String data) {
        Log.i("JSInterface", "setHeader==" + data);
        JSMessage msg = new JSMessage(Constants.TYPE_SETHEADER, data);
        EventBus.getDefault().post(msg);
        Toast.makeText(mContext, "setHeader：" + data, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void nativePermission(String data) {
        JSMessage msg = new JSMessage(Constants.TYPE_NATIVEPERMISSION, data);
        EventBus.getDefault().post(msg);
        Log.i("JSInterface", "nativePermission==" + data);
        Toast.makeText(mContext, "nativePermission：" + data, Toast.LENGTH_SHORT).show();
    }

}
