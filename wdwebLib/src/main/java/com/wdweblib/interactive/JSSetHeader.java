package com.wdweblib.interactive;

import com.google.gson.Gson;
import com.wdweblib.BaseFragment;
import com.wdweblib.bean.HeaderBean;
import com.wdweblib.web.WDWebView;
import com.wdweblib.widget.TitleBar;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 09:29
 */
public class JSSetHeader {

    private static Gson mGson;
    private static HeaderBean mBean;

    private JSSetHeader() {
    }

    private static class JSSetHeaderInstance {
        private static final JSSetHeader INSTANCE = new JSSetHeader();
    }

    public static JSSetHeader getInstance() {
        return JSSetHeader.JSSetHeaderInstance.INSTANCE;
    }

    public void setHeader(BaseFragment fragment,
                          WDWebView wdWebView,
                          TitleBar titleBar,
                          String headerJson) {
        try {
            if (mGson == null) {
                mGson = new Gson();
            }
            mBean = mGson.fromJson(headerJson, HeaderBean.class);
            titleBar.setTitleBarStyle(fragment, wdWebView, mBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
