package com.wdweblib.web;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;

import com.tencent.smtt.export.external.interfaces.ClientCertRequest;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wdweblib.LoadView;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-26 16:11
 */
public class WDWebViewClient extends WebViewClient {

    private LoadView mLoadView;



    public WDWebViewClient() {
        super();
    }

    @Override
    public void onLoadResource(WebView webView, String s) {
        super.onLoadResource(webView, s);
        Log.i("WDWeb","onLoadResource");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        Log.i("WDWeb","shouldOverrideUrlLoading");
        return super.shouldOverrideUrlLoading(webView, s);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        Log.i("WDWeb","shouldOverrideUrlLoading");
        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
    }

    @Override
    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
        if (mLoadView != null) {
            mLoadView.loading();
        }
        Log.i("WDWeb","onPageStarted");
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        Log.i("WDWeb","onPageFinished");
        String authToken="123456";
        String js = "window.localStorage.setItem('wondersAuthToken','" + authToken + "');";
        String jsUrl = "javascript:(function({ var localStorage = window.localStorage; localStorage.setItem('wondersAuthToken','" + authToken + "') })()";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl(jsUrl);
            webView.reload();
        }
    }

    @Override
    public void onReceivedError(WebView webView, int i, String s, String s1) {
        super.onReceivedError(webView, i, s, s1);
        if (mLoadView != null) {
            mLoadView.showError();
        }
        Log.i("WDWeb","onReceivedError");
    }

    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        if (mLoadView != null) {
            mLoadView.showError();
        }
        Log.i("WDWeb","onReceivedError");
    }

    @Override
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        Log.i("WDWeb","onReceivedHttpError");
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        Log.i("WDWeb","shouldInterceptRequest");
        return super.shouldInterceptRequest(webView, s);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        Log.i("WDWeb","shouldInterceptRequest");
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
        Log.i("WDWeb","shouldInterceptRequest");
        return super.shouldInterceptRequest(webView, webResourceRequest, bundle);
    }

    @Override
    public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
        super.doUpdateVisitedHistory(webView, s, b);
        Log.i("WDWeb","doUpdateVisitedHistory");
    }

    @Override
    public void onFormResubmission(WebView webView, Message message, Message message1) {
        super.onFormResubmission(webView, message, message1);
        Log.i("WDWeb","onFormResubmission");
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String s, String s1) {
        super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1);
        Log.i("WDWeb","onReceivedHttpAuthRequest");
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        Log.i("WDWeb","onReceivedSslError");
    }

    @Override
    public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
        super.onReceivedClientCertRequest(webView, clientCertRequest);
        Log.i("WDWeb","onReceivedClientCertRequest");
    }

    @Override
    public void onScaleChanged(WebView webView, float v, float v1) {
        super.onScaleChanged(webView, v, v1);
        Log.i("WDWeb","onScaleChanged");
    }

    @Override
    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
        super.onUnhandledKeyEvent(webView, keyEvent);
        Log.i("WDWeb","onUnhandledKeyEvent");
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        Log.i("WDWeb","shouldOverrideKeyEvent");
        return super.shouldOverrideKeyEvent(webView, keyEvent);
    }

    @Override
    public void onTooManyRedirects(WebView webView, Message message, Message message1) {
        super.onTooManyRedirects(webView, message, message1);
        Log.i("WDWeb","onTooManyRedirects");
    }

    @Override
    public void onReceivedLoginRequest(WebView webView, String s, String s1, String s2) {
        super.onReceivedLoginRequest(webView, s, s1, s2);
        Log.i("WDWeb","onReceivedLoginRequest");
    }

    @Override
    public void onDetectedBlankScreen(String s, int i) {
        super.onDetectedBlankScreen(s, i);
        Log.i("WDWeb","onDetectedBlankScreen");
    }

    @Override
    public void onPageCommitVisible(WebView webView, String s) {
        super.onPageCommitVisible(webView, s);
        Log.i("WDWeb","onPageCommitVisible");
    }



    public void setLoadView(LoadView mLoadView) {
        this.mLoadView = mLoadView;
    }
}
