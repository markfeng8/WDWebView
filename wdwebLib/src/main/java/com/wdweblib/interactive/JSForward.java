package com.wdweblib.interactive;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 09:30
 */
public class JSForward {

    private JSForward() {}

    private static class JSForwardInstance {
        private static final JSForward INSTANCE = new JSForward();
    }

    public static JSForward getInstance() {
        return JSForwardInstance.INSTANCE;
    }

}
