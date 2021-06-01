package com.wdweblib.interactive;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 09:30
 */
public class JSNativePermission {

    private JSNativePermission() {
    }

    private static class JSNativePermissionInstance {
        private static final JSNativePermission INSTANCE = new JSNativePermission();
    }

    public static JSNativePermission getInstance() {
        return JSNativePermission.JSNativePermissionInstance.INSTANCE;
    }
}
