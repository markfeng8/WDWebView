package com.wdweblib.utils.avoidonresult;

import android.content.Intent;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-02 17:08
 */


public class ActivityResultInfo {
    private int resultCode;
    private Intent data;

    public ActivityResultInfo(int resultCode, Intent data) {
        this.resultCode = resultCode;
        this.data = data;
    }


    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Intent getData() {

        return data;
    }

    public void setData(Intent data) {
        this.data = data;
    }
}