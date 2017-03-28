package com.dandy.helper.android;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.dandy.helper.java.ObjectHelper;

public class AppHelper {
    /**
     * 判断是否为系统应用
     * 
     * @param info
     * @return
     */
    public static boolean isSystemApp(ApplicationInfo info) {
        return (info.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
    }

    /**
     * 重启应用
     * 
     * @param context
     */
    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid()); // 结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    /**
     * 判断某个服务是否正在运行的方法
     * 
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 判断一个应用程序是否正在运行
     * 
     * @param context
     * @param packageName
     *            :应用程序的包名
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo rapi : infos) {
            if (rapi.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static Drawable getApplicationIcon(Context context, String packageName) {
        Drawable icon = null;
        PackageManager pm = context.getPackageManager();
        try {
            icon = pm.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return icon;
    }

    public static Drawable getActivityIcon(Context context, Intent intent) {
        Drawable icon = null;
        PackageManager pm = context.getPackageManager();
        try {
            icon = pm.getActivityIcon(intent);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return icon;
    }

    /**
     * to judge whether the class name in the ComponentName is the same with the class name of the application we can get by its package name
     * 
     * @param context
     *            the context of current application
     * @param componentName
     *            a ComponentName instance that maybe contains a package name and a class name
     * @return
     */
    public static boolean isComponentClassNameEqualsLauncherClassName(Context context, ComponentName componentName) {
        if (ObjectHelper.isNull(componentName)) {
            return false;
        }
        String componentPackageName = componentName.getPackageName();
        String componentClassName = componentName.getClassName();
        if (ObjectHelper.isNull(componentPackageName)) {
            return false;
        }
        String launcherActivityName = getLauncherActivityNameByPackageName(context, componentPackageName);
        if (ObjectHelper.isNull(launcherActivityName)) {
            return false;
        }
        if (componentClassName.equals(launcherActivityName)) {
            return true;
        }
        return false;
    }

    /**
     * get the launcher activity class full name of an application by the package name
     * 
     * @param context
     *            the context of current application
     * @param packageName
     *            the package name of the application (it can be any application)
     * @return
     */
    public static String getLauncherActivityNameByPackageName(Context context, String packageName) {
        String className = null;
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);// android.intent.action.MAIN
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);// android.intent.category.LAUNCHER
        resolveIntent.setPackage(packageName);
        List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            className = resolveinfo.activityInfo.name;
        }
        return className;
    }

    /**
     * 退出程序的方法
     */
    public static void exit() {
//      android.os.Process.killProcess(android.os.Process.myPid());
//      System.exit(0);
        ExitApplication.getInstance().exit();
    }

    /**
     * <pre>
     * 杀死某个进程
     * </pre>
     * 
     * @param activityManager
     * @param processName
     *            进程名，或包名
     */
    public static void killAppProcesses(ActivityManager activityManager, String processName) {
        String packageName = null;
        try {
            if (processName.indexOf(":") == -1) {
                packageName = processName;
            } else {
                packageName = processName.split(":")[0];
            }
            activityManager.killBackgroundProcesses(packageName);
            Method forceStopPackage = activityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);
            forceStopPackage.setAccessible(true);
            forceStopPackage.invoke(activityManager, packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 获取系统应用包名列表
     * </pre>
     * 
     * @param context
     * @return
     */
    public static List<String> getSystemApps(Context context) {
        List<String> packages = new ArrayList<String>();
        List<PackageInfo> installPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installPackages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
//                packages.add(packageInfo.packageName);//not system app
            } else {// system app
                packages.add(packageInfo.packageName);
            }
        }
        return packages;
    }

    /**
     * <pre>
     * 获取非系统应用包名列表
     * </pre>
     * 
     * @param context
     * @return
     */
    public static List<String> getNotSystemApps(Context context) {
        List<String> packages = new ArrayList<String>();
        List<PackageInfo> installPackages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : installPackages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                packages.add(packageInfo.packageName);// not system app
            } else {// system app
//                packages.add(packageInfo.packageName);
            }
        }
        return packages;
    }
}
