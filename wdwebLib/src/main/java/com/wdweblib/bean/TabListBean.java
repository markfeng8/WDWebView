package com.wdweblib.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-01 11:22
 */
public  class TabListBean implements Parcelable {


    /**
     * tabName : 首页
     * selectedUrl : www.123.jpg
     * unselectedUrl : www.456.jpg
     * tabPage : [{"title":"wode","url":"http"}]
     * hasNavigation : true
     * isDialog : false
     */

    private String tabName;
    private String selectedUrl;
    private String unselectedUrl;
    private boolean hasNavigation;
    private boolean isDialog;
    private List<TabPageBean> tabPage;

    public TabListBean() {

    }


    protected TabListBean(Parcel in) {
        tabName = in.readString();
        selectedUrl = in.readString();
        unselectedUrl = in.readString();
        hasNavigation = in.readByte() != 0;
        isDialog = in.readByte() != 0;
        tabPage = in.createTypedArrayList(TabPageBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tabName);
        dest.writeString(selectedUrl);
        dest.writeString(unselectedUrl);
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

    public String getSelectedUrl() {
        return selectedUrl;
    }

    public void setSelectedUrl(String selectedUrl) {
        this.selectedUrl = selectedUrl;
    }

    public String getUnselectedUrl() {
        return unselectedUrl;
    }

    public void setUnselectedUrl(String unselectedUrl) {
        this.unselectedUrl = unselectedUrl;
    }

    public boolean isHasNavigation() {
        return hasNavigation;
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



    public static class TabPageBean implements Parcelable{
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