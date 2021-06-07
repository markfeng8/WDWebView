package com.wdweblib.bean.nativeparam;

import android.app.Activity;
import android.webkit.CookieManager;

import com.wdweblib.Constants;
import com.wdweblib.bean.NativeBean;
import com.wdweblib.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-04 16:34
 */
public class NativeStorageParam extends NativeBean<NativeStorageParam> {


    private String method;
    private String key;
    private String value;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String backJsonStr(Activity activity) {
        try {
            JSONObject jsonObject = new JSONObject();
            if ("get".equals(method)) {
                String value = (String) SPUtils.get(activity,
                        key, "");
                jsonObject.put(key, value);
            } else if ("set".equals(getMethod())) {
                SPUtils.put(activity, getKey(), getValue());
                if (Constants.KEY_REFRESHTOKEN.equals(key)) {
                    //登录成功保存token到本地的时候同步缓存下cookie
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.setAcceptCookie(true);
                    // 获取登录后的 cookie
                    // TODO: 2021/6/4 此处要写登录页面url
                    String cookieStr = cookieManager.getCookie("");
                    SPUtils.put(activity, Constants.KEY_COOKIE, cookieStr);
                }

            } else if ("delete".equals(method)) {
                SPUtils.remove(activity, key);
            }

            return jsonObject.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
