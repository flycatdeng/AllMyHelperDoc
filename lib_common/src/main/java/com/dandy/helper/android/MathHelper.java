package com.dandy.helper.android;

import android.graphics.RectF;

public class MathHelper {

    /**
     * <pre>
     * 判断一点是否在某个矩形坐标范围内
     * </pre>
     * 
     * @param inValue
     * @param scopeRect
     * @return
     */
    public static boolean isPointIn(float[] inValue, RectF scopeRect) {
        if (inValue == null || scopeRect == null) {
            return false;
        }
        if (inValue[1] < scopeRect.top) {
            return false;
        }
        if (inValue[1] > scopeRect.bottom) {
            return false;
        }
        if (inValue[0] < scopeRect.left) {
            return false;
        }
        if (inValue[0] > scopeRect.right) {
            return false;
        }
        return true;
    }
}
