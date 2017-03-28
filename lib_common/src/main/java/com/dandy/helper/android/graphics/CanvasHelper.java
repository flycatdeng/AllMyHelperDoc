package com.dandy.helper.android.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

public class CanvasHelper {
    public static void setTransparent(Canvas canvas) {
        canvas.save();
        // canvas.drawColor(Color.TRANSPARENT);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
        canvas.restore();
    }
}
