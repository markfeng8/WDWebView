package com.wdweblib.bean.nativeparam;

import com.wdweblib.bean.NativeBean;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-04 17:14
 */
public class ShareParam  extends NativeBean<ShareParam> {


    private String title;
    private String link;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
