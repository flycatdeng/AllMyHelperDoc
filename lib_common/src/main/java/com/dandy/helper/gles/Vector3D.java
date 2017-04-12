package com.dandy.helper.gles;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/12.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Vector3D {
    public float x;
    public float y;
    public float z;

    /**
     * 向量规格化
     *
     * @return
     */
    public Vector3D normalize() {
        Vector3D result = new Vector3D();
        // 求向量的模
        float module = (float) Math.sqrt(x * x + y * y + z * z);
        result.x = x / module;
        result.y = y / module;
        result.z = z / module;
        return result;
    }

    public Vector3D add(Vector3D vector3D) {
        Vector3D result = new Vector3D();
        result.x = x + vector3D.x;
        result.y = y + vector3D.y;
        result.z = z + vector3D.z;
        return result;
    }

    public float[] toFloat() {
        return new float[]{x, y, z};
    }
}
