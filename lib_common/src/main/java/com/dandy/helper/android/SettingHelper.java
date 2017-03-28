package com.dandy.helper.android;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * 设置页面帮助类
 * 
 * @author dengchukun
 * 
 */
public class SettingHelper {
    /**
     * 进入日期设置页面
     * 
     * @param context
     */
    public static void gotoDateSetAty(Context context) {
        String action = Settings.ACTION_DATE_SETTINGS;
        gotoSettingAty(context, action);
    }

    /**
     * 调转到系统设置的一些页面action
     * 
     * @param context
     *            :上下文
     * @param action
     *            :通过action进入对应的系统页面
     */
    private static void gotoSettingAty(Context context, String action) {
        context.startActivity(new Intent(action));
    }
}
