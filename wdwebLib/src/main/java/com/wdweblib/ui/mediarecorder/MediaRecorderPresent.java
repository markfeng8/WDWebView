package com.wdweblib.ui.mediarecorder;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.TextureView;


import com.wdweblib.utils.CameraHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class MediaRecorderPresent {
    private MediaRecorderView mView;
    private static final String TAG = "Recorder";

    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;
    CamcorderProfile profile = CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,
            CamcorderProfile.QUALITY_QVGA);
    private CompositeDisposable compositeDisposable;

    public MediaRecorderPresent(MediaRecorderView mediaRecorderView, CompositeDisposable compositeDisposable) {
        this.mView = mediaRecorderView;
        this.compositeDisposable = compositeDisposable;
    }

    void startCameraPreview(Activity activity, TextureView mPreview) {
        mCamera = CameraHelper.getDefaultFrontFacingCameraInstance();
        if (mCamera != null) {
            // BEGIN_INCLUDE (configure_preview)
            CameraHelper.setCameraDisplayOrientation(activity, Camera.CameraInfo.CAMERA_FACING_FRONT, mCamera);

            // We need to make sure that our preview and recording video size are supported by the
            // camera. Query camera to find all the sizes and choose the optimal size given the
            // dimensions of our preview surface.
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
            List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
            Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                    mSupportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());

            // Use the same size for recording profile.
            profile.videoFrameWidth = optimalSize.width;
            profile.videoFrameHeight = optimalSize.height;

            // likewise for the camera object itself.
            parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
            mCamera.setParameters(parameters);
            try {
                // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
                // with {@link SurfaceView}
                mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
                mCamera.startPreview();
            } catch (IOException e) {
                Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
                //
            }
            // END_INCLUDE (configure_preview)
        }
    }

    public void startVideoRecorder(int minDuration, int maxDuration) {
        Observable.create(emitter -> {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                emitter.onError(new RuntimeException(""));
            }
            emitter.onNext(true);
            emitter.onComplete();
        })
                .flatMap(o ->
                        Observable.intervalRange(0, maxDuration + 1, 0, 1, TimeUnit.SECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> compositeDisposable.add(disposable))
                .subscribe(aLong -> {
                            mView.showContent(String.format("已拍摄：%d秒", aLong));
                            if (aLong <= minDuration) {
                                mView.录像中不可停止();
                            } else {
                                mView.录像中可停止();
                            }
                        }, throwable -> mView.录像停止("录像出错")
                        , () -> stopReleaseMediaRecorder());
    }

    private boolean prepareVideoRecorder() {
        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set all values contained in profile except audio settings
        mMediaRecorder.setOutputFormat(profile.fileFormat);
        mMediaRecorder.setVideoEncoder(profile.videoCodec);
        mMediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoFrameRate(profile.videoFrameRate);
        mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);

        // Step 4: Set output file
        mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
        if (mOutputFile == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(mOutputFile.getPath());
        // END_INCLUDE (configure_media_recorder)
        //视频生成后预览图像的角度，算不来写死了。。
        mMediaRecorder.setOrientationHint(270);
        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder.reset();
            // release the recorder object
            mMediaRecorder.release();
            mMediaRecorder = null;
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            if (mCamera != null) {
                mCamera.lock();
            }
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean mStoppingMR;
    private boolean mFinishingDoNotDelete;

    public void stopReleaseMediaRecorder() {
        if (mStoppingMR) {
            return;
        }
        mStoppingMR = true;
        // BEGIN_INCLUDE(stop_release_media_recorder)

        // stop recording and release camera
        try {
            mMediaRecorder.stop();  // stop the recording
        } catch (RuntimeException e) {
            // RuntimeException is thrown when stop() is called immediately after start().
            // In this case the output file is not properly constructed ans should be deleted.
            Log.d(TAG, "RuntimeException: stop() is called immediately after start()");
            //noinspection ResultOfMethodCallIgnored
            if (!mFinishingDoNotDelete) {
                mOutputFile.delete();
            }
        }
        releaseMediaRecorder(); // release the MediaRecorder object
        if (mCamera != null) {
            mCamera.lock();         // take camera access back from MediaRecorder
        }

        // inform the user that recording has stopped
        if (mOutputFile.exists()) {
            mFinishingDoNotDelete = true;
            mView.录像停止("录像完成");
            mView.finishWithResult(mOutputFile.getAbsolutePath());
        } else {
            mView.录像停止("生成视频失败，请重试");
        }
        // END_INCLUDE(stop_release_media_recorder)
        mStoppingMR = false;
    }

    public void remove() {
        this.mView = null;
        this.compositeDisposable = null;
    }
}
