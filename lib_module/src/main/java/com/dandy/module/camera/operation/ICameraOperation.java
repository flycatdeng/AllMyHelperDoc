package com.dandy.module.camera.operation;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;

/**
 * @Description:相机操作接口
 * 
 * @author dengchukun 2016年11月23日
 */
public interface ICameraOperation {
    /**
     * <pre>
     * 开始录像
     * start to record
     * </pre>
     * 
     * @return
     * 
     *         <pre>
     *是否成功开始录像 
     *the action start to record is successful or not
     *         </pre>
     */
    public boolean startRecord();

    /**
     * <pre>
     * 停止录像
     * stop record
     * </pre>
     * 
     * @return 录像缩略图
     */
    public Bitmap stopRecord();

    /**
     * <pre>
     * 切换前置和后置相机
     * switch the direction of the camera
     * </pre>
     */
    public void switchCamera();

    /**
     * 
     * <pre>
     * 获取当前闪光灯模式
     * get the flash mode of the camera
     * </pre>
     * 
     * @return
     */
    public FlashMode getFlashMode();

    /**
     * 
     * <pre>
     * 设置闪光灯模式
     * set the flash mode of the camera
     * </pre>
     * 
     * @param flashMode
     */
    public void setFlashMode(FlashMode flashMode);

    /**
     * 拍照
     * 
     * @param callback
     *            拍照回调函数
     * @param listener
     *            拍照动作监听函数
     */
    public void takePicture(PictureCallback callback);

    /**
     * 相机最大缩放级别
     * 
     * @return
     */
    public int getMaxZoom();

    /**
     * 设置当前缩放级别
     * 
     * @param zoom
     */
    public void setZoom(int zoom);

    /**
     * 获取当前缩放级别
     * 
     * @return
     */
    public int getZoom();

    /**
     * <pre>
     * 设置图片或视频的存储路径
     * set the image or video saving path
     * </pre>
     * 
     * @param path
     *            TODO
     */
    public void setResPath(String path);

    /**
     * <pre>
     * 相机开始预览
     * the camera start to preview
     * </pre>
     */
    void startPreview();

    /**
     * <pre>
     * 打开相机，如果不打卡的话预览会直接报错
     * open camera, 
     * the program will throw a RuntimeException without opened the camera 
     * when you invoke method {@link #startPreview()}
     * </pre>
     * 
     * @param viewWidth
     *            width of the View you display
     * @param viewHeight
     *            height of the View you display
     */
    void openCamera(int viewWidth, int viewHeight);

    /**
     * <pre>
     * 设置操作时的回调，因为不同的view绑定的内容不同，所以在这个回调里操作
     * callback when operation since different view will bind different "canvas"
     * </pre>
     * 
     * @param callback
     */
    void setOperationCallback(IOperationCallback callback);

    /**
     * <pre>
     * 销毁Camera相关信息
     * destroy the camera information
     * </pre>
     */
    void stopCamera();

    /**
     * <pre>
     * 设置对焦
     * set on focus in one point the user click
     * </pre>
     * 
     * @param eventPoint
     *            用户点击的点，view中的坐标，而不是全屏坐标，即通过event.getX/Y来获得
     * @param autoFocusCallback
     *            对焦结果的回调
     */
    void setOnFocus(Point eventPoint, AutoFocusCallback autoFocusCallback);
}
