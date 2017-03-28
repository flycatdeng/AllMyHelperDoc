package com.dandy.module.camera.operation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;

/**
 * <pre>
 * 空实现，如果这个模块不使用，可以将这个类重新命名为CameraOperationImp，用以空实现，这个类在实际过程中是没用的， 
 * 只不过是为了以后使用代码方便所以直接加在这里，如果真的用到了这个模块还需要依据实际情况删除
 *  The fake operation of the camera
 * </pre>
 * 
 * @author dengchukun 2016年11月25日
 */
public class CameraEmptyImp implements ICameraOperation, ICameraState {

    public CameraEmptyImp(Context context, Camera camera) {

    }

    @Override
    public boolean startRecord() {
        return false;
    }

    @Override
    public Bitmap stopRecord() {
        return null;
    }

    @Override
    public void switchCamera() {
    }

    @Override
    public FlashMode getFlashMode() {
        return null;
    }

    @Override
    public void setFlashMode(FlashMode flashMode) {
    }

    @Override
    public void takePicture(PictureCallback callback) {
    }

    @Override
    public int getMaxZoom() {
        return 0;
    }

    @Override
    public void setZoom(int zoom) {
    }

    @Override
    public int getZoom() {
        return 0;
    }

    @Override
    public void setResPath(String path) {
    }

    @Override
    public void startPreview() {
    }

    @Override
    public void openCamera(int viewWidth, int viewHeight) {

    }

    @Override
    public void setOperationCallback(IOperationCallback callback) {

    }

    @Override
    public void stopCamera() {

    }

    @Override
    public boolean isCameraNull() {
        return false;
    }

    @Override
    public boolean isPreviewing() {
        return false;
    }

    @Override
    public boolean isFrontCamera() {
        return false;
    }

    @Override
    public void setOnFocus(Point eventPoint, AutoFocusCallback autoFocusCallback) {

    }

}
