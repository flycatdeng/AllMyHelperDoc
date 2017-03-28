package com.dandy.helper.android;

import android.os.Handler;
import android.os.Looper;

public class ThreadHelper {

    public void postOnMainLooper(Runnable runnable) {
        Handler h = new Handler(Looper.getMainLooper());
        h.post(runnable);
    }
}
