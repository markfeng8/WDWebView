package com.wdweblib.bean.nativeparam;

import com.wdweblib.bean.NativeBean;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-04 17:41
 */
public class DownloadParam extends NativeBean<DownloadParam> {

    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
