package com.wdweblib.widget;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wdweblib.BaseFragment;
import com.wdweblib.R;
import com.wdweblib.bean.HeaderBean;
import com.wdweblib.utils.SizeUtils;
import com.wdweblib.utils.StringUtils;
import com.wdweblib.web.WDWebView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 10:04
 */
public class TitleBar extends RelativeLayout {

    private LinearLayout ly_left;
    private LinearLayout ly_right;
    private LinearLayout ly_center;

    private Context mContext;
    private BaseFragment mBaseFragment;
    private WDWebView mWDWebView;

    //导航栏是否是悬浮展示，再js交互方法forward中传递过来的
    private boolean isFloat;


    /*
    目前头部 分为三个部分left、titleView（中间）、right
    left和right包含的视图类型有：（对应的type值）
    back、image、title、collection
    titleView包含的类型有：
    title、search

    所以只需要在bar上建立三个视图模块，
    然后再创建这些对应type类型的视图，
    往里面添加就好了。
    以后增加什么type，
    就创建什么type视图就行了
    */

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View view = inflate(context, R.layout.wd_view_title_bar, this);
        ly_left = view.findViewById(R.id.ly_left);
        ly_right = view.findViewById(R.id.ly_right);
        ly_center = view.findViewById(R.id.ly_center);
    }

    public void setTitleBarStyle(BaseFragment baseFragment,
                                 WDWebView wdWebView,
                                 HeaderBean headerBean) {
        mBaseFragment = baseFragment;
        mWDWebView = wdWebView;

        ly_left.removeAllViews();
        ly_right.removeAllViews();
        ly_center.removeAllViews();


        if (headerBean == null) return;
        //默认为update
        if (!StringUtils.isNotEmpty(headerBean.getType())) {
            headerBean.setType("update");
        }

        switch (headerBean.getType()) {
            case "init":
            case "update":
                initandUpdate(headerBean);
                break;
            case "3th"://标题更新
                break;
        }


    }

    private void initandUpdate(HeaderBean headerBean) {
        //此处设计的如果left为空，默认创建一个返回按钮，即type=back的类型视图
        if (headerBean.getLeft() == null) {
            ly_left.addView(buildLeftBack(new HeaderBean.LeftBean()));
        }
//        back、image、title、collection
        if (headerBean.getLeft() != null) {
            View typeView = null;
            for (HeaderBean.LeftBean leftBean : headerBean.getLeft()) {
                if ("back".equals(leftBean.getType())) {
                    typeView = buildLeftBack(leftBean);
                } else if ("image".equals(leftBean.getType())) {
                    typeView = bulidLeftImage(leftBean);
                } else if ("title".equals(leftBean.getType())) {
                    typeView = buildLeftTitle(leftBean);
                } else if ("collection".equals(leftBean.getType())) {
                    typeView = bulidLeftCollection(leftBean);
                }
                if (typeView != null) {
                    ly_left.addView(typeView);
                }
            }
        }

        if (headerBean.getRight() != null) {
            View typeView = null;
            for (HeaderBean.RightBean rightBean : headerBean.getRight()) {
                if ("image".equals(rightBean.getType())) {
                    typeView = bulidRightImage(rightBean);
                } else if ("title".equals(rightBean.getType())) {
                    typeView = buildRightTitle(rightBean);
                } else if ("collection".equals(rightBean.getType())) {
                    typeView = bulidRightCollection(rightBean);
                }
                if (typeView != null) {
                    ly_right.addView(typeView);
                }
            }
        }

        if (headerBean.getTitleView() != null) {
            if ("title".equals(headerBean.getTitleView().getType())) {
                View view = buildTitleViewTitle(headerBean.getTitleView());
                ly_center.addView(view);
            } else if ("search".equals(headerBean.getTitleView().getType())) {
                //此处修改ly_center的属性，因为xml里写的是直接居中，防止左右布局不一样宽
                //导致title不居中。但是为search的时候，要在左右两个模块之间，所以此处做了属性修改
                LayoutParams lp = (LayoutParams) ly_center.getLayoutParams();
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.ly_left);
                lp.addRule(RelativeLayout.LEFT_OF, R.id.ly_right);
                ly_center.setLayoutParams(lp);
                View view = buildTitleViewSearch(headerBean.getTitleView());

                ly_center.addView(view);
            }
        }
    }

    //=====================================创建left模块===============================

    /**
     * 创建left模块里type=back的视图
     *
     * @param bean
     * @return
     */
    private View buildLeftBack(HeaderBean.LeftBean bean) {
        ImageView backView = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                SizeUtils.dp2px(mContext, 20),
                SizeUtils.dp2px(mContext, 20));
        lp.gravity = Gravity.CENTER;
        lp.leftMargin = SizeUtils.dp2px(mContext, 10);
        backView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        backView.setLayoutParams(lp);
        backView.setImageResource(R.mipmap.icon_back);
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(bean.getBackUrl())) {
                    // TODO: 2021/5/31 返回指定url界面
                }
                mBaseFragment.pop();
            }
        });
        return backView;
    }

    /**
     * 创建left模块里type=image的视图
     *
     * @param bean
     * @return
     */
    private View bulidLeftImage(HeaderBean.LeftBean bean) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                SizeUtils.dp2px(mContext, 20),
                SizeUtils.dp2px(mContext, 20));
        lp.leftMargin = SizeUtils.dp2px(mContext, 10);
        lp.gravity = Gravity.CENTER;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(lp);
        imageView.setImageResource(R.mipmap.icon_back);
        String imgName = bean.getImgName();
        String imgUrl = bean.getImgUrl();

        //imgName不为空时，设置默认对应图标，如果为空并且imgUrl不为空，就加载imgUrl
        if (StringUtils.isNotEmpty(imgName)) {
            switch (imgName) {
                case "voice":
                    imageView.setImageResource(R.mipmap.ic_voice);
                    break;
                case "message":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_mail_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_mail_black);
                    }
                    break;
                case "set":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_setting_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_setting_black);
                    }
                    break;
                case "search":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_search_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_search_grey);
                    }
                    break;
            }
        } else if (StringUtils.isNotEmpty(imgUrl)) {
            Glide.with(imageView)
                    .load(Uri.parse(imgUrl))
                    .into(imageView);
        } else {

        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return imageView;
    }

    /**
     * 创建left模块里type=title的视图
     *
     * @param bean
     * @return
     */
    private View buildLeftTitle(HeaderBean.LeftBean bean) {
        TextView titleTv = new TextView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = SizeUtils.dp2px(mContext, 12);
        lp.gravity = Gravity.CENTER;
        titleTv.setLayoutParams(lp);
        titleTv.setText(bean.getTitle());
        titleTv.setTextSize(16);
        titleTv.setGravity(Gravity.CENTER);
        if (isFloat) {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.color_gray_666));
        }
        titleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return titleTv;
    }


    /**
     * 创建left模块里type=collection的视图
     *
     * @param bean
     * @return
     */
    private View bulidLeftCollection(HeaderBean.LeftBean bean) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutCollect = new LinearLayout(mContext);
        linearLayoutCollect.setLayoutParams(layoutParams);

        ImageView imageCollect = (ImageView) bulidLeftImage(bean);
        TextView textCollect = (TextView) buildLeftTitle(bean);

        linearLayoutCollect.addView(imageCollect);
        linearLayoutCollect.addView(textCollect);

        linearLayoutCollect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return linearLayoutCollect;
    }


    //=====================================创建right模块===============================
    //其实和left模块是一样的，此处重写是为了方便后期功能拓展

    /**
     * 创建right模块里type=back的视图
     *
     * @param bean
     * @return
     */
    private View buildRightBack(HeaderBean.RightBean bean) {
        ImageView backView = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                SizeUtils.dp2px(mContext, 20),
                SizeUtils.dp2px(mContext, 20));
        lp.rightMargin = SizeUtils.dp2px(mContext, 10);
        lp.gravity = Gravity.CENTER;
        backView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        backView.setLayoutParams(lp);
        backView.setImageResource(R.mipmap.icon_back);
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(bean.getBackUrl())) {
                    // TODO: 2021/5/31 返回指定url界面
                }
                mBaseFragment.pop();
            }
        });
        return backView;
    }

    /**
     * 创建right模块里type=image的视图
     *
     * @param bean
     * @return
     */
    private View bulidRightImage(HeaderBean.RightBean bean) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                SizeUtils.dp2px(mContext, 20),
                SizeUtils.dp2px(mContext, 20));
        lp.rightMargin = SizeUtils.dp2px(mContext, 10);
        lp.gravity = Gravity.CENTER;
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(lp);
        String imgName = bean.getImgName();
        String imgUrl = bean.getImgUrl();

        //imgName不为空时，设置默认对应图标，如果为空并且imgUrl不为空，就加载imgUrl
        if (StringUtils.isNotEmpty(imgName)) {
            switch (imgName) {
                case "voice":
                    imageView.setImageResource(R.mipmap.ic_voice);
                    break;
                case "message":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_mail_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_mail_black);
                    }
                    break;
                case "set":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_setting_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_setting_black);
                    }
                    break;
                case "search":
                    if (isFloat) {
                        imageView.setImageResource(R.mipmap.ic_search_white);
                    } else {
                        imageView.setImageResource(R.mipmap.ic_search_grey);
                    }
                    break;
            }
        } else if (StringUtils.isNotEmpty(imgUrl)) {
            Glide.with(imageView)
                    .load(Uri.parse(imgUrl))
                    .into(imageView);
        } else {

        }
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return imageView;
    }

    /**
     * 创建right模块里type=title的视图
     *
     * @param bean
     * @return
     */
    private View buildRightTitle(HeaderBean.RightBean bean) {
        TextView titleTv = new TextView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.rightMargin = SizeUtils.dp2px(mContext, 12);
        lp.gravity = Gravity.CENTER;
        titleTv.setLayoutParams(lp);
        titleTv.setText(bean.getTitle());
        titleTv.setTextSize(16);
        titleTv.setGravity(Gravity.CENTER);
        if (isFloat) {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.color_gray_666));
        }
        titleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return titleTv;
    }


    /**
     * 创建right模块里type=collection的视图
     *
     * @param bean
     * @return
     */
    private View bulidRightCollection(HeaderBean.RightBean bean) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutCollect = new LinearLayout(mContext);
        linearLayoutCollect.setLayoutParams(layoutParams);

        ImageView imageCollect = (ImageView) bulidRightImage(bean);
        TextView textCollect = (TextView) buildRightTitle(bean);

        linearLayoutCollect.addView(imageCollect);
        linearLayoutCollect.addView(textCollect);

        linearLayoutCollect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWDWebView.callJs(bean.getCallBackMethod(), "");
            }
        });
        return linearLayoutCollect;
    }

    //==========================创建titleview模块===============================

    /**
     * 创建titleView模块里type=title的视图
     *
     * @param bean
     * @return
     */
    private View buildTitleViewTitle(HeaderBean.TitleViewBean bean) {
        TextView titleTv = new TextView(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        titleTv.setLayoutParams(lp);
        titleTv.setText(bean.getTitle());
        titleTv.setTextSize(18);
        titleTv.setGravity(Gravity.CENTER);
        if (isFloat) {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            titleTv.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        return titleTv;
    }

    /**
     * 创建titleView模块里type=search的视图
     *
     * @param bean
     * @return
     */
    private View buildTitleViewSearch(HeaderBean.TitleViewBean bean) {
        View view = inflate(mContext, R.layout.wd_view_titleview_search, null);
        EditText et_search = view.findViewById(R.id.et_search);
        ImageView iv_voice = view.findViewById(R.id.iv_voice);

        if (bean.isVoice()) {
            iv_voice.setVisibility(VISIBLE);
        } else {
            iv_voice.setVisibility(GONE);
        }
        /*监听文字改变*/
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (bean.getSearchMethods() != null &&
                        StringUtils.isNotEmpty(bean.getSearchMethods().getEditingchanged())) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("inputValue", s.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mWDWebView.callJs(bean.getSearchMethods().getEditingchanged(),
                            jsonObject.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        /*监听操作事件*/
        et_search.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    && bean.getSearchMethods() != null
                    && StringUtils.isNotEmpty(bean.getSearchMethods().getEditingfinished())) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("inputValue", et_search.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mWDWebView.callJs(bean.getSearchMethods().getEditingfinished(),
                        jsonObject.toString().trim());
            }
            return false;
        });
        /*焦点事件处理*/
        et_search.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (bean.getSearchMethods() != null) {
                    if (hasFocus) {
                        // 此处为获得焦点时的处理
                        if (StringUtils.isNotEmpty(bean.getSearchMethods().getEditingdidbegin())) {
                            mWDWebView.callJs(bean.getSearchMethods().getEditingdidbegin(), "");
                        }
                    } else {
                        // 此处为失去焦点时的处理
                        if (StringUtils.isNotEmpty(bean.getSearchMethods().getEditingdidend())) {
                            mWDWebView.callJs(bean.getSearchMethods().getEditingdidend(), "");
                        }
                    }
                }
            }
        });
        return view;
    }
}
