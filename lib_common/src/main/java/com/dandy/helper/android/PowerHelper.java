package com.dandy.helper.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 电量帮助类，此类的调用有一定的顺序,而且需要创建对象来调用方法
 * <p>
 * 1.调用PowerHelper.init(Context context)注册广播
 * <p>
 * 2.此时才能调用 PowerHelper.getXXX方法得到需要的值
 * <p>
 * 3.在移除时钟的时候要注销该广播PowerHelper.unregister(Context context)
 * 
 * @author dengchukun
 * 
 */
public class PowerHelper {

    private static final String TAG = "PowerHelper";

    private int mPowerLevel;
    private int mPowerScale;
    private int mPowerStatus;
    private int mPowerHealth;
    private OnChangedListener mOnChangedListener;
    private int mPrePowerStatus = -2;

    /**
     * 注册广播
     * 
     * @param context
     */
    public void onInit(Context context) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(mBatteryReceiver, intentFilter);
    }

    /**
     * 注销广播
     * 
     * @param context
     */
    public void onDestroy(Context context) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mBatteryReceiver != null) {
            context.unregisterReceiver(mBatteryReceiver);
            mBatteryReceiver = null;
        }
        mOnChangedListener = null;
    }

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPowerLevel = intent.getIntExtra("level", -1);
            mPowerScale = intent.getIntExtra("scale", -1);
            mPowerStatus = intent.getIntExtra("status", -1);
            mPowerHealth = intent.getIntExtra("health", -1);
            LogHelper.d(TAG, "level-" + mPowerLevel + " scale-" + mPowerScale + " status-" + mPowerStatus + " health-" + mPowerHealth);
            mOnChangedListener.onChanged();
            if (mPowerStatus != mPrePowerStatus) {
                mOnChangedListener.onStatusChanged();
            }
            mPrePowerStatus = mPowerStatus;
        }
    };

    /**
     * 是否正在充电
     * 
     * @return
     */
    public boolean isCharging() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        return mPowerStatus == BatteryManager.BATTERY_STATUS_CHARGING;
    }

    /**
     * 是否充满了
     * 
     * @return
     */
    public boolean isChargedFull() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        return mPowerStatus == BatteryManager.BATTERY_STATUS_FULL;
    }

    /**
     * 是否没有正在充电
     * 
     * @return
     */
    public boolean isNotCharging() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        return mPowerStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING;
    }

    /**
     * 得到手机电量值
     * 
     * @return
     */
    public int getPowerLevel() {
        return mPowerLevel;
    }

    public void setOnChangedListener(OnChangedListener onChangedListener) {
        mOnChangedListener = onChangedListener;
    }

    /**
     * 得到手机规模总值
     * 
     * @return
     */
    public int getPowerScale() {
        return mPowerScale;
    }

    /**
     * 得到手机状态级别
     * 
     * @return
     */
    public int getPowerStatus() {
        return mPowerStatus;
    }

    /**
     * 得到手机健康程度
     * 
     * @return
     */
    public int getPowerHealth() {
        return mPowerHealth;
    }

    /**
     * 对电量等因子的监听
     * 
     * @author dengchukun
     * 
     */
    public interface OnChangedListener {
        /**
         * 任意一个电量因子变换，都会调用该方法
         */
        public void onChanged();

        /**
         * 当电池状态改变的时候调用，例如充电
         */
        public void onStatusChanged();
    }

    /**
     * 对电量等因子的监听，调用的时候重写需要的方法即可
     * 
     * @author dengchukun
     * 
     */
    public class OnChangedListenerAdapter implements OnChangedListener {
        private String SUB_TAG = TAG + ".OnChangedListenerAdapter";

        @Override
        public void onChanged() {
            LogHelper.d(SUB_TAG, LogHelper.getThreadName());
        }

        @Override
        public void onStatusChanged() {
            LogHelper.d(SUB_TAG, LogHelper.getThreadName() + " mPrePowerStatus-" + mPrePowerStatus + " mPowerStatus-" + mPowerStatus);
        }

    }
}
