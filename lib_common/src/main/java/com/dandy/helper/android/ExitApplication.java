package com.dandy.helper.android;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 用于完全退出程序，关闭所有页面的类
 * 
 * @author flycatdeng 需要在每个activity的oncreate方法中加上ExitApplication.getInstance().addActivity(this)方法
 *         退出则调用ExitApplication.getInstance().exit();循环将activity给finish掉
 * 
 */
public class ExitApplication extends Application {
    private static final String TAG = ExitApplication.class.getSimpleName();
    private static ExitApplication instance;
    private List<Activity> activityList = new LinkedList<Activity>();
    private HashMap<String, Activity> mActivitys = new HashMap<String, Activity>();

    public ExitApplication() {
    }

    // 单例模式中获取唯一的ExitApplication 实例
    public static ExitApplication getInstance() {
        if (null == instance) {
            instance = new ExitApplication();
        }
        return instance;

    }

    // 添加Activity 到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
        mActivitys.put(activity.getClass().getName(), activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
        mActivitys.remove(activity.getClass().getName());
    }

    public Activity getActivity(String atyName) {
        return mActivitys.get(atyName);
    }

    // 遍历所有Activity 并finish

    public void exit() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }
}