package com.dandy.helper.android;

import android.app.Activity;
import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class WallpaperHelper {

    /**
     * 判断一个动态壁纸是否已经在运行
     * 
     * @param context
     *            :上下文
     * @param tagetPackageName
     *            :要判断的动态壁纸的包名
     * @return
     */
    public static boolean isLiveWallpaperRunning(Context context, String tagetPackageName) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);// 得到壁纸管理器
        WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();// 如果系统使用的壁纸是动态壁纸话则返回该动态壁纸的信息,否则会返回null
        if (wallpaperInfo != null) {// 如果是动态壁纸,则得到该动态壁纸的包名,并与想知道的动态壁纸包名做比较
            String currentLiveWallpaperPackageName = wallpaperInfo.getPackageName();
            if (currentLiveWallpaperPackageName.equals(tagetPackageName)) {
                Toast.makeText(context, "该动态壁纸已经在运行了", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
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
        activity.startActivityForResult(intent, 0);
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
    public static void startLiveWallpaperPrevivew(Context context, String packageName, String classFullName) {
        ComponentName componentName = new ComponentName(packageName, classFullName);
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT < 16) {
            intent = new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        } else {
            intent = new Intent("android.service.wallpaper.CHANGE_LIVE_WALLPAPER");
            intent.putExtra("android.service.wallpaper.extra.LIVE_WALLPAPER_COMPONENT", componentName);
        }
        context.startActivity(intent);
    }

    /**
     * <pre>
     * 添加一个去往设置动态壁纸的按钮，主要是为了方便调试
     * </pre>
     */
    public static void addGoToLiveWallpaperBtn(final Context context, ViewGroup parentView, final String packageName, final String classFullName) {
        Button btn = new Button(context);
        btn.setText("Wallpaper");
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parentView.addView(btn, lp);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WallpaperHelper.startLiveWallpaperPrevivew(context, packageName, classFullName);
            }
        });
    }
}
