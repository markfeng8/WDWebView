package com.wdweblib.bean.nativeparam;

import android.content.Intent;
import android.net.Uri;

import com.wdweblib.bean.NativeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-03 16:25
 */
public class PhotoLibraryParam extends NativeBean<PhotoLibraryParam> {

    private int maxCount;
    private int maxBytes;
    private String tag;

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxBytes() {
        return maxBytes;
    }

    public void setMaxBytes(int maxBytes) {
        this.maxBytes = maxBytes;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
