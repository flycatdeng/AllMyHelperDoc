package com.dandy.helper.android;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class ComponentNameHelper {
    /**
     * <pre>
     * 得到屏幕最顶层的Activity的ComponentName
     * </pre>
     * 
     * @param context
     * @return
     */
    public static ComponentName getTopActivityComponentName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos == null || runningTaskInfos.get(0) == null) {
            return null;
        }
        ComponentName componentName = runningTaskInfos.get(0).topActivity;
        return componentName;
    }
}
