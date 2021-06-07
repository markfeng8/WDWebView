package com.wdweblib.ui.mediarecorder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.wdweblib.R;
import com.wdweblib.utils.DrawableUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

public class MediaRecorderActivity extends AppCompatActivity implements MediaRecorderView {
    private static final int MEDIA_RECORDER_REQUEST = 0;
    public static final String VIDEO_PATH = "video_path";
    private static final String PARAM_MIN = "min";
    private static final String PARAM_MAX = "max";

    public static Intent start(Context context, String minDuration, String maxDuration) {
        Intent starter = new Intent(context, MediaRecorderActivity.class);
        starter.putExtra(PARAM_MIN, minDuration);
        starter.putExtra(PARAM_MAX, maxDuration);
        return starter;
    }

    private TextureView mPreview;
    private Button captureButton;
    private Button btnStop;
    private TextView tvContent;

    private MediaRecorderPresent mPresent;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final String[] requiredPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
    };
    private String minDuration = "3";
    private String maxDuration = "5";
    private ImageView btnLeft;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wd_layout_media_recorder);
        Intent bundle = getIntent();
        if (bundle != null) {
            minDuration = bundle.getStringExtra(PARAM_MIN);
            maxDuration = bundle.getStringExtra(PARAM_MAX);
        }

        btnLeft = findViewById(R.id.btnLeft);
        tvTitle = findViewById(R.id.tvTitle);
        btnLeft.setOnClickListener(v -> finish());
        btnLeft.setImageDrawable(DrawableUtil.tintDrawable(this, R.mipmap.icon_back, R.color.white));
        tvTitle.setText("拍摄视频");

        tvContent = findViewById(R.id.tvContent);
        mPresent = new MediaRecorderPresent(this, compositeDisposable);
        mPreview = (TextureView) findViewById(R.id.surface_view);
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(v -> {
            mPresent.startVideoRecorder(Integer.parseInt(minDuration), Integer.parseInt(maxDuration));
        });
        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(v -> {
            mPresent.stopReleaseMediaRecorder();
        });
        录像前();
    }

    @Override
    public void showContent(String content) {
        tvContent.setText(content);
    }

    @Override
    public void 录像前() {
        captureButton.setVisibility(View.VISIBLE);
        captureButton.setText("开始录像");
        btnStop.setVisibility(View.GONE);
        tvContent.setText(String.format("请拍摄一段动态的人脸视频，持续%s到%s秒", minDuration, maxDuration));
    }

    @Override
    public void 录像中不可停止() {
        captureButton.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
    }

    @Override
    public void 录像中可停止() {
        captureButton.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
    }

    @Override
    public void 录像停止(String tip) {
        captureButton.setVisibility(View.VISIBLE);
        captureButton.setText("重新录像");
        tvContent.setText(tip);
        btnStop.setVisibility(View.GONE);
    }

    @Override
    public void finishWithResult(String base64) {
        Intent intent = new Intent();
        intent.putExtra(VIDEO_PATH, base64);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (areCameraPermissionGranted()) {
            Observable.timer(1, TimeUnit.SECONDS)
                    .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                    .subscribe(
                            aLong -> mPresent.startCameraPreview(this, mPreview)
                    );
        } else {
            requestPermission();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        // if we are using MediaRecorder, release it first
        mPresent.releaseMediaRecorder();
        // release the camera immediately on pause event
        mPresent.releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        if (mPresent != null) {
            mPresent.remove();
            mPresent = null;
        }
    }

    private boolean areCameraPermissionGranted() {
        for (String permission : requiredPermissions) {
            if (!(ActivityCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                MEDIA_RECORDER_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (MEDIA_RECORDER_REQUEST != requestCode) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        boolean areAllPermissionsGranted = true;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                areAllPermissionsGranted = false;
                break;
            }
        }

        if (areAllPermissionsGranted) {
            mPresent.startCameraPreview(this, mPreview);
        } else {
            // User denied one or more of the permissions, without these we cannot record
            // Show a toast to inform the user.
            Toast.makeText(getApplicationContext(),
                    "应用需要外置存储、录像、相机权限以使用相机获取影像。",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
