package com.wdweblib.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wdweblib.BaseFragment;
import com.wdweblib.Constants;
import com.wdweblib.R;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-28 16:57
 */
public class MulItemFragment extends BaseFragment {

    private String mUrl;

    public static MulItemFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.FLAG_JUMP_URL, url);

        MulItemFragment fragment = new MulItemFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wd_fragment_mul_item, container, false);
        initView(view, savedInstanceState);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mWDWebView = view.findViewById(R.id.wd_webview);
        mWebLoadHintView = view.findViewById(R.id.view_web_load_hint);

        mWDWebView.loadUrl_(mUrl, MulItemFragment.this);

    }
}
