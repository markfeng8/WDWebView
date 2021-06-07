package com.wdweblib.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wdweblib.R;
import com.wdweblib.bean.Maps;
import com.wdweblib.utils.GlideEngine;
import com.wdweblib.utils.ImageOperation;
import com.wdweblib.utils.StatusBarUtils;
import com.wdweblib.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by x-sir on 2019/09/10 :)
 * Function:照片选择库页面
 */
public class PhotoLibraryActivity extends AppCompatActivity {

    private int maxCount;
    private int maxBytes;

    private static final String MAX_COUNT = "MAX_COUNT";
    private static final String MAX_BYTES = "MAX_BYTES";
    private static final String TAG = "PhotoLibraryActivity";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    private static OnSelectListener mOnSelectListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.tint(this);
        setContentView(R.layout.wd_activity_photo_library);
        Intent intent = getIntent();
        if (intent != null) {
            maxCount = intent.getIntExtra(MAX_COUNT, 0);
            maxBytes = intent.getIntExtra(MAX_BYTES, 0);
        }
        requestStoragePermission();
    }

    private void requestStoragePermission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        mCompositeDisposable.add(
                rxPermissions
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                openPhotoLibrary();
                            } else {
                                ToastUtils.showToast("您拒绝了权限！！！");
                            }
                        })
        );
    }

    private void openPhotoLibrary() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(maxCount)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(List<LocalMedia> result) {
                        // onResult Callback
                        if (result.size() > 0) {
                            opera(result);
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onCancel() {
                        // onCancel Callback
                        finish();
                    }
                });
    }

    private void opera(List<LocalMedia> list) {
        mCompositeDisposable.add(
                Observable
                        .fromIterable(list)
                        .filter(localMedia -> localMedia != null)
                        .map(LocalMedia::getRealPath)
                        .map(s -> "file:///" + s)
                        .map(this::getUriByPath)
                        .map(uri -> getImageBase64Str(uri, maxBytes))
                        .filter(s -> !TextUtils.isEmpty(s))
                        .map(s -> "data:image/png;base64," + s)
                        .toList()
                        .map(this::getJsonByList)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {

                            if (mOnSelectListener != null) {
                                mOnSelectListener.onResult(s);
                            }
                            // 包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                            PictureFileUtils.deleteCacheDirFile(PhotoLibraryActivity.this,
                                    PictureConfig.TYPE_IMAGE);
                            finish();
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                String a = throwable.toString();
                            }
                        })
        );

    }

    private String getCompressPath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CompressDir";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public <T> SingleTransformer<T, T> singleToMain() {
        SingleTransformer transformer = new SingleTransformer() {
            @NonNull
            @Override
            public SingleSource apply(@NonNull Single upstream) {
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                return null;
            }
        };
        return transformer;
    }


    private String getJsonByList(List<String> list) {
        HashMap<String, Object> map = Maps.newHashMapWithExpectedSize(1);
        map.put("images", list);
        return new Gson().toJson(map);
    }

    private Uri getUriByPath(String path) {
        Uri picUri = Uri.parse(path);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = this.getPackageName() + ".FileProvider";
            picUri = FileProvider.getUriForFile(this, authority, new File(picUri.getPath()));
        }
        return picUri;
    }

    private String getImageBase64Str(Uri imageUri, int maxBytes) {
        String base64Str = "";
        try {
            Bitmap bitmapFormUri = ImageOperation.getBitmapFormUri(this, imageUri, maxBytes);
            base64Str = ImageOperation.bitmapToBase64(bitmapFormUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Str;
    }

    public static void actionStart(Context context, int maxCount, int maxBytes, OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
        if (context != null) {
            Intent intent = new Intent(context, PhotoLibraryActivity.class);
            intent.putExtra(MAX_COUNT, maxCount);
            intent.putExtra(MAX_BYTES, maxBytes);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public interface OnSelectListener {
        void onResult(String json);
    }

}
