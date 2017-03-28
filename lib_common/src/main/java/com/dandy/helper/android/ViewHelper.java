package com.dandy.helper.android;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class ViewHelper {

	/**
	 * get the View's position in window
	 * 
	 * @param view
	 * @return the Top-left location of this view.
	 */
	public static int[] getPositionInWindow(View view) {
		int[] location = new int[2];
		view.getLocationInWindow(location);
		return location;
	}

	public static View getActivityDecorView(LocalActivityManager manager, String id, Intent intent) {
		if (manager == null) {
			throw new RuntimeException("LocalActivityManager can not be null, you must init it");
		}
		return manager.startActivity(id, intent).getDecorView();
	}

	public static View getActivityDecorView(Context context, LocalActivityManager manager, Class<?> atyCls) {
		if (manager == null) {
			throw new RuntimeException("LocalActivityManager can not be null, you must init it");
		}
		Intent intent = new Intent(context, atyCls);
		return manager.startActivity(atyCls.getName(), intent).getDecorView();
	}
}
