package com.wdweblib.widget.bottombar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wdweblib.R;
import com.wdweblib.bean.TabListBean;


/**
 * Created by YoKeyword on 16/6/3.
 */
public class BottomBarTab extends FrameLayout {
    private ImageView mIcon;
    private TextView mTvTitle;
    private Context mContext;
    private int mTabPosition = -1;


    private String mSelectedTextColor;
    private String mUnSelectedTextColor;
    private TabListBean mTabBean;

    public BottomBarTab(Context context,
                        TabListBean bean,
                        String selectedTextColor,
                        String unselectedTextColor) {
        this(context, null, bean, selectedTextColor, unselectedTextColor);
    }

    public BottomBarTab(Context context,
                        AttributeSet attrs,
                        TabListBean bean,
                        String selectedTextColor,
                        String unselectedTextColor) {
        this(context, attrs, 0, bean, selectedTextColor, unselectedTextColor);
    }

    public BottomBarTab(Context context,
                        AttributeSet attrs,
                        int defStyleAttr,
                        TabListBean bean,
                        String selectedTextColor,
                        String unselectedTextColor) {
        super(context, attrs, defStyleAttr);
        init(context, bean, selectedTextColor, unselectedTextColor);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, TabListBean bean,
                      String selectedTextColor,
                      String unselectedTextColor) {

        mTabBean = bean;
        mSelectedTextColor = selectedTextColor;
        mUnSelectedTextColor = unselectedTextColor;

        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(
                new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();

        LinearLayout lLContainer = new LinearLayout(context);
        lLContainer.setOrientation(LinearLayout.VERTICAL);
        lLContainer.setGravity(Gravity.CENTER);
        LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContainer.gravity = Gravity.CENTER;
        lLContainer.setLayoutParams(paramsContainer);

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

        loadImage(mIcon, bean.getUnselectedUrl());

        mIcon.setLayoutParams(params);
//        mIcon.setColorFilter(ContextCompat.getColor(context, Color.parseColor(unselectedTextColor)));
        lLContainer.addView(mIcon);

        mTvTitle = new TextView(context);
        mTvTitle.setText(bean.getTabName());
        LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTv.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mTvTitle.setTextSize(12);
        mTvTitle.setTextColor(Color.parseColor(unselectedTextColor));
        mTvTitle.setLayoutParams(paramsTv);
        lLContainer.addView(mTvTitle);

        addView(lLContainer);

    }

    @SuppressLint("ResourceType")
    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            loadImage(mIcon, mTabBean.getSelectedUrl());
            mTvTitle.setTextColor(Color.parseColor(mSelectedTextColor));
        } else {
            loadImage(mIcon, mTabBean.getUnselectedUrl());
            mTvTitle.setTextColor(Color.parseColor(mUnSelectedTextColor));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }


    private void loadImage(ImageView mIcon, Object image) {
        if (image instanceof String) {
            Glide.with(mContext).load((String) image)
                    .placeholder(R.mipmap.img_load_placeholder)
                    .error(R.mipmap.img_load_error)
                    .into(mIcon);
        } else if (image instanceof Integer) {
            Glide.with(mContext).load((int) image)
                    .placeholder(R.mipmap.img_load_placeholder)
                    .error(R.mipmap.img_load_error)
                    .into(mIcon);
        }

    }

    private int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
