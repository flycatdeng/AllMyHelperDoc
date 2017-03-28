package com.dandy.helper.android;

import com.dandy.helper.java.ObjectHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;

/**
 * 手电筒帮助类
 * <p>
 * 1.需要添加以下权限
 * <p>
 * uses-permission android:name="android.permission.FLASHLIGHT"
 * <p>
 * uses-permission android:name="android.permission.CAMERA"
 * <p>
 * uses-feature android:name="android.hardware.camera"
 * <p>
 * uses-feature android:name="android.hardware.autofocus"
 * 
 * @author dengchukun
 * 
 */
@SuppressLint("InlinedApi")
public class FlashLightHelper {
    private static final String TAG = "FlashLightHelper";

    // / Operate Result
    public static final int CAN_NOT_USE_FLASH_LIGHT = 0;
    public static final int SUCCEED = 1;
    public static final int FAILED = -1;

    public FlashLightHelper(Context context) {

    }

    private boolean mIsLightOn = false;
    private Camera mCamera;

    public boolean getIsOn() {
        return mIsLightOn;
    }

    public int turnLightOn() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (!ConfigHelper.isCanUseFlashLight()) {
            return CAN_NOT_USE_FLASH_LIGHT;
        }
        try {
            mCamera = Camera.open();
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(parameters);
            mIsLightOn = true;
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + e.getMessage());
            return FAILED;
        }
        return SUCCEED;
    }

    public int turnLightOff() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (!ConfigHelper.isCanUseFlashLight()) {
            return CAN_NOT_USE_FLASH_LIGHT;
        }
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(parameters);
            mCamera.release();
            mIsLightOn = false;
        } catch (Exception e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + e.getMessage());
            return FAILED;
        }
        return SUCCEED;
    }

    /**
     * 如果没有关闭手电筒则关闭
     * <p>
     * 必须要调用这个方法
     */
    public void onDestroy() {
        LogHelper.d(TAG, LogHelper.getThreadName() + "mIsLightOn-" + mIsLightOn);
        if (ObjectHelper.isNotNull(mCamera)) {
            turnLightOff();
        }
    }
}
