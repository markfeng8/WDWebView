package com.wdweblib.bean.nativeparam;

import android.content.Intent;
import android.net.Uri;

import com.wdweblib.bean.NativeBean;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-03 11:29
 */
public class PhoneParam extends NativeBean<PhoneParam> {

    private static final String MODE_CALL = "call";

    private String mode;
    private String phoneNumber;
    private String content;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Intent newIntent() {
        if (MODE_CALL.equals(mode)) {
            return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        } else {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", content);
            return intent;
        }
    }
}
