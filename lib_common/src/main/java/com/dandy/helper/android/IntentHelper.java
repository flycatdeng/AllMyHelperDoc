package com.dandy.helper.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class IntentHelper {

    /**
     * 跳转到一个应用的页面(可以是其他应用程序的页面)
     * 
     * @param context
     *            :上下文
     * @param targetPakageName
     *            :包名如com.android.deskclock
     * @param targetClassFullName
     *            :类名如com.android.deskclock.AlarmClock
     */
    public static void startActivityByComponentName(Context context, String targetPakageName, String targetClassFullName) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName(targetPakageName, targetClassFullName);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    /**
     * 发送一个action给广播,对该action有监听的广播接收器都可以截获
     */
    public static void sendActionToBroadcast(Context context, String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    /**
     * 去往某个动态壁纸的预览页面,那里可以设置壁纸
     * 
     * @param context
     * @param packageName
     *            动态壁纸的包名
     * @param classFullName
     *            动态壁纸service类的类全名
     */
    @SuppressLint("InlinedApi")
    public static void startLiveWallpaperPrevivew(Context context, String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, componentName);
        context.startActivity(intent);
    }

    /**
     * 去往某个动态壁纸的预览页面,那里可以设置壁纸
     * 
     * @param context
     * @param packageName
     *            动态壁纸的包名
     * @param classFullName
     *            动态壁纸service类的类全名
     */
    public static void startLiveWallpaperPrevivew(Activity activity, String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        activity.startActivity(intent);
    }
}
