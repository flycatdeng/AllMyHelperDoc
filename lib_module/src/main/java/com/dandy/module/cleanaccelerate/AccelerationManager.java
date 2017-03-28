package com.dandy.module.cleanaccelerate;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.dandy.helper.android.AppHelper;
import com.dandy.helper.android.LogHelper;

/**
 * <pre>
 * 清理加速模块
 * 需要一开始初始化{@link #init(Context)}
 * 在需要清理内存的地方调用{@link #start(Context, MemoryActionListener)}
 * 
 * </pre>
 * 
 * @author flycatdeng
 * 
 */
public class AccelerationManager {
    enum CleanState {
        NONE, CLEANING
    }

    protected static final String TAG = "AccelerationManager";

    private ActivityManager mActivityManager = null;
//    private ArrayList<MemoryActionListener> mOnActionListener;
    private CleanState mCleanState = CleanState.NONE;
    private static AccelerationManager mAccelerationManager;

    private AccelerationManager() {
    }

    public static AccelerationManager getInstance() {
        if (mAccelerationManager == null) {
            mAccelerationManager = new AccelerationManager();
        }
        return mAccelerationManager;
    }

    public void init(Context context) {
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        mOnActionListener = new ArrayList<MemoryActionListener>();
    }

    public void start(final Context context, final MemoryActionListener listener) {
        if (mCleanState.equals(CleanState.CLEANING)) {
            listener.onCanClean(false, "清理进行中...");
            return;
        } else {
            listener.onCanClean(true, "");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                mCleanState = CleanState.CLEANING;
                List<String> mlist = scanMemory(context);
                cleanMemery(listener, mlist, startTime);
                mCleanState = CleanState.NONE;
            }
        }).start();
    }

    /**
     * <pre>
     * 搜索要清理的进程名或包名
     * </pre>
     * 
     * @param context
     * @return
     */
    private List<String> scanMemory(Context context) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        List<RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
        if (appProcessList != null && appProcessList.size() > 2) {
            List<String> packages = getNeedKilledProcess(context, appProcessList);
            LogHelper.d(TAG, LogHelper.getThreadName() + " getNeedKilledProcess size=" + packages.size());
            return packages;
        } else {
            List<String> packages = AppHelper.getNotSystemApps(context);
            List<String> needKilledPackages = getNeedKilledPackage(context, packages);
            LogHelper.d(TAG, LogHelper.getThreadName() + " getNeedKilledPackage size=" + needKilledPackages.size());
            return needKilledPackages;
        }

    }

    /**
     * <pre>
     * 得到要清理的应用进程名（除白名单的）
     * </pre>
     * 
     * @param context
     * @param packages
     * @return
     */
    private List<String> getNeedKilledProcess(Context context, List<RunningAppProcessInfo> appProcessList) {
        List<String> needKilledList = new ArrayList<String>();
        List<String> whiteLis = AccelerWhiteListUtil.getWhitePackageList(context, true);
        for (RunningAppProcessInfo processInfo : appProcessList) {
            String processName = processInfo.processName;
            boolean isWhite = false;
            for (String whitePackageName : whiteLis) {
                if (processName.contains(whitePackageName)) {// 进程名可能比包名多一些字符，所以要判断当前进程名的包名是否为白名单中的
                    isWhite = true;
                }
            }
            if (!isWhite) {
                needKilledList.add(processName);
            }
        }
        return needKilledList;
    }

    /**
     * <pre>
     * 得到要清理的应用包名（除白名单的）
     * </pre>
     * 
     * @param context
     * @param packages
     * @return
     */
    private List<String> getNeedKilledPackage(Context context, List<String> packages) {
        List<String> needKilledList = new ArrayList<String>();
        List<String> whiteLis = AccelerWhiteListUtil.getWhitePackageList(context, true);
        for (String packageName : packages) {
            boolean isWhite = false;
            for (String whitePackageName : whiteLis) {
                if (packageName.contains(whitePackageName)) {// 进程名可能比包名多一些字符，所以要判断当前进程名的包名是否为白名单中的
                    isWhite = true;
                }
            }
            if (!isWhite) {
                needKilledList.add(packageName);
            }
        }
        return needKilledList;
    }

    private void cleanMemery(MemoryActionListener listener, List<String> mlist, long startTime) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        long beforeMemory = 0;
        long endMemory = 0;
        int mAppCount = 0;

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        beforeMemory = memoryInfo.availMem;
        int size = mlist.size();
        for (int i = 0; i < size; i++) {
            String processName = mlist.get(i);
            AppHelper.killAppProcesses(mActivityManager, processName);
//            for (MemoryActionListener actionListener : mOnActionListener) {
//                actionListener.onCleanProgressUpdated(++mAppCount, size);
//            }
            if (listener != null) {
                listener.onCleanProgressUpdated(++mAppCount, size, processName);
            }
        }
        mActivityManager.getMemoryInfo(memoryInfo);
        endMemory = memoryInfo.availMem;
        final long cacheSize = beforeMemory - endMemory;
        long timeDur = System.currentTimeMillis() - startTime;
//        for (MemoryActionListener actionListener : mOnActionListener) {
//            actionListener.onCleanCompleted(cacheSize, beforeMemory, timeDur);
//        }
        if (listener != null) {
            listener.onCleanCompleted(cacheSize, beforeMemory, timeDur);
        }
    }

//    public void setOnActionListener(MemoryActionListener listener) {
//        mOnActionListener.add(listener);
//    }
}
