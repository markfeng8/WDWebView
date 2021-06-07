package com.wdweblib.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.wdweblib.LoadView;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-26 16:12
 */
public class WDWebChromeClient extends WebChromeClient {

    private LoadView mLoadView;


    public WDWebChromeClient() {
        super();
    }

    @Override
    public void onExceededDatabaseQuota(String s, String s1, long l, long l1, long l2, WebStorage.QuotaUpdater quotaUpdater) {
        super.onExceededDatabaseQuota(s, s1, l, l1, l2, quotaUpdater);
        Log.i("WDWeb","onExceededDatabaseQuota");
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        Log.i("WDWeb","getDefaultVideoPoster");
        return super.getDefaultVideoPoster();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
        super.getVisitedHistory(valueCallback);
        Log.i("WDWeb","getVisitedHistory");
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.i("WDWeb","onConsoleMessage");
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onCreateWindow(WebView webView, boolean b, boolean b1, Message message) {
        Log.i("WDWeb","onCreateWindow");
        return super.onCreateWindow(webView, b, b1, message);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        Log.i("WDWeb","onGeolocationPermissionsHidePrompt");
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        Log.i("WDWeb","onGeolocationPermissionsShowPrompt");
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void onHideCustomView() {
        Log.i("WDWeb","onHideCustomView");
        super.onHideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        Log.i("WDWeb","onJsAlert");
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
        Log.i("WDWeb","onJsConfirm");
        return super.onJsConfirm(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
        Log.i("WDWeb","onJsPrompt");
        return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
    }

    @Override
    public boolean onJsBeforeUnload(WebView webView, String s, String s1, JsResult jsResult) {
        Log.i("WDWeb","onJsBeforeUnload");
        return super.onJsBeforeUnload(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsTimeout() {
        Log.i("WDWeb","onJsTimeout");
        return super.onJsTimeout();
    }

    @Override
    public void onProgressChanged(WebView webView, int newProgress) {
        Log.i("WDWeb","onProgressChanged");
        super.onProgressChanged(webView, newProgress);
        if (newProgress == 100) {
            if (mLoadView != null) {
                mLoadView.hide();
            }
        }
    }

    @Override
    public void onReachedMaxAppCacheSize(long l, long l1, WebStorage.QuotaUpdater quotaUpdater) {
        super.onReachedMaxAppCacheSize(l, l1, quotaUpdater);
        Log.i("WDWeb","onReachedMaxAppCacheSize");
    }

    @Override
    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
        super.onReceivedIcon(webView, bitmap);
        Log.i("WDWeb","onReceivedIcon");
    }

    @Override
    public void onReceivedTouchIconUrl(WebView webView, String s, boolean b) {
        super.onReceivedTouchIconUrl(webView, s, b);
        Log.i("WDWeb","onReceivedTouchIconUrl");
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
        Log.i("WDWeb","onReceivedTitle");
    }

    @Override
    public void onRequestFocus(WebView webView) {
        super.onRequestFocus(webView);
        Log.i("WDWeb","onRequestFocus");
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        super.onShowCustomView(view, customViewCallback);
        Log.i("WDWeb","onShowCustomView");
    }

    @Override
    public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        super.onShowCustomView(view, i, customViewCallback);
        Log.i("WDWeb","onShowCustomView");
    }

    @Override
    public void onCloseWindow(WebView webView) {
        super.onCloseWindow(webView);
        Log.i("WDWeb","onCloseWindow");
    }

    @Override
    public View getVideoLoadingProgressView() {
        Log.i("WDWeb","getVideoLoadingProgressView");
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        super.openFileChooser(valueCallback, s, s1);
        Log.i("WDWeb","openFileChooser");
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        Log.i("WDWeb","onShowFileChooser");
        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }

    @Override
    public void onPermissionRequest(PermissionRequest permissionRequest) {
        super.onPermissionRequest(permissionRequest);
        Log.i("WDWeb","onPermissionRequest");
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
        super.onPermissionRequestCanceled(permissionRequest);
        Log.i("WDWeb","onPermissionRequestCanceled");
    }

    public void setLoadView(LoadView mLoadView) {
        this.mLoadView = mLoadView;
    }

}
