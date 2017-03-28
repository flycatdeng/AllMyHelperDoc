package com.dandy.helper.android;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class SoftInputHelper {

    /**
     * 隐藏软件盘
     * 
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
