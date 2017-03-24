package com.dandy.helper.android;

import android.content.Context;
import android.os.Vibrator;

/**
 * need <uses-permission android:name="android.permission.VIBRATE" />
 * 
 * @author dandy
 * 
 */
public class VibratorAider {
	private static final String TAG = VibratorAider.class.getSimpleName();
	private Vibrator mVibrator;
	private int mRepeatType = -1;// 0表示一直循环，-1表示不循环
	private long[] mVibratePattern = new long[] { 500, 200, 500, 200 };// 第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时间

	public VibratorAider(Context context) {
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public void startVibrator() {
		LogHelper.d(TAG, LogHelper.getThreadName());
		mVibrator.vibrate(mVibratePattern, mRepeatType);
	}

	public void stopVibrator() {
		mVibrator.cancel();
	}

	public void vibrate(long[] pattern, int repeat) {
		mVibrator.vibrate(pattern, repeat);
	}

	public void setRepeat(boolean repeat) {
		if (repeat) {
			setRepeatType(0);
		} else {
			setRepeatType(-1);
		}
	}

	/**
	 * 0表示一直循环，-1表示不循环
	 * 
	 * @return
	 */
	public int getRepeatType() {
		return mRepeatType;
	}

	private void setRepeatType(int repeatType) {
		this.mRepeatType = repeatType;
	}

	public long[] getVibratePattern() {
		return mVibratePattern;
	}

	public void setVibratePattern(long[] vibratePattern) {
		this.mVibratePattern = vibratePattern;
	}

}
