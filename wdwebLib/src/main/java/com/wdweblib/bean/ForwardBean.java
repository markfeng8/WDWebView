package com.wdweblib.bean;

import com.google.gson.annotations.SerializedName;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 15:23
 */
public class ForwardBean<T> {

    private String type;
    private String toPage;
    private String refreshUrl;
    private String hasNavigation;
    @SerializedName("float")
    private String floatX;
    private String animate;
    private T params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToPage() {
        return toPage;
    }

    public void setToPage(String toPage) {
        this.toPage = toPage;
    }

    public String getRefreshUrl() {
        return refreshUrl;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
    }

    public String getHasNavigation() {
        return hasNavigation;
    }

    public void setHasNavigation(String hasNavigation) {
        this.hasNavigation = hasNavigation;
    }

    public String getFloatX() {
        return floatX;
    }

    public void setFloatX(String floatX) {
        this.floatX = floatX;
    }

    public String getAnimate() {
        return animate;
    }

    public void setAnimate(String animate) {
        this.animate = animate;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
