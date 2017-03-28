package com.dandy.helper.java;

import android.graphics.Bitmap;

public class ImageHelper {

    public static void recycleBitmap(Bitmap bitmap) {
        if (ObjectHelper.isNotNull(bitmap) && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }
}
