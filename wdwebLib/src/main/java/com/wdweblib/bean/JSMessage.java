package com.wdweblib.bean;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-28 17:18
 */
public class JSMessage {
    private String type;
    private String message;

    public JSMessage(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
