package com.dandy.helper.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络相关类
 * 
 * @author dandy
 * 
 */
public class NetworkHelper {
    /**
     * 显示是否有网弹出框 如果有网络连结则不弹出，无网络则弹出
     * 
     * @param context
     *            :上下文
     */
    public static boolean showConnectState(Context context) {
        System.out.println("NetConnectService context" + context);
        if (!isConnect(context)) {// 没网络连结
            AlertDialog.Builder builders = new AlertDialog.Builder(context);
            builders.setTitle("网络错误");
            builders.setMessage("网络连接失败，请确认网络连接");
            builders.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            builders.show();
            return false;
        }
        return true;
    }

    /**
     * 判断网络是否连结
     * 
     * @param context
     *            ：上下文，看你是在哪个activity里显示
     * @return连结则返回true，否则返回false
     */
    public static boolean isConnect(Context context) {
        if (context == null) {// 这个null是专门为逻辑类加载数据准备的，不用担心，因为在这之前都是先加载页面类的
            return true;
        } else {
            // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    // 判断当前网络是否已经连接
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }
}
