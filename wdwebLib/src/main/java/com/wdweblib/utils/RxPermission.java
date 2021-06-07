package com.wdweblib.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * code by markfeng
 * <p>
 * create on 2021-06-02 16:26
 */
public class RxPermission {

    protected RxPermissions rxPermissions;

    private RxPermission() {
    }

    private static class RxPermissionInstance {
        private static final RxPermission INSTANCE = new RxPermission();
    }

    public static RxPermission getInstance() {
        return RxPermission.RxPermissionInstance.INSTANCE;
    }

    public interface PermissionsImp {
        void accept(Permission permission);
    }

    public void requestPermissions(FragmentActivity activity,
                                   final String prompt,
                                   PermissionsImp imp,
                                   String... permissions) {
        rxPermissions = new RxPermissions(activity);
        rxPermissions.requestEachCombined(permissions)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // All permissions are granted !
//                            askForPermission(prompt);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // At least one denied permission without ask never again
//                            askForPermission(prompt);
                        } else {
                            // At least one denied permission with ask never again
                            // Need to go to the settings
                            askForPermission(activity, prompt);
                        }
                        if (imp != null) {
                            imp.accept(permission);
                        }
                    }
                });
    }

    public Observable<Permission> requestPermissionsX(FragmentActivity activity,
                                                      final String prompt,
                                                      String... permissions) {
        rxPermissions = new RxPermissions(activity);
       return Observable.create(new ObservableOnSubscribe<Permission>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Permission> emitter) throws Exception {
                rxPermissions.requestEachCombined(permissions)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    // All permissions are granted !
//                            askForPermission(prompt);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    // At least one denied permission without ask never again
//                            askForPermission(prompt);
                                } else {
                                    // At least one denied permission with ask never again
                                    // Need to go to the settings
                                    askForPermission(activity, prompt);
                                }
                                emitter.onNext(permission);
                            }
                        });
            }
        });

    }

    protected void askForPermission(FragmentActivity activity, String prompt) {
        if (activity != null & !activity.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setTitle("提示");
            builder.setMessage(prompt);
            builder.setNeutralButton("否", null);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName())); // 根据包名打开对应的设置界面
                    activity.startActivity(intent);
                }
            });
            builder.create().show();
        }
    }
}
