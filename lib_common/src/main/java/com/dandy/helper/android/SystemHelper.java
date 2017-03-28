package com.dandy.helper.android;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.View;

/**
 * 系统帮助类
 * 
 * @author flycat
 * 
 */
@SuppressWarnings("deprecation")
public class SystemHelper {

    /**
     * 将一段字符串拷贝到系统的剪切板中
     * 
     * @param str
     *            ：要拷贝的字符串
     */
    public static void copyStringToClipboard(Context context, String str) {
        // 获取剪贴板对象
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(str);// 设置剪贴板内容为需要拷贝的字符串
    }

    /**
     * 退出程序的方法
     */
    public static void exit() {
//		android.os.Process.killProcess(android.os.Process.myPid());
//		System.exit(0);
        ExitApplication.getInstance().exit();
    }

    /**
     * 隐藏状态栏与否
     * 
     * @param hide
     */
    protected void hideStatusBar(Activity activity, boolean hide) {
        if (!hide)
            return;
        View rootView = activity.getWindow().getDecorView();
        try {
            Method m = View.class.getMethod("setSystemUiVisibility", int.class);
            m.invoke(rootView, 0x0);
            m.invoke(rootView, 0x1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
