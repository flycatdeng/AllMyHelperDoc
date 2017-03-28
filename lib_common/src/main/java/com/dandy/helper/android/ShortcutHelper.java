package com.dandy.helper.android;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class ShortcutHelper {

    /**
     * 判断某个应用有不有桌面快捷方式
     * 
     * @param context
     *            上下文
     * @param appNameRID
     *            在string配置文件中的appname
     * @return
     */
    public static boolean hasShortCut(Context context, int appNameRID) {
        String url = "";
        if (android.os.Build.VERSION.SDK_INT < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?", new String[] { context.getString(appNameRID)}, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    /**
     * <pre>
     * create a shortcut, 创建一个快捷方式
     * this method need ，需要权限
     * uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"
     * </pre>
     * 
     * @param context
     * @param name
     *            快捷方式的名称
     * @param iconResID
     *            快捷方式的图片
     * @param launchIntent
     *            启动相关的intent
     */
    public static void addShortcut(Context context, String name, int iconResID, Intent launchIntent) {
        Intent addShortcutIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        // 不允许重复创建
        addShortcutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 名字
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // 图标
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, iconResID));
        // 设置关联程序
        if (launchIntent != null) {
            addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        }
        // 发送广播
        context.sendBroadcast(addShortcutIntent);
    }

}
