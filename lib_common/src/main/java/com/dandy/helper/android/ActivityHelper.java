package com.dandy.helper.android;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ActivityHelper {
    /**
     * 设置activity没有标题
     */
    public static void setActivityNoTitle(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置activity全屏
     */
    public static void setActivityFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置为横屏模式
     */
    public static void setActivityHengScreen(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置为竖屏模式
     */
    public static void setActivityShuScreen(Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 全屏，并且隐藏导航栏
     * 
     * @param activity
     */
    public static void hideNavigationBar(Activity activity) {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use
                                   // magic number directly for higher API target level
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * <pre>
     * 得到屏幕最顶层的Activity
     * </pre>
     * 
     * @param context
     * @return
     */
    public static Activity getTopActivity(Context context) {
        String atyName = ComponentNameHelper.getTopActivityComponentName(context).getClassName();
        return ExitApplication.getInstance().getActivity(atyName);
    }
}
