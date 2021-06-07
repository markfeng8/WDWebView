package com.wdweblib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 11:22
 */
public class TabListBean implements Parcelable {


    /**
     * tabName : 首页
     * selectedUrl : www.123.jpg
     * unselectedUrl : www.456.jpg
     * tabPage : [{"title":"wode","url":"http"}]
     * hasNavigation : true
     * isDialog : false
     */

    private String tabName;
    private Object selectedUrl;
    private Object unselectedUrl;
    private boolean hasNavigation;
    private boolean isDialog;
    private List<TabPageBean> tabPage;

    public TabListBean() {

    }

    protected TabListBean(Parcel in) {
        tabName = in.readString();
        hasNavigation = in.readByte() != 0;
        isDialog = in.readByte() != 0;
        tabPage = in.createTypedArrayList(TabPageBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tabName);
        dest.writeByte((byte) (hasNavigation ? 1 : 0));
        dest.writeByte((byte) (isDialog ? 1 : 0));
        dest.writeTypedList(tabPage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TabListBean> CREATOR = new Creator<TabListBean>() {
        @Override
        public TabListBean createFromParcel(Parcel in) {
            return new TabListBean(in);
        }

        @Override
        public TabListBean[] newArray(int size) {
            return new TabListBean[size];
        }
    };

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public boolean isHasNavigation() {
        return hasNavigation;
    }

    public Object getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(Object selectedUrl) {
        this.selectedUrl = selectedUrl;
    }

    public Object getUnselectedUrl() {
        return unselectedUrl;
    }

    public void setUnselectedUrl(Object unselectedUrl) {
        this.unselectedUrl = unselectedUrl;
    }

    public void setHasNavigation(boolean hasNavigation) {
        this.hasNavigation = hasNavigation;
    }

    public boolean isDialog() {
        return isDialog;
    }

    public void setDialog(boolean dialog) {
        isDialog = dialog;
    }

    public List<TabPageBean> getTabPage() {
        return tabPage;
    }

    public void setTabPage(List<TabPageBean> tabPage) {
        this.tabPage = tabPage;
    }

    /**
     * 获取MulItemList
     * @return
     */
    public List<MulItemFragmentBean> getMulItemList() {
        List<MulItemFragmentBean> mulItemList = new ArrayList<>();
        MulItemFragmentBean item;
        for (TabPageBean tabPageBean : tabPage) {
            item = new MulItemFragmentBean(tabPageBean.getTitle(), tabPageBean.getUrl());
            mulItemList.add(item);
        }
        return mulItemList;
    }


    public static class TabPageBean implements Parcelable {
        /**
         * title : wode
         * url : http
         */

        private String title;
        private String url;

        public TabPageBean(String title, String url) {
            this.title = title;
            this.url = url;
        }

        protected TabPageBean(Parcel in) {
            title = in.readString();
            url = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(url);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<TabPageBean> CREATOR = new Creator<TabPageBean>() {
            @Override
            public TabPageBean createFromParcel(Parcel in) {
                return new TabPageBean(in);
            }

            @Override
            public TabPageBean[] newArray(int size) {
                return new TabPageBean[size];
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
    }
}