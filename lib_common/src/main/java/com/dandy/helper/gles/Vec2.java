package com.dandy.helper.gles;

import com.dandy.helper.java.basedata.FloatHelper;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/14.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vec2 {
    public float x;
    public float y;

    public Vec2() {

    }

    public Vec2(float sameValue) {
        this(sameValue, sameValue);
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 向量规格化
     *
     * @return
     */
    public Vec2 normalize() {
        return floatToVec(FloatHelper.normalize(this.vecToFloat()));
    }

    public Vec2 add(Vec2 vec2) {
        Vec2 result = new Vec2();
        result.x = x + vec2.x;
        result.y = y + vec2.y;
        return result;
    }

    public float[] vecToFloat() {
        return new float[]{x, y};
    }

    public static float[] vecToFloat(Vec2 vec) {
        return new float[]{vec.x, vec.y};
    }

    public static Vec2 floatToVec(float[] floats) {
        if (floats == null) {
            throw new RuntimeException("floats array list can not be null");
        }
        if (floats.length == 1) {
            return new Vec2(floats[0]);
        } else if (floats.length == 2) {
            return new Vec2(floats[0], floats[1]);
        } else {
            throw new RuntimeException("floats array list length must be 1 or 2");
        }
    }
}
