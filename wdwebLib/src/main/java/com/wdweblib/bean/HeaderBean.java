package com.wdweblib.bean;

import java.util.List;

public class HeaderBean {


    /**
     * type : init
     * left : [{"type":"back","backUrl":"www.baidu.com/home"},{"type":"image","imgName":"message","imgUrl":"www.123.jpg","mark":"2"},{"type":"title","title":"返回","callBackMethod":"didClickFinishBtn"},{"type":"collection","collection":{"title":"位置","imgUrl":"www.123.jpg","imgPosition":"left"}}]
     * right : [{"type":"image","imgName":"share","imgUrl":"www.123.jpg"},{"type":"image","imgUrl":"www.123.jpg","callBackMethod":"attent"}]
     * titleView : {"type":"search","placeHolder":"请输入您要搜索的医院","voice":"true","searchMethods":{"editingdidbegin":"didbegin","editingdidend":"didend","editingchanged":"changed","editingfinished":"finished"}}
     */

    private String type;
    private TitleViewBean titleView;
    private List<LeftBean> left;
    private List<RightBean> right;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TitleViewBean getTitleView() {
        return titleView;
    }

    public void setTitleView(TitleViewBean titleView) {
        this.titleView = titleView;
    }

    public List<LeftBean> getLeft() {
        return left;
    }

    public void setLeft(List<LeftBean> left) {
        this.left = left;
    }

    public List<RightBean> getRight() {
        return right;
    }

    public void setRight(List<RightBean> right) {
        this.right = right;
    }

    public static class TitleViewBean {
        /**
         * type : search
         * placeHolder : 请输入您要搜索的医院
         * voice : true
         * searchMethods : {"editingdidbegin":"didbegin","editingdidend":"didend","editingchanged":"changed","editingfinished":"finished"}
         */

        private String type;
        private String title;
        private String placeHolder;
        private boolean voice;
        private SearchMethodsBean searchMethods;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPlaceHolder() {
            return placeHolder;
        }

        public void setPlaceHolder(String placeHolder) {
            this.placeHolder = placeHolder;
        }

        public boolean isVoice() {
            return voice;
        }

        public void setVoice(boolean voice) {
            this.voice = voice;
        }

        public SearchMethodsBean getSearchMethods() {
            return searchMethods;
        }

        public void setSearchMethods(SearchMethodsBean searchMethods) {
            this.searchMethods = searchMethods;
        }

        public static class SearchMethodsBean {
            /**
             * editingdidbegin : didbegin
             * editingdidend : didend
             * editingchanged : changed
             * editingfinished : finished
             */

            private String editingdidbegin;
            private String editingdidend;
            private String editingchanged;
            private String editingfinished;

            public String getEditingdidbegin() {
                return editingdidbegin;
            }

            public void setEditingdidbegin(String editingdidbegin) {
                this.editingdidbegin = editingdidbegin;
            }

            public String getEditingdidend() {
                return editingdidend;
            }

            public void setEditingdidend(String editingdidend) {
                this.editingdidend = editingdidend;
            }

            public String getEditingchanged() {
                return editingchanged;
            }

            public void setEditingchanged(String editingchanged) {
                this.editingchanged = editingchanged;
            }

            public String getEditingfinished() {
                return editingfinished;
            }

            public void setEditingfinished(String editingfinished) {
                this.editingfinished = editingfinished;
            }
        }
    }

    public static class LeftBean {
        /**
         * type : back
         * backUrl : www.baidu.com/home
         * imgName : message
         * imgUrl : www.123.jpg
         * mark : 2
         * title : 返回
         * callBackMethod : didClickFinishBtn
         * collection : {"title":"位置","imgUrl":"www.123.jpg","imgPosition":"left"}
         */

        private String type;
        private String backUrl;
        private String imgName;
        private String imgUrl;
        private String mark;
        private String title;
        private String callBackMethod;
        private CollectionBean collection;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBackUrl() {
            return backUrl;
        }

        public void setBackUrl(String backUrl) {
            this.backUrl = backUrl;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCallBackMethod() {
            return callBackMethod;
        }

        public void setCallBackMethod(String callBackMethod) {
            this.callBackMethod = callBackMethod;
        }

        public CollectionBean getCollection() {
            return collection;
        }

        public void setCollection(CollectionBean collection) {
            this.collection = collection;
        }

        public static class CollectionBean {
            /**
             * title : 位置
             * imgUrl : www.123.jpg
             * imgPosition : left
             */

            private String title;
            private String imgUrl;
            //imgPosition	图标在名称的相对位置	left/top/right/bottom 默认图标在名称的左侧
            private String imgPosition;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getImgPosition() {
                return imgPosition;
            }

            public void setImgPosition(String imgPosition) {
                this.imgPosition = imgPosition;
            }
        }
    }

    public static class RightBean {
        /**
         * type : back
         * backUrl : www.baidu.com/home
         * imgName : message
         * imgUrl : www.123.jpg
         * mark : 2
         * title : 返回
         * callBackMethod : didClickFinishBtn
         * collection : {"title":"位置","imgUrl":"www.123.jpg","imgPosition":"left"}
         */

        private String type;
        private String backUrl;
        private String imgName;
        private String imgUrl;
        private String mark;
        private String title;
        private String callBackMethod;
        private LeftBean.CollectionBean collection;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBackUrl() {
            return backUrl;
        }

        public void setBackUrl(String backUrl) {
            this.backUrl = backUrl;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCallBackMethod() {
            return callBackMethod;
        }

        public void setCallBackMethod(String callBackMethod) {
            this.callBackMethod = callBackMethod;
        }

        public LeftBean.CollectionBean getCollection() {
            return collection;
        }

        public void setCollection(LeftBean.CollectionBean collection) {
            this.collection = collection;
        }

        public static class CollectionBean {
            /**
             * title : 位置
             * imgUrl : www.123.jpg
             * imgPosition : left
             */

            private String title;
            private String imgUrl;
            //imgPosition	图标在名称的相对位置	left/top/right/bottom 默认图标在名称的左侧
            private String imgPosition;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getImgPosition() {
                return imgPosition;
            }

            public void setImgPosition(String imgPosition) {
                this.imgPosition = imgPosition;
            }
        }
    }
}
