package com.dandy.module.camera.operation;

import java.util.List;

import com.dandy.helper.android.LogHelper;

import android.hardware.Camera;
import android.hardware.Camera.Size;

/**
 * 相机帮助类
 * 
 * @author dengchukun 2016年11月25日
 */
public class CameraHelper {

    private static final String TAG = CameraHelper.class.getSimpleName();
    private static float PREVIEW_SIZE_RATIO = 0;

    public static Size getOptimalPreviewSize(Camera camera, int width, int height) {
        Camera.Parameters params = camera.getParameters();
        List<Size> sizes = params.getSupportedPreviewSizes();
        for (Size size : sizes) {
            LogHelper.d(TAG, "width:" + size.width + " height " + size.height);
        }
        Size res = null;
        int k = 0, mk = 10000;
        for (Size size : sizes) {
            int min = 0, max = 0;
            min = Math.min(size.width, size.height);
            max = size.width + size.height - min;
            if ((max / 16 == min / 9)) {
                k = Math.abs(Math.min(width, height) - min);
                if (k < mk) {
                    mk = k;
                    res = size;
                    PREVIEW_SIZE_RATIO = (float) max / (float) min;
                }
                LogHelper.d(TAG, "SET width:" + size.width + " height " + size.height);
            }

        }
        if (res == null) {
            k = 0;
            mk = 10000;
            for (Size size : sizes) {
                int min = 0, max = 0;
                min = Math.min(size.width, size.height);
                max = size.width + size.height - min;
                if ((max / 4 == min / 3)) {
                    k = Math.abs(Math.min(width, height) - min);
                    if (k < mk) {
                        mk = k;
                        res = size;
                        PREVIEW_SIZE_RATIO = (float) max / (float) min;
                    }
                    LogHelper.d(TAG, "SET width:" + size.width + " height " + size.height);
                }

            }
        }
        return res;
    }

    //
    public static Size getOptimalPictureSize(Camera camera, int width, int height) {
        Camera.Parameters params = camera.getParameters();
        List<Size> sizes = params.getSupportedPictureSizes();
        for (Size size : sizes) {
            LogHelper.d(TAG, "width:" + size.width + " height " + size.height);
        }
        Size res = null;
        int k = 0, mk = 10000;
        for (Size size : sizes) {
            int min = 0, max = 0;
            min = Math.min(size.width, size.height);
            max = size.width + size.height - min;
            if (Math.abs((float) max / (float) min - PREVIEW_SIZE_RATIO) < 0.2) {
                k = Math.abs(Math.min(width, height) - min);
                if (k < mk) {
                    mk = k;
                    res = size;
                }
                LogHelper.d("pictureSize", "SET width:" + size.width + " height " + size.height);
            }
        }
        if (res == null) {
            for (Size size : sizes) {
                int min = 0, max = 0;
                min = Math.min(size.width, size.height);
                max = size.width + size.height - min;
                if ((max / 16 == min / 9) || (max / 4 == min / 3)) {
                    k = Math.abs(Math.min(width, height) - min);
                    if (k < mk) {
                        mk = k;
                        res = size;
                    }
                    LogHelper.d(TAG, "SET width:" + size.width + " height " + size.height);
                }
            }
        }
        return res;
    }
}
