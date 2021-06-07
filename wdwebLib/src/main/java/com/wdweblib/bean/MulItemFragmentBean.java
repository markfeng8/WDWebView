package com.wdweblib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 17:41
 */
public class MulItemFragmentBean implements Parcelable {
    private String title;
    private String url;

    public MulItemFragmentBean(String title, String url) {
        this.title = title;
        this.url = url;
    }

    protected MulItemFragmentBean(Parcel in) {
        title = in.readString();
        url = in.readString();
    }

    public static final Creator<MulItemFragmentBean> CREATOR = new Creator<MulItemFragmentBean>() {
        @Override
        public MulItemFragmentBean createFromParcel(Parcel in) {
            return new MulItemFragmentBean(in);
        }

        @Override
        public MulItemFragmentBean[] newArray(int size) {
            return new MulItemFragmentBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
    }
}
