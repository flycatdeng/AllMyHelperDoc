package com.dandy.helper.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.dandy.helper.java.ObjectHelper;

public class PackageHelper {

    private static final String TAG = "PackageHelper";

    // 根据包名获取应用信息
    @SuppressLint("NewApi")
    public static void loadAppInfo(Context context, String pkgName) {
        PackageInfo pkgInfo = null;
        try {
            PackageManager pm = context.getPackageManager();
            pkgInfo = pm.getPackageInfo(pkgName, 0);
            if (pkgInfo != null && pkgInfo.applicationInfo != null) {
                ApplicationInfo appInfo = pkgInfo.applicationInfo;
                // 安装时间
                long time = pkgInfo.firstInstallTime;
                // 版本名
                String versionName = pkgInfo.versionName;
                // 版本号
                int versionCode = pkgInfo.versionCode;
                // 应用名
                pkgInfo.applicationInfo.loadLabel(pm);
                // 应用Icon
                pkgInfo.applicationInfo.loadIcon(pm);
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 
     * 检查应用程序是否安装并安装应用程序
     * 
     * @param context
     * @param packageName
     * @return
     */
    public boolean checkApkExist(Context context, String packageName) {
        if (ObjectHelper.isNull(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取未安装的APK信息
     * 
     * @param context
     * @param archiveFilePath
     *            APK文件的路径。如：/sdcard/download/XX.apk
     */
    public static PackageInfo getApkInfo(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_META_DATA);
        return apkInfo;
    }

    /**
     * 从一个apk文件去获取该文件的版本信息
     * 
     * @param context
     *            本应用程序上下文
     * @param archiveFilePath
     *            APK文件的路径。如：/sdcard/download/XX.apk
     * @return
     */
    public static String getVersionNameFromApk(Context context, String archiveFilePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo packInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 得到当前应用的包名(不是说某个类所在的包名,而是整个应用的包名)
     */
    public static String getCurrentAppPackageName(Context context) {
        String packageName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            packageName = packInfo.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 查看系统中某个包名是否存在
     * 
     * @param mContext调用的上下文
     * @param packageName要检测的包名
     * @return
     */
    public static boolean isPackageExistInOS(Context mContext, String packageName) {
        try {
            mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "there is no such package name-" + packageName);
            return false;
        }
    }

    /**
     * 得到当前应用版本名称的方法
     * 
     * @param context
     *            :上下文
     * @throws Exception
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 获取指定包名的版本号
     * 
     * @param context
     *            本应用程序上下文
     * @param packageName
     *            你想知道版本信息的应用程序的包名
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context, String packageName) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(packageName, 0);
        String version = packInfo.versionName;
        LogHelper.d(TAG, LogHelper.getThreadName() + "current version-" + version);
        return version;
    }

    /**
     * 返回当前程序版本号
     */
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * <pre>
     * 获得最顶层activity的应用包名
     * </pre>
     * 
     * @param context
     * @return
     */
    public static String getTopActivityPackageName(Context context) {
        return ComponentNameHelper.getTopActivityComponentName(context).getPackageName();
    }
}
