package com.wdweblib.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wdweblib.R;
import com.wdweblib.utils.NetworkUtil;


public class WebLoadHintView extends RelativeLayout {

    private RelativeLayout mRlLoaddingContainer;
    private ImageView mIvLoaddingError;
    private TextView mTvLoaddingTips;
    private Button mBtnRetry;

    public WebLoadHintView(Context context) {
        super(context);
        init(context);
    }

    public WebLoadHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebLoadHintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.wd_view_web_load_hint, this);
        mRlLoaddingContainer = view.findViewById(R.id.rl_loading_container);
        mIvLoaddingError = view.findViewById(R.id.iv_loading_error);
        mTvLoaddingTips = view.findViewById(R.id.tv_loadding_tips);
        mBtnRetry = view.findViewById(R.id.btn_retry);
    }

    /**
     * 显示加载
     *
     * @param _mActivity
     */
    public void loading(Activity _mActivity) {
        mRlLoaddingContainer.setVisibility(View.VISIBLE);
        mBtnRetry.setVisibility(View.GONE);
        if (NetworkUtil.isNetworkAvailable(_mActivity) && !_mActivity.isDestroyed()) {
            Glide.with(_mActivity).load(R.drawable.ic_loading).into(mIvLoaddingError);
            mTvLoaddingTips.setText("页面加载中，请稍后");
        } else {
            mIvLoaddingError.setImageResource(R.mipmap.ic_network_error);
            mTvLoaddingTips.setText("当前网络不可用，请检查你的网络设置");
        }
    }

    /**
     * 加载结束
     */
    public void hideLoad() {
        mRlLoaddingContainer.setVisibility(View.GONE);
        this.setVisibility(View.GONE);
        mIvLoaddingError.clearAnimation();
    }

    /**
     * 加载出错，重新加载
     *
     * @param listener
     */
    public void showError(OnClickListener listener) {
        mRlLoaddingContainer.setVisibility(View.VISIBLE);
        mIvLoaddingError.setImageResource(R.mipmap.ic_load_error);
        mTvLoaddingTips.setText("网页异常，请点击刷新");
        mBtnRetry.setVisibility(View.VISIBLE);
        mBtnRetry.setOnClickListener(v -> {
            listener.onClick(v);
        });
    }
}
