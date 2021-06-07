package com.wdweblib.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.wdweblib.BaseFragment;
import com.wdweblib.Constants;
import com.wdweblib.R;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-02 14:52
 */
public class WDLoginFragment extends BaseFragment {

    private static final String TAG = WDLoginFragment.class.getSimpleName();


    private String mUrl;

    public static WDLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.FLAG_JUMP_URL, url);

        WDLoginFragment fragment = new WDLoginFragment();
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
        View view = inflater.inflate(R.layout.wd_fragment_login, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mWDWebView = view.findViewById(R.id.wd_webview);
        mWebLoadHintView = view.findViewById(R.id.view_web_load_hint);

        mWDWebView.loadUrl_(mUrl, WDLoginFragment.this);

    }
}
