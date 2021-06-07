package com.zxing.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.wdweblib.R;
import com.wdweblib.utils.StatusBarUtils;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.decoding.RGBLuminanceSource;
import com.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends AppCompatActivity implements Callback {

    private static final int REQUEST_CODE_SCAN_GALLERY = 100;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private ImageView back;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ProgressDialog mProgress;
    private String photo_path;
    private Bitmap scanBitmap;
    private LinearLayout llTitle;
    @ColorRes
    private static int mTitleBarColorRes = -1;
    public static final int RESULT_CODE_QR_SCAN = 0xA1;
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    private int cameraId = -1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.tint(this);
        setContentView(R.layout.wd_activity_scanner);
        Intent intent = getIntent();
        if (intent != null) {
            cameraId = intent.getIntExtra("CAMERA_ID", -1);
        }
        CameraManager.init(getApplication());

        llTitle = findViewById(R.id.llTitle);
        viewfinderView = findViewById(R.id.viewfinder_content);
        back = findViewById(R.id.scanner_toolbar_back);

        setTitleBackgroundColor();

        back.setOnClickListener(v -> finish());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    private void setTitleBackgroundColor() {
        if (mTitleBarColorRes != -1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                llTitle.setBackgroundColor(getResources().getColor(mTitleBarColorRes, null));
            } else {
                llTitle.setBackgroundColor(getResources().getColor(mTitleBarColorRes));
            }
        }
    }

    /**
     * 打开手机中的相册
     */
    private void openMobileGallery() {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        this.startActivityForResult(wrapperIntent, REQUEST_CODE_SCAN_GALLERY);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK && requestCode == REQUEST_CODE_SCAN_GALLERY) {
            // 获取选中图片的路径
            Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            if (cursor.moveToFirst()) {
                photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();

            mProgress = new ProgressDialog(CaptureActivity.this);
            mProgress.setMessage("正在扫描...");
            mProgress.setCancelable(false);
            mProgress.show();

            new Thread(() -> {
                Result result = scanningImage(photo_path);
                if (result != null) {
                    Intent resultIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, result.getText());
                    resultIntent.putExtras(bundle);
                    CaptureActivity.this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
                } else {
                    Message m = handler.obtainMessage();
                    m.what = R.id.decode_failed;
                    m.obj = "Scan failed!";
                    handler.sendMessage(m);
                }
            }).start();
        }
    }

    /**
     * 扫描二维码图片的方法
     *
     * @param path image path
     * @return scanner result
     */
    public Result scanningImage(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        // 设置二维码内容的编码
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 先获取原大小
        options.inJustDecodeBounds = true;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        // 获取新的大小
        options.inJustDecodeBounds = false;
        int sampleSize = (int) (options.outHeight / (float) 200);
        if (sampleSize <= 0) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        scanBitmap = BitmapFactory.decodeFile(path, options);
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            return reader.decode(bitmap1, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = findViewById(R.id.scanner_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * Handler scan result
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, resultString);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_CODE_QR_SCAN, resultIntent);
        }
        CaptureActivity.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            if (cameraId == -1) {
                CameraManager.get().openDriver(surfaceHolder);
            } else {
                CameraManager.get().openDriver(surfaceHolder, cameraId);
                CameraManager.get().setCameraDisplayOrientation(this, cameraId);
            }
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = mediaPlayer -> mediaPlayer.seekTo(0);

    public static void actionStart(Context context, int requestCode) {
        actionStart(context, requestCode, -1);
    }

    public static void actionStart(Context context, int requestCode, int cameraId) {
        actionStart(context, requestCode, -1, cameraId);
    }

    /**
     * The override method that launcher activity.
     *
     * @param context     activity context
     * @param requestCode request code
     * @param id          Title bar background color resource id.
     */
    public static void actionStart(Context context, int requestCode, @ColorRes int id, int cameraId) {
        if (context == null) {
            return;
        }
        mTitleBarColorRes = id;
        Intent intent = new Intent(context, CaptureActivity.class);
        intent.putExtra("CAMERA_ID", cameraId);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public static void actionStart(Fragment fragment, int requestCode) {
        actionStart(fragment, requestCode, -1);
    }

    /**
     * The override method that launcher activity.
     *
     * @param fragment    fragment object
     * @param requestCode request code
     * @param id          Title bar background color resource id.
     */
    public static void actionStart(Fragment fragment, int requestCode, @ColorRes int id) {
        actionStart(fragment, requestCode, id, -1);
    }

    public static void actionStart(Fragment fragment, int requestCode, @ColorRes int id, int cameraId) {
        if (fragment == null) {
            return;
        }
        mTitleBarColorRes = id;
        Intent intent = new Intent(fragment.getActivity(), CaptureActivity.class);
        intent.putExtra("CAMERA_ID", cameraId);
        fragment.startActivityForResult(intent, requestCode);
    }
}