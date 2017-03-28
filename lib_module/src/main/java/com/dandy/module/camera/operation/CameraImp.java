package com.dandy.module.camera.operation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.android.MediaPlayerHelper;
import com.dandy.helper.android.SDCardHelper;
import com.dandy.helper.java.CalendarHelper;
import com.dandy.helper.java.ObjectHelper;

/**
 * The real operation of the camera
 * 
 * @author dengchukun 2016年11月25日
 */
public class CameraImp implements ICameraOperation, ICameraState {
    private static final String TAG = CameraImp.class.getSimpleName();
//    private Context mContext;
    private static final String FILE_DIRECTORY = "Dandy/Helper/Camera";
    private static final String VIDEO_DIRECTORY = "Video";
    private Camera mCamera;
    /** 相机配置，在录像前记录，用以录像结束后恢复原配置 */
    private Camera.Parameters mParameters;
    /** 当前缩放级别 默认为0 */
    private int mZoom = 0;
    /** 当前闪光灯类型，默认为关闭 */
    private FlashMode mFlashMode = FlashMode.ON;
    /** 录像类 */
    private MediaRecorder mMediaRecorder;
    /** 录像存放路径 ，用以生成缩略图 */
    private String mRecordDirectory = "";
    private String mRecordPath = "";
    private boolean mIsFrontCamera = false;
    private boolean mIsPreviewing = false;
    private IOperationCallback mOperationCallback;
    private int mPreviewViewWidth;
    private int mPreviewViewHeight;

    public CameraImp() {
//        mContext = context;
//        mCamera = camera;
        initRecordPath();
    }

    public ICameraOperation getOperationInstance() {
        return this;
    }

    public ICameraState getStateInstance() {
        return this;
    }

    /**
     * <pre>
     * 初始化视频存储路径,如果用户设置了则使用用户设置的
     * initial the saving path of the video,
     * we will use the value if someone invoked {@link #setResPath(String)}
     * </pre>
     */
    private void initRecordPath() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mRecordDirectory = SDCardHelper.getSDCardDirPath() + "/" + FILE_DIRECTORY + "/" + VIDEO_DIRECTORY;
    }

    @Override
    public boolean startRecord() {
        if (mCamera == null) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " mCamera == null");
            return false;
        }
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        else {
            mMediaRecorder.reset();
        }
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置录像参数，由于应用需要此处取一个较小格式的视频
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
        // 设置输出视频朝向，便于播放器识别。由于是竖屏录制，需要正转90°
        if (mIsFrontCamera) {
            mMediaRecorder.setOrientationHint(270);
        } else {
            mMediaRecorder.setOrientationHint(90);
        }
        File directory = new File(mRecordDirectory);
        if (!directory.exists())
            directory.mkdirs();
        try {
            if (ObjectHelper.isNull(mRecordPath)) {
                String name = "video" + CalendarHelper.getFormatDate("yyyyMMddHHmmss") + ".3gp";
                mRecordPath = mRecordDirectory + File.separator + name;
            }
            File mRecAudioFile = new File(mRecordPath);
            mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Bitmap stopRecord() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        Bitmap bitmap = null;
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                // 保存视频的缩略图
                bitmap = MediaPlayerHelper.getVideoThumbnail(mRecordPath);
            }
            if (mParameters != null && mCamera != null) {
                // 重新连接相机
                mCamera.reconnect();
                // 停止预览，注意这里必须先调用停止预览再设置参数才有效
                mCamera.stopPreview();
                // 设置参数为录像前的参数，不然如果录像是低配，结束录制后预览效果还是低配画面
                mCamera.setParameters(mParameters);
                // 重新打开
                mCamera.startPreview();
                mParameters = null;
            }
        } catch (IOException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " exception=" + e.getMessage());
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void switchCamera() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mIsFrontCamera = !mIsFrontCamera;
    }

    @Override
    public void openCamera(int viewWidth, int viewHeight) {
        mPreviewViewWidth = viewWidth;
        mPreviewViewHeight = viewHeight;
        if (mCamera != null) {
            // TODO
//            mCamera.setPreviewCallback(null);
            if (mOperationCallback != null) {
                mOperationCallback.onStopBeforOpenCamera(mCamera);
            }
            // TODO
            mIsPreviewing = false;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if (mIsFrontCamera) {
            mCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
        } else {
            mCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
        }
        LogHelper.d(TAG, LogHelper.getThreadName() + " mCamera=" + mCamera);
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            Size preivewSize = CameraHelper.getOptimalPreviewSize(mCamera, viewWidth, viewHeight);
            // TODO
            if (mOperationCallback != null) {
                mOperationCallback.onPreviewSizeGot(preivewSize.width, preivewSize.height);
            }
            // TODO
            parameters.setPreviewSize(preivewSize.width, preivewSize.height);
            Size pictureSize = CameraHelper.getOptimalPictureSize(mCamera, preivewSize.width, preivewSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            parameters.setPreviewFormat(ImageFormat.NV21);

            // 设置闪光灯模式。此处主要是用于在相机摧毁后又重建，保持之前的状态
            if (!mIsFrontCamera) {
                // 自动聚焦模式
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                setFlashMode(mFlashMode);
                // 设置缩放级别
                setZoom(mZoom);
            }
            try {
                mCamera.setParameters(parameters);
            } catch (Exception e) {
                LogHelper.d(TAG, "Setting parameter failed in Open Camera!");
            }
        }
    }

    @Override
    public void startPreview() {
        LogHelper.d(TAG, LogHelper.getThreadName() + " mIsPreviewing=" + mIsPreviewing);
        if (mIsPreviewing) {
            return;
        }
        if (mCamera == null) {
            throw new RuntimeException(TAG + LogHelper.getThreadName() + " mCamera==null");
        }
        // TODO
//        mCamera.setPreviewCallback(mPreviewCallback);
//        try {
//            mCamera.setPreviewDisplay(mSurfaceHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (mOperationCallback != null) {
            mOperationCallback.onSetSurfaceHolderBeforeStartPreview(mCamera);
        }
        // TODO
        mCamera.startPreview();
        mIsPreviewing = true;
    }

    @Override
    public FlashMode getFlashMode() {
        return mFlashMode;
    }

    @Override
    public void setFlashMode(FlashMode flashMode) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mCamera == null)
            return;
        mFlashMode = flashMode;
        Camera.Parameters parameters = mCamera.getParameters();
        switch (flashMode) {
            case ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                break;
            case AUTO:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case TORCH:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            default:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
        mCamera.setParameters(parameters);
    }

    @Override
    public int getMaxZoom() {
        if (mCamera == null)
            return -1;
        Camera.Parameters parameters = mCamera.getParameters();
        if (!parameters.isZoomSupported())
            return -1;
        return parameters.getMaxZoom();
    }

    @Override
    public void setZoom(int zoom) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mCamera == null)
            return;
        Camera.Parameters parameters;
        // 注意此处为录像模式下的setZoom方式。在Camera.unlock之后，调用getParameters方法会引起android框架底层的异常
        // stackoverflow上看到的解释是由于多线程同时访问Camera导致的冲突，所以在此使用录像前保存的mParameters。
        if (mParameters != null)
            parameters = mParameters;
        else {
            parameters = mCamera.getParameters();
        }

        if (!parameters.isZoomSupported())
            return;
        parameters.setZoom(zoom);
        mCamera.setParameters(parameters);
        mZoom = zoom;
    }

    @Override
    public int getZoom() {
        return mZoom;
    }

    @Override
    public void takePicture(PictureCallback callback) {
        mCamera.takePicture(null, null, callback);
    }

    @Override
    public void setResPath(String path) {
        mRecordPath = path;
    }

    @Override
    public void setOperationCallback(IOperationCallback callback) {
        mOperationCallback = callback;
    }

    @Override
    public void stopCamera() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mCamera != null) {
            // TODO
//            mCamera.setPreviewCallback(null);
            if (mOperationCallback != null) {
                mOperationCallback.onDestroy(mCamera);
            }
            mCamera.stopPreview();
            mIsPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public boolean isCameraNull() {
        return mCamera == null;
    }

    @Override
    public boolean isPreviewing() {
        return mIsPreviewing;
    }

    @Override
    public boolean isFrontCamera() {
        return mIsFrontCamera;
    }

    @Override
    public void setOnFocus(Point eventPoint, AutoFocusCallback autoFocusCallback) {
        if (mCamera == null) {
            openCamera(mPreviewViewWidth, mPreviewViewHeight);
        }
        Camera.Parameters parameters = mCamera.getParameters();
        // 不支持设置自定义聚焦，则使用自动聚焦，返回setFocusAreas
        if (parameters.getMaxNumFocusAreas() <= 0) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " parameters.getMaxNumFocusAreas()<=0");
            mCamera.autoFocus(autoFocusCallback);
            return;
        }
        List<Area> areas = new ArrayList<Area>();
        int left = eventPoint.x - 300;
        int top = eventPoint.y - 300;
        int right = eventPoint.x + 300;
        int bottom = eventPoint.y + 300;
        left = left < -1000 ? -1000 : left;
        top = top < -1000 ? -1000 : top;
        right = right > 1000 ? 1000 : right;
        bottom = bottom > 1000 ? 1000 : bottom;
        areas.add(new Area(new Rect(left, top, right, bottom), 100));
        parameters.setFocusAreas(areas);
        try {
            // 本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
            // 目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
            mCamera.setParameters(parameters);
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " e=" + e.getMessage());
            e.printStackTrace();
        }
        LogHelper.d(TAG, LogHelper.getThreadName() + "last");
        mCamera.autoFocus(autoFocusCallback);
    }

}
