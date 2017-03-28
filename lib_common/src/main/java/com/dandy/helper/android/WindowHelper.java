package com.dandy.helper.android;

import java.lang.reflect.Method;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 窗口帮助类
 * 
 * @author dengchukun
 * 
 */
public class WindowHelper {
    private static final String TAG = "WindowHelper";
    private static int sWindowWidth;
    private static int sWindowHeight;
    private static boolean sIsInited;

    /**
     * 得到屏幕宽
     * 
     * @return
     */
    public static int getWindowWidth() {
        if (!sIsInited) {
            throw new RuntimeException("WindowHelper getWindowWidth did not invoke initWindowWH");
        }
        return sWindowWidth;
    }

    /**
     * 得到屏幕高
     * 
     * @return
     */
    public static int getWindowHeight() {
        if (!sIsInited) {
            throw new RuntimeException("WindowHelper getWindowHeight did not invoke initWindowWH");
        }
        return sWindowHeight;
    }

    /**
     * 得到面积
     * 
     * @return
     */
    public static int getWindowArea() {
        if (!sIsInited) {
            throw new RuntimeException("WindowHelper getWindowArea did not invoke initWindowWH");
        }
        return sWindowWidth * sWindowHeight;
    }

    /**
     * 初始化窗口信息,得到窗口的高和宽
     * 
     * @param context
     */
    public static void initWindowWH(Context context) {
        if (sIsInited) {
            return;
        }
        WindowManager mageger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        mageger.getDefaultDisplay().getMetrics(mDisplayMetrics);
        sWindowWidth = mDisplayMetrics.widthPixels;
        sWindowHeight = mDisplayMetrics.heightPixels;
        LogHelper.d(TAG, LogHelper.getThreadName() + "sWindowWidth-" + sWindowWidth + " sWindowHeight-" + sWindowHeight);
        sIsInited = true;
    }

    // 获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getCellPhoneHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }
}
// Gionee <dengck><2014-01-24> Add for CR01032821 end