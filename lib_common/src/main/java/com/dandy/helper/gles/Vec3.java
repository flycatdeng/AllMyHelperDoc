package com.dandy.helper.gles;

import com.dandy.helper.java.basedata.FloatHelper;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/12.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3() {

    }

    public Vec3(float sameValue) {
        this(sameValue, sameValue, sameValue);
    }

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 向量规格化
     *
     * @return
     */
    public Vec3 normalize() {
        return floatToVec(FloatHelper.normalize(this.vecToFloat()));
    }

    public Vec3 add(Vec3 vec3) {
        Vec3 result = new Vec3();
        result.x = x + vec3.x;
        result.y = y + vec3.y;
        result.z = z + vec3.z;
        return result;
    }

    public float[] vecToFloat() {
        return new float[]{x, y, z};
    }

    public static float[] vecToFloat(Vec3 vec3) {
        return new float[]{vec3.x, vec3.y, vec3.z};
    }

    public static Vec3 floatToVec(float[] floats) {
        if (floats == null) {
            throw new RuntimeException("floats array list can not be null");
        }
        if (floats.length == 1) {
            return new Vec3(floats[0]);
        } else if (floats.length == 3) {
            return new Vec3(floats[0], floats[1], floats[2]);
        } else {
            throw new RuntimeException("floats array list length must be 1 or 3");
        }
    }
}