package com.wdweblib.interactive;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.fragmentation.fragmentation.SupportActivity;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.Permission;
import com.wdweblib.BaseFragment;
import com.wdweblib.bean.Maps;
import com.wdweblib.bean.NativeBean;
import com.wdweblib.bean.nativeparam.BlueToothDeviceParam;
import com.wdweblib.bean.nativeparam.DownloadParam;
import com.wdweblib.bean.nativeparam.LocationParam;
import com.wdweblib.bean.nativeparam.NativeStorageParam;
import com.wdweblib.bean.nativeparam.PhoneParam;
import com.wdweblib.bean.nativeparam.PhotoLibraryParam;
import com.wdweblib.bean.nativeparam.QrCodeScanParam;
import com.wdweblib.bean.nativeparam.RotateParams;
import com.wdweblib.bean.nativeparam.SearchContentParam;
import com.wdweblib.bean.nativeparam.ShareParam;
import com.wdweblib.bean.nativeparam.VideoParam;
import com.wdweblib.ui.PhotoLibraryActivity;
import com.wdweblib.ui.mediarecorder.MediaRecorderActivity;
import com.wdweblib.utils.BDAsrManger;
import com.wdweblib.utils.CacheUtil;
import com.wdweblib.utils.CameraHelper;
import com.wdweblib.utils.DownloadTask;
import com.wdweblib.utils.GDGpsManger;
import com.wdweblib.utils.ImageOperation;
import com.wdweblib.utils.PhotoUtils;
import com.wdweblib.utils.RxPermission;
import com.wdweblib.utils.SPUtils;
import com.wdweblib.utils.StringUtils;
import com.wdweblib.utils.ToastUtils;
import com.wdweblib.utils.ToolUtils;
import com.wdweblib.utils.UriUtils;
import com.wdweblib.utils.avoidonresult.ActivityResultInfo;
import com.wdweblib.utils.avoidonresult.AvoidOnResult;
import com.wdweblib.web.WDWebView;
import com.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * code by markfeng
 * <p>
 * create on 2021-05-31 09:30
 */
public class JSNativePermission {

    private SupportActivity _mActivity;
    private BaseFragment mBaseFragment;
    private WDWebView mWDWebView;
    private static Gson mGson;

    private JSNativePermission() {
    }

    private static class JSNativePermissionInstance {
        private static final JSNativePermission INSTANCE = new JSNativePermission();
    }

    public static JSNativePermission getInstance() {
        return JSNativePermission.JSNativePermissionInstance.INSTANCE;
    }

    public void doNative(SupportActivity _mActivity,
                         BaseFragment fragment,
                         WDWebView wdWebView,
                         String nativeJson) {

        this._mActivity = _mActivity;
        this.mBaseFragment = fragment;
        this.mWDWebView = wdWebView;

        if (mGson == null) {
            mGson = new Gson();
        }
        NativeBean bean = mGson.fromJson(nativeJson, NativeBean.class);

        String type = bean.getType();

        if ("qrCodeScan".equals(type)) {
            qrCodeScan(nativeJson);
        } else if ("phone".equals(type)) {
            phone(nativeJson);
        } else if ("location".equals(type)) {
            location(nativeJson);
        } else if ("photoLibrary".equals(type)) {
            photoLibrary(nativeJson);
        } else if ("camera".equals(type)) {
            camera(nativeJson);
        } else if ("version".equals(type)) {
            version(nativeJson);
        } else if ("nativeStorage".equals(type)) {
            nativeStorage(nativeJson);
        } else if ("searchContent".equals(type)) {
            searchContent(nativeJson);
        } else if ("rotate".equals(type)) {
            rotate(nativeJson);
        } else if ("bluetoothDevice".equals(type)) {
            bluetoothDevice(nativeJson);
        } else if ("video".equals(type)) {
            video(nativeJson);
        } else if ("share".equals(type)) {
            share(nativeJson);
        } else if ("clearCache".equals(type)) {
            clearCache(nativeJson);
        } else if ("clearCache".equals(type)) {
            cacheSize(nativeJson);
        } else if ("download".equals(type)) {
            download(nativeJson);
        } else if ("voice".equals(type)) {
            voice(nativeJson);
        }
    }

    private void voice(String nativeJson) {
        RxPermission.getInstance().requestPermissions(_mActivity,
                "您已禁止了麦克风权限，是否前往设置界面打卡？", new RxPermission.PermissionsImp() {
                    @Override
                    public void accept(Permission permission) {
                        BDAsrManger.getInstance()
                                .init(_mActivity)
                                .start();
                    }
                },
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void download(String nativeJson) {
        DownloadParam bean = mGson.fromJson(nativeJson, DownloadParam.class);
        String downloadUri = bean.getParams().getDownloadUrl();
        if (StringUtils.isNotEmpty(downloadUri)) {
            String fileName = UriUtils.getDownloadFileName(downloadUri);
            if (TextUtils.isEmpty(fileName)) {
                fileName = UriUtils.getDownloadFileName(downloadUri);
                if (TextUtils.isEmpty(fileName)) {
                    fileName = "移动综管导出.xls";
                }
            }
            String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + File.separator + fileName;
            new DownloadTask(_mActivity).execute(downloadUri, destPath);
        }
    }

    private void cacheSize(String nativeJson) {
        NativeBean bean = mGson.fromJson(nativeJson, NativeBean.class);
        try {
            String totalCacheSize = CacheUtil.getTotalCacheSize(_mActivity);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cacheSize", TextUtils.isEmpty(totalCacheSize) ? "0M" : totalCacheSize);
            mWDWebView.callJs(bean.getCallBackMethod(), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCache(String nativeJson) {
        NativeBean bean = mGson.fromJson(nativeJson, NativeBean.class);
        CacheUtil.clearAllCache(_mActivity);
        mWDWebView.callJs(bean.getCallBackMethod(), "");
    }

    private void share(String nativeJson) {
        ShareParam bean = mGson.fromJson(nativeJson, ShareParam.class);
        if (bean == null
                || TextUtils.isEmpty(bean.getParams().getTitle())
                || TextUtils.isEmpty(bean.getParams().getLink())) {
            ToastUtils.showToast("参数为空");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain"); //分享内容的类型
        intent.putExtra(Intent.EXTRA_SUBJECT, "" + bean.getParams().getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, "" + bean.getParams().getLink());
        _mActivity.startActivity(Intent.createChooser(intent, "" + bean.getParams().getTitle()));
    }

    private void video(String nativeJson) {
        VideoParam bean = mGson.fromJson(nativeJson, VideoParam.class);
        AvoidOnResult rxOnResult = new AvoidOnResult(_mActivity);
        Intent intent = MediaRecorderActivity.start(_mActivity,
                bean.getParams().getMinDuration(),
                bean.getParams().getMaxDuration());
        rxOnResult.startForResult(intent).
                filter(activityResultInfo -> {
                    return activityResultInfo.getResultCode() == _mActivity.RESULT_OK;
                }).
                map(new Function<ActivityResultInfo, String>() {
                    @Override
                    public String apply(ActivityResultInfo activityResultInfo) throws Exception {
                        String videoPath = activityResultInfo.getData().getStringExtra(MediaRecorderActivity.VIDEO_PATH);
                        String base64 = CameraHelper.convertFileToString(videoPath);
                        // 2798617 3586737 3794156 4203832

                        if (TextUtils.isEmpty(base64)) {
                            ToastUtils.showToast("转码为空");
                        } else {
                            HashMap<String, String> map = Maps.newHashMapWithExpectedSize(1);
                            map.put("video", base64);
                            String json = new Gson().toJson(map);
                            return json;
                        }
                        return "";
                    }
                }).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(
                        result -> {
//                            LogUtil.i(TAG, "video base64 string :" + result);
                            // 用 JsBridge 调用时，当 str 太大会无反应，因此用原生调用方式！！！
                            //TODO
//                            webView.loadUrl("javascript:finishShoot(" + str + ")");
                            mWDWebView.callJs(bean.getCallBackMethod(), result);

                        },
                        throwable -> {
                            ToastUtils.showToast(throwable.getMessage().toString());
                        });
    }

    private void bluetoothDevice(String nativeJson) {
        BlueToothDeviceParam bean = mGson.fromJson(nativeJson, BlueToothDeviceParam.class);
    }

    private void rotate(String nativeJson) {
        RotateParams bean = mGson.fromJson(nativeJson, RotateParams.class);

    }

    private void searchContent(String nativeJson) {
        SearchContentParam bean = mGson.fromJson(nativeJson, SearchContentParam.class);
    }

    private void nativeStorage(String nativeJson) {
        SPUtils.put(_mActivity,"wondersAuthToken","123456");
        NativeStorageParam bean = mGson.fromJson(nativeJson, NativeStorageParam.class);
        RxPermission.getInstance().requestPermissions(_mActivity,
                "您已禁止了SD存储权限，是否前往设置打开？",
                new RxPermission.PermissionsImp() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            mWDWebView.callJs(bean.getCallBackMethod(),
                                    bean.getParams().backJsonStr(_mActivity));
                        }
                    }
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void version(String nativeJson) {
        try {
            NativeBean bean = mGson.fromJson(nativeJson, NativeBean.class);
            String currentVersion = ToolUtils.getAppVersionName(_mActivity);
            String lastVersion = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currentVersion", currentVersion);
            jsonObject.put("lastVersion", lastVersion);
            mWDWebView.callJs(bean.getCallBackMethod(), jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void camera(String nativeJson) {
        NativeBean bean = mGson.fromJson(nativeJson, NativeBean.class);
        RxPermission.getInstance().requestPermissions(_mActivity,
                "您已禁止了拍照和访问SD卡权限，是否前往设置打开？", new RxPermission.PermissionsImp() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            new AvoidOnResult(_mActivity).startForResult(PhotoUtils.takePicture(_mActivity), new AvoidOnResult.Callback() {
                                @Override
                                public void onActivityResult(int resultCode, Intent data) {
                                    if (resultCode == _mActivity.RESULT_OK) {
                                        try {
                                            Bitmap bitmap = ImageOperation.getBitmapFormUri(_mActivity,
                                                    Uri.fromFile(PhotoUtils.fileUri),
                                                    0);
                                            String baseStr = ImageOperation.bitmapToBase64(bitmap);
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("avatar", "data:image/png;base64," + baseStr);
                                            Log.i("avatar", "data:image/png;base64," + baseStr);
                                            mWDWebView.callJs(bean.getCallBackMethod(), jsonObject.toString());
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                    }
                },
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    private void photoLibrary(String nativeJson) {
        PhotoLibraryParam bean = mGson.fromJson(nativeJson, PhotoLibraryParam.class);
        PhotoLibraryActivity.actionStart(_mActivity,
                bean.getParams().getMaxCount(),
                bean.getParams().getMaxBytes(),
                new PhotoLibraryActivity.OnSelectListener() {
                    @Override
                    public void onResult(String json) {
                        Log.i("fct", json);
                        mWDWebView.callJs(bean.getCallBackMethod(), json);
                    }
                });
    }

    private void phone(String nativeJson) {
        PhoneParam bean = mGson.fromJson(nativeJson, PhoneParam.class);
        _mActivity.startActivity(bean.getParams().newIntent());
    }

    private void location(String nativeJson) {
        LocationParam bean = mGson.fromJson(nativeJson, LocationParam.class);
        GDGpsManger.getInstance().init(_mActivity);
        GDGpsManger.getInstance().getLocation(_mActivity)
                .subscribe(aMapLocation -> {
                    mWDWebView.callJs(bean.getCallBackMethod(),
                            bean.getParams().backJsonStr(
                                    aMapLocation.getLongitude() + "",
                                    aMapLocation.getLatitude() + ""));
                }, throwable -> {
                    ToastUtils.showToast(throwable.toString());
                });
    }

    private void qrCodeScan(String nativeJson) {
        QrCodeScanParam bean = mGson.fromJson(nativeJson, QrCodeScanParam.class);
        RxPermission.getInstance().requestPermissions(_mActivity,
                "您已禁止了拍照和存储权限，是否前往设置界面打开权限？",
                new RxPermission.PermissionsImp() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            new AvoidOnResult(_mActivity).startForResult(CaptureActivity.class,
                                    new AvoidOnResult.Callback() {
                                        @Override
                                        public void onActivityResult(int resultCode, Intent data) {
                                            // get your data from intent
                                            if (resultCode == CaptureActivity.RESULT_CODE_QR_SCAN) {
                                                Bundle bundle = data.getExtras();
                                                String scanResult = bundle.getString(
                                                        CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN);
                                                Log.i("scanResult", scanResult);
                                                mWDWebView.callJs(bean.getCallBackMethod(),
                                                        bean.getParams().backJsonStr(scanResult));
                                            }
                                        }
                                    });
                        }
                    }
                },
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
