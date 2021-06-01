package com.wdweblib.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.wdweblib.BaseFragment;
import com.wdweblib.Constants;
import com.wdweblib.R;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-27 13:23
 */
public class SingleFragment extends BaseFragment {

    private static final String TAG = SingleFragment.class.getSimpleName();


    private String mUrl;

    public static SingleFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.FLAG_JUMP_URL, url);

        SingleFragment fragment = new SingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString(Constants.FLAG_JUMP_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wd_fragment_single, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mWDWebView = view.findViewById(R.id.wd_webview);
        mWebLoadHintView = view.findViewById(R.id.view_web_load_hint);
        mTitleBar = view.findViewById(R.id.bar_title);

        mWDWebView.loadUrl_(mUrl, SingleFragment.this);

    }

}
