package com.wdweblib.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
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
    }

    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    @Override
    public void getVisitedHistory(ValueCallback<String[]> valueCallback) {
        super.getVisitedHistory(valueCallback);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onCreateWindow(WebView webView, boolean b, boolean b1, Message message) {
        return super.onCreateWindow(webView, b, b1, message);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String s, GeolocationPermissionsCallback geolocationPermissionsCallback) {
        super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
    }

    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    @Override
    public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsAlert(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsConfirm(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
        return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
    }

    @Override
    public boolean onJsBeforeUnload(WebView webView, String s, String s1, JsResult jsResult) {
        return super.onJsBeforeUnload(webView, s, s1, jsResult);
    }

    @Override
    public boolean onJsTimeout() {
        return super.onJsTimeout();
    }

    @Override
    public void onProgressChanged(WebView webView, int newProgress) {
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
    }

    @Override
    public void onReceivedIcon(WebView webView, Bitmap bitmap) {
        super.onReceivedIcon(webView, bitmap);
    }

    @Override
    public void onReceivedTouchIconUrl(WebView webView, String s, boolean b) {
        super.onReceivedTouchIconUrl(webView, s, b);
    }

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
    }

    @Override
    public void onRequestFocus(WebView webView) {
        super.onRequestFocus(webView);
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        super.onShowCustomView(view, customViewCallback);
    }

    @Override
    public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
        super.onShowCustomView(view, i, customViewCallback);
    }

    @Override
    public void onCloseWindow(WebView webView) {
        super.onCloseWindow(webView);
    }

    @Override
    public View getVideoLoadingProgressView() {
        return super.getVideoLoadingProgressView();
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
        super.openFileChooser(valueCallback, s, s1);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        return super.onShowFileChooser(webView, valueCallback, fileChooserParams);
    }

    @Override
    public void onPermissionRequest(PermissionRequest permissionRequest) {
        super.onPermissionRequest(permissionRequest);
    }

    @Override
    public void onPermissionRequestCanceled(PermissionRequest permissionRequest) {
        super.onPermissionRequestCanceled(permissionRequest);
    }

    public void setLoadView(LoadView mLoadView) {
        this.mLoadView = mLoadView;
    }

}
