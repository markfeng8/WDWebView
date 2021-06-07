package com.wdweblib.bean.nativeparam;

import com.wdweblib.bean.NativeBean;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-04 17:10
 */
public class VideoParam extends NativeBean<VideoParam> {


    private String maxDuration;
    private String minDuration;

    public String getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(String maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(String minDuration) {
        this.minDuration = minDuration;
    }
}
