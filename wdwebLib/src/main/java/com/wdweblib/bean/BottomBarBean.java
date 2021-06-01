package com.wdweblib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 16:56
 */
public class BottomBarBean extends NativeBean<BottomBarBean.ParamsBean>{

    /**
     * type : tabConfig
     * params : {"selectedTextColor":"#51da8a","unselectedTextColor":"#636363","tabList":[{"tabName":"首页","selectedUrl":"www.123.jpg","unselectedUrl":"www.456.jpg","tabPage":[{"title":"wode","url":"http"}],"hasNavigation":"true","isDialog":"false"}]}
     */

    public static class ParamsBean {
        /**
         * selectedTextColor : #51da8a
         * unselectedTextColor : #636363
         * tabList : [{"tabName":"首页","selectedUrl":"www.123.jpg","unselectedUrl":"www.456.jpg","tabPage":[{"title":"wode","url":"http"}],"hasNavigation":"true","isDialog":"false"}]
         */

        private String selectedTextColor;
        private String unselectedTextColor;
        private List<TabListBean> tabList;

        public String getSelectedTextColor() {
            return selectedTextColor;
        }

        public void setSelectedTextColor(String selectedTextColor) {
            this.selectedTextColor = selectedTextColor;
        }

        public String getUnselectedTextColor() {
            return unselectedTextColor;
        }

        public void setUnselectedTextColor(String unselectedTextColor) {
            this.unselectedTextColor = unselectedTextColor;
        }

        public List<TabListBean> getTabList() {
            return tabList;
        }

        public void setTabList(List<TabListBean> tabList) {
            this.tabList = tabList;
        }


    }
}
