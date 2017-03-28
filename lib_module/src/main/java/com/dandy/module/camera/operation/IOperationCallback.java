package com.dandy.module.camera.operation;

import android.hardware.Camera;

/**
 * 
 * @author dengchukun 2016年11月25日
 */
public interface IOperationCallback {

    /**
     * <pre>
     * 在打开相机之前清空一些东西，这个主要是涉及到不同的view实现不同的显示方式
     * destroy some information if the camera is not null before open it
     * </pre>
     * 
     * @param mCamera
     */
    void onStopBeforOpenCamera(Camera camera);

    /**
     * <pre>
     * 得到了当前的相机预览界面的宽和高
     * </pre>
     * 
     * @param previewWidth
     * @param previewHeight
     */
    void onPreviewSizeGot(int previewWidth, int previewHeight);

    /**
     * <pre>
     * 在开启预览之前设置“画布”例如你是要设置setPreviewDisplay和setPreviewCallback还是绑定OpenGL的纹理ID
     * set the "canvas" before you start preview 
     * such as call setPreviewDisplay and setPreviewCallback method or bind the OpenGL texture id
     * </pre>
     * 
     * @param mCamera
     */
    void onSetSurfaceHolderBeforeStartPreview(Camera camera);

    /**
     * <pre>
     * 销毁Camera相关信息
     * destroy the camera information
     * </pre>
     * 
     * @param camera
     */
    void onDestroy(Camera camera);

}
