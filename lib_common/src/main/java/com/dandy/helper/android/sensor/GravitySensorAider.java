package com.dandy.helper.android.sensor;

import com.dandy.helper.android.LogHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * <pre>
 * 重力感应器
 * 1.通过new关键字来得到该对象
 * 2.onResume的时候注册感应监听，onPause的时候注销监听
 * 3.通过DataCallback来回调处理过的值
 * </pre>
 *
 * @author dengchukun 2016年12月9日
 */
@SuppressLint("NewApi")
public class GravitySensorAider implements SensorEventListener {
    private static final String TAG = GravitySensorAider.class.getSimpleName();
    /**
     * <pre>
     * 数据回调，当改变的数据值差达到阈值才回调，不要使用我师父说的在onSensorChanged中乘以200什么的以便灵敏，其实可以直接将结果写给DATA_CALLBACK_CHANGED_THRESHOLD
     * 否则你乘以一个很大的数再去比较，本身就带有很多的计算量啊，每次onSensorChanged都多了几次相乘的计算量啊
     * </pre>
     */
    private static float DATA_CALLBACK_CHANGED_THRESHOLD = 0.001f;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    // Flare variables
    private float mOldX = 0.0f;
    private float mOldY = 0.0f;
    private float mPreX = -1f, mPreY = -1f;
    private float mDataCallbackChangedThreshold = DATA_CALLBACK_CHANGED_THRESHOLD;

    public GravitySensorAider(Context context) {
        init(context);
    }

    public GravitySensorAider(Context context, DataCallback callback) {
        init(context);
        mDataCallback = callback;
    }

    private void init(Context context) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    public void onResume() {
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        mPreX = -1f;
        mPreY = -1f;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * <pre>
     * 对得到的xy值进行优化，否则你想想，这些值每一个都可以精确到小数点后8/9位，而且值一直在变，
     * 这样就会导致如果你需要按照它的值来做动画的时候它给你的感觉就是一直在晃动
     * </pre>
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        float centerX = 0.0f;
        float centerY = 0.15f;

        float maximillian = 1.0f;
        float tmpX = map(event.values[0], -8f, 8.f, -0, maximillian);
        float tmpY = map(event.values[1], -8f, 8.f, maximillian, 0);

        mOldX += mOldX - tmpX;
        mOldY += mOldY - tmpY;
        mOldX = tmpX;
        mOldY = tmpY;
        if (mOldX > 1.0 - centerX)
            mOldX = 1.0f - centerX;

        if (mOldY > 1.0 - centerY)
            mOldY = 1.0f - centerY;

        if (mOldX < 0.0 + centerX)
            mOldX = 0.0f + centerX;

        if (mOldY < 0.0 + centerY)
            mOldY = 0.0f + centerY;

        // setting the center better
        tmpY += 0.0;
        if (Math.abs(mPreX - mOldX) < mDataCallbackChangedThreshold && Math.abs(mPreY - mOldY) < mDataCallbackChangedThreshold) {
            return;
        }
        if (mDataCallback != null) {
            mDataCallback.onDataCallback(mOldX, mOldY);
        }
        mPreX = mOldX;
        mPreY = mOldY;
//        float sensor_x = event.values[0] / 8.0f;
//        if (sensor_x > 2.f)
//            sensor_x = 2.f;
//        else if (sensor_x < -2.f)
//            sensor_x = -2.f;
//        float sensor_y = event.values[1] / 10.0f - event.values[2] / 8.0f;
//        if (sensor_y > 2.f)
//            sensor_y = 2.f;
//        else if (sensor_y < -2.f)
//            sensor_y = -2.f;
        // setAngle(sensor_x,sensor_y);
    }

    private float map(float value, float istart, float istop, float ostart, float ostop) {
        return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
    }

    public void onDestroy() {
        clearCallbacks();
    }

    public void clearCallbacks() {
        mDataCallback = null;
    }

    /**
     * 用于将优化之后的x y坐标回调
     *
     * @author dengchukun 2016年12月9日
     */
    public interface DataCallback {
        void onDataCallback(float x, float y);
    }

    private DataCallback mDataCallback;

    public void setDataCallback(DataCallback callback) {
        mDataCallback = callback;
    }

    public void setDataCallbackChangedThreshold(float thresholdValue) {
        mDataCallbackChangedThreshold = thresholdValue;
    }
}
