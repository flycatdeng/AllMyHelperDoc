package com.dandy.helper.gles;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/17.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public interface IActorMatrixOperation {
    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    public void setTranslate(float x, float y, float z);

    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    public void setTranslate(Vec3 offset);

    /**
     * <pre>
     * 设置绕xyz轴转动
     * </pre>
     */
    public void setRotation(float angle, float x, float y, float z);

    /**
     * 缩放
     *
     * @param x
     * @param y
     * @param z
     */
    public void setScale(float x, float y, float z);

    /**
     * 缩放
     */
    public void setScale(Vec3 scale);

    /**
     * 设置摄像机
     *
     * @param cx  摄像机位置x
     * @param cy  摄像机位置y
     * @param cz  摄像机位置z
     * @param tx  摄像机目标点x
     * @param ty  摄像机目标点y
     * @param tz  摄像机目标点z
     * @param upx 摄像机UP向量X分量
     * @param upy 摄像机UP向量Y分量
     * @param upz 摄像机UP向量Z分量
     */
    public void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz);

    /**
     * 设置透视投影参数
     *
     * @param left   near面的left
     * @param right  near面的right
     * @param bottom near面的bottom
     * @param top    near面的top
     * @param near   near面距离to view point
     * @param far    far面距离to view point
     */
    public void setProjectFrustum(float left, float right, float bottom, float top, float near, float far);

    /**
     * 设置正交投影参数
     *
     * @param left   near面的left
     * @param right  near面的right
     * @param bottom near面的bottom
     * @param top    near面的top
     * @param near   near面距离
     * @param far    far面距离
     */
    public void setProjectOrtho(float left, float right, float bottom, float top, float near, float far);

    public float[] getMVPMatrix();

    public float[] getModelMatrix();

    public float[] getViewMatrix();

    public float[] getModelViewMatrix();

    public float[] getProjectMatrix();
}
