package com.wdweblib.bean;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 11:20
 */
public class NativeBean<T> {

    private String type;
    private T params;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
