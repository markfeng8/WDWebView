package com.wdweblib.utils;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.Permission;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


public class GDGpsManger {

    private static final String TAG = GDGpsManger.class.getSimpleName();

    private static GDGpsManger instance = null;

    //声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public static AMapLocationClientOption mLocationOption = null;

    public double mLongitude;//经度
    public double mLatitude;// 纬度
    public String mLocType;// 定位类型

    public static GDGpsManger getInstance() {
        if (instance == null) {
            synchronized (GDGpsManger.class) {
                if (instance == null) {
                    instance = new GDGpsManger();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(context.getApplicationContext());
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(1000);
            //设置是否允许模拟位置,默认为true，允许模拟位置
            mLocationOption.setMockEnable(true);
            //关闭缓存机制
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }

    }


    public Observable<AMapLocation> getLocation(FragmentActivity _mActivity) {
        return Observable.create(new ObservableOnSubscribe<AMapLocation>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<AMapLocation> emitter) throws Exception {
                RxPermission.getInstance().requestPermissions(_mActivity,
                        "您已禁止了定位相关权限，是否前往设置界面打开权限？", new RxPermission.PermissionsImp() {
                            @Override
                            public void accept(Permission permission) {
                                if(permission.granted){
                                    //设置定位回调监听

                                    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
                                        @Override
                                        public void onLocationChanged(AMapLocation aMapLocation) {
                                            if (aMapLocation != null) {
                                                if (aMapLocation.getErrorCode() == 0) {
                                                    //可在其中解析amapLocation获取相应内容。
                                                    mLocType = aMapLocation.getLocationType() + "";//获取当前定位结果来源，如网络定位结果，详见定位类型表
                                                    mLatitude = aMapLocation.getLatitude();//获取纬度
                                                    mLongitude = aMapLocation.getLongitude();//获取经度
                                                    Log.d(TAG, "纬度:" + mLatitude + "==经度:" + mLongitude + "== 定位来源:" + mLocType);
                                                    emitter.onNext(aMapLocation);
                                                    emitter.onComplete();
                                                } else {
                                                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                                                    emitter.onError(new RuntimeException("定位失败"));
                                                }
                                                mLocationClient.unRegisterLocationListener(this);
                                                mLocationClient.stopLocation();
                                            }
                                        }
                                    };
                                    mLocationClient.setLocationListener(mAMapLocationListener);
                                    if (mLocationClient != null && !mLocationClient.isStarted()) {
                                        //启动定位
                                        mLocationClient.startLocation();
                                    }
                                }
                            }
                        },
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE);
            }
        });
    }
}
