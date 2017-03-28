package com.dandy.module.camera.operation;

import com.dandy.module.IModuleInterface;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;

/**
 * <pre>
 * 这个类有两个作用，一个是实现模块化接口用以判断这个功能模块是否用于项目
 * 一个是相当于代理的作用，具体的实现由CameraOperationImp实现
 * This class has two functions.
 * On the one hand , it is the instance of the module interface to declare whether the code will be used in the program.
 * On the other hand , it is a proxy of ICameraOperation
 * </pre>
 * 
 * <p style="color:red">
 * 需要照相需要权 uses-permission android:name="android.permission.CAMERA"
 * </p>
 * 
 * @author dengchukun 2016年11月25日
 */
public class CameraManager implements IModuleInterface, ICameraOperation, ICameraState {
    private ICameraOperation mCameraOperation;
    private ICameraState mCameraState;
//    public void init(Context context, Camera camera) {
//        /**
//         * TODO if isModuleTurnedOn returns false,this should be replaced by an empty Implementation
//         */
//        mCameraOperation = new CameraOperationImp(context, camera);
//    }

    @Override
    public boolean startRecord() {
        return mCameraOperation.startRecord();
    }

    @Override
    public Bitmap stopRecord() {
        return mCameraOperation.stopRecord();
    }

    @Override
    public void switchCamera() {
        mCameraOperation.switchCamera();
    }

    @Override
    public FlashMode getFlashMode() {
        return mCameraOperation.getFlashMode();
    }

    @Override
    public void setFlashMode(FlashMode flashMode) {
        mCameraOperation.setFlashMode(flashMode);
    }

    @Override
    public void takePicture(PictureCallback callback) {
        mCameraOperation.takePicture(callback);
    }

    @Override
    public int getMaxZoom() {
        return mCameraOperation.getMaxZoom();
    }

    @Override
    public void setZoom(int zoom) {
        mCameraOperation.setZoom(zoom);
    }

    @Override
    public int getZoom() {
        return mCameraOperation.getZoom();
    }

    @Override
    public void setResPath(String path) {
        mCameraOperation.setResPath(null);
    }

    @Override
    public void startPreview() {
        mCameraOperation.startPreview();
    }

    @Override
    public void openCamera(int viewWidth, int viewHeight) {
        mCameraOperation.openCamera(viewWidth, viewHeight);
    }

    @Override
    public void setOperationCallback(IOperationCallback callback) {
        mCameraOperation.setOperationCallback(callback);
    }

    @Override
    public void stopCamera() {
        mCameraOperation.stopCamera();
    }

    @Override
    public void setOnFocus(Point eventPoint, AutoFocusCallback autoFocusCallback) {
        mCameraOperation.setOnFocus(eventPoint, autoFocusCallback);
    }

    @Override
    public boolean isCameraNull() {
        return mCameraState.isCameraNull();
    }

    @Override
    public boolean isPreviewing() {
        return mCameraState.isPreviewing();
    }

    @Override
    public boolean isFrontCamera() {
        return mCameraState.isFrontCamera();
    }

    /************************** instance *****************************/
    private static CameraManager sManager;

    private CameraManager() {
        CameraImp cameraImp = new CameraImp();
        mCameraOperation = cameraImp.getOperationInstance();
        mCameraState = cameraImp.getStateInstance();
    }

    public static CameraManager getInstance() {
        if (sManager == null) {
            sManager = new CameraManager();
        }
        return sManager;
    }

    /************************** IModuleInterface *****************************/
    @Override
    public boolean isModuleTurnedOn() {
        return true;
    }

}
