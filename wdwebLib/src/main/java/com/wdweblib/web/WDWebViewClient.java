package com.wdweblib.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
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
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        return super.shouldOverrideUrlLoading(webView, s);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
    }

    @Override
    public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
        if (mLoadView != null) {
            mLoadView.loading();
        }
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
    }

    @Override
    public void onReceivedError(WebView webView, int i, String s, String s1) {
        super.onReceivedError(webView, i, s, s1);
        if (mLoadView != null) {
            mLoadView.showError();
        }
    }

    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
    }

    @Override
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, String s) {
        return super.shouldInterceptRequest(webView, s);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        return super.shouldInterceptRequest(webView, webResourceRequest);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest, Bundle bundle) {
        return super.shouldInterceptRequest(webView, webResourceRequest, bundle);
    }

    @Override
    public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
        super.doUpdateVisitedHistory(webView, s, b);
    }

    @Override
    public void onFormResubmission(WebView webView, Message message, Message message1) {
        super.onFormResubmission(webView, message, message1);
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String s, String s1) {
        super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
    }

    @Override
    public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
        super.onReceivedClientCertRequest(webView, clientCertRequest);
    }

    @Override
    public void onScaleChanged(WebView webView, float v, float v1) {
        super.onScaleChanged(webView, v, v1);
    }

    @Override
    public void onUnhandledKeyEvent(WebView webView, KeyEvent keyEvent) {
        super.onUnhandledKeyEvent(webView, keyEvent);
    }

    @Override
    public boolean shouldOverrideKeyEvent(WebView webView, KeyEvent keyEvent) {
        return super.shouldOverrideKeyEvent(webView, keyEvent);
    }

    @Override
    public void onTooManyRedirects(WebView webView, Message message, Message message1) {
        super.onTooManyRedirects(webView, message, message1);
    }

    @Override
    public void onReceivedLoginRequest(WebView webView, String s, String s1, String s2) {
        super.onReceivedLoginRequest(webView, s, s1, s2);
    }

    @Override
    public void onDetectedBlankScreen(String s, int i) {
        super.onDetectedBlankScreen(s, i);
    }

    @Override
    public void onPageCommitVisible(WebView webView, String s) {
        super.onPageCommitVisible(webView, s);
    }



    public void setLoadView(LoadView mLoadView) {
        this.mLoadView = mLoadView;
    }
}
