package com.wdweblib.web;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.wdweblib.LoadView;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-24 16:36
 */
public class WDWebView extends WebView implements LoadView {

    private LoadView mLoadView;
    private WDWebViewClient mWDWebViewClient;
    private WDWebChromeClient mWDWebChromeClient;

    public WDWebView(@NonNull Context context) {
        super(getFixedContext(context));
        init(context);
    }

    public WDWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(getFixedContext(context), attrs);
        init(context);
    }

    public WDWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {


        mWDWebViewClient = new WDWebViewClient();
        mWDWebChromeClient = new WDWebChromeClient();

        addJavascriptInterface(new JSInterface(context, this), "WDAndroid");

        //声明WebSettings子类
        WebSettings webSettings = getSettings();

        String ua = webSettings.getUserAgentString();
        // 设置用户 Agent 标识
        if (ua.contains("android_appName")) {
            String[] uas = ua.split("android_appName");
            ua = uas[0];
        }
        webSettings.setUserAgentString(ua + " android_smartidata");

        // 设置支持 JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 是否阻塞加载网络图片
        webSettings.setBlockNetworkImage(false);
        // 设置文字编码方式
        webSettings.setDefaultTextEncodingName("UTF-8");
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //设置缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 设置定位服务可用
        webSettings.setGeolocationEnabled(true);
        // 禁止使用 File 域
        webSettings.setAllowFileAccess(false);
        // 显式移除有风险的 WebView 系统隐藏接口
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");

        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //设置自适应屏幕，两者合用
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存

        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //不使用缓存:
        //WebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        // enable:true(日间模式)，enable：false（夜间模式）
//        getSettingsExtension().setDayOrNight(true);
        setWebViewClient(mWDWebViewClient);
        setWebChromeClient(mWDWebChromeClient);
    }

    public void loadUrl_(String url, LoadView loadView) {
//        loadDataWithBaseURL(null, url, "text/html", "UTF-8", null);
        loadUrl(url);
        mLoadView = loadView;
        mWDWebViewClient.setLoadView(mLoadView);
        mWDWebChromeClient.setLoadView(mLoadView);
    }

    @Override
    public void loading() {
        if (mLoadView != null) {
            mLoadView.loading();
        }
    }

    @Override
    public void hide() {
        if (mLoadView != null) {
            mLoadView.hide();
        }
    }

    @Override
    public void showError() {
        if (mLoadView != null) {
            mLoadView.showError();
        }
    }

    private static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return context.createConfigurationContext(new Configuration());
        } else {
            return context;
        }
    }

    /**
     * 调用js方法
     *
     * @param callBackMethod
     * @param params
     */
    public void callJs(String callBackMethod, String params) {
        evaluateJavascript("javascript:executeJS('" + callBackMethod + "','" + params + "')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {

            }
        });
    }
}
