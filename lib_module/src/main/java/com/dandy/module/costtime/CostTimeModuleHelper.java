package com.dandy.module.costtime;

import android.util.Log;

import com.dandy.module.IModuleInterface;

/**
 * <pre>
 *      用来打印某个过程花费的时间的帮助类
 *      1.记录短过程时间，在过程执行之前先调用{@link #clearPreLastTime()}重置mLastTime，
 *      过程结束后调用{@link #recordDeltaTime(String)}即可打印出你这个过程使用了多长时间。
 *      2.记录长过程或者多个过程的。过程执行之前先调用{@link #startRecord()}开始记录mStartTime，
 *      在某个过程后面{@link #recordTimeFromStart(String)}即可打印出你这个过程从记录点开始到结束后花费了多长时间
 * </pre>
 */
public class CostTimeModuleHelper implements IModuleInterface {
    private static String TAG = "CostTimeModuleHelper";
    private static CostTimeModuleHelper sCostTimeModuleHelper;
    private long mStartTime = 0L;
    private long mLastTime = 0L;
    private boolean mIsStarted = false;

    private CostTimeModuleHelper() {
    }

    public synchronized static CostTimeModuleHelper getInstance() {
        if (sCostTimeModuleHelper == null) {
            sCostTimeModuleHelper = new CostTimeModuleHelper();
        }
        return sCostTimeModuleHelper;
    }

    public void startRecord() {
        Log.d(TAG, "startRecord mIsStarted=" + mIsStarted);
        if (mIsStarted) {
            return;
        }
        mStartTime = System.currentTimeMillis();
        mLastTime = mStartTime;
        mIsStarted = true;
    }

    public void clearPreLastTime() {
        mLastTime = System.currentTimeMillis();
    }

    public void recordDeltaTime(String from) {
        long curTime = System.currentTimeMillis();
        Log.d(TAG, from + " " + (curTime - mLastTime) + " ms");
        mLastTime = curTime;
    }

    public void recordTimeFromStart(String from) {
        long curTime = System.currentTimeMillis();
        Log.d(TAG, from + " " + (curTime - mStartTime) + " ms");
    }

    @Override
    public boolean isModuleTurnedOn() {
        return true;
    }

}
