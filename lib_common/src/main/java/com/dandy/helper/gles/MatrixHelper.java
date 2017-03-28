package com.dandy.helper.gles;

import android.annotation.SuppressLint;
import android.opengl.Matrix;

@SuppressLint("NewApi")
public class MatrixHelper {// 存储系统矩阵状态的类
    private static float[] mProjMatrix = new float[16];// 4x4矩阵 投影用
    private static float[] mVMatrix = new float[16];// 摄像机位置朝向9参数矩阵
    private static float[] mMVPMatrix;// 最后起作用的总变换矩阵
    static float[] mMMatrix = new float[16];// 具体物体的移动旋转矩阵

    public static void setInitStack()// 获取不变换初始矩阵
    {
        Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    }

    public static void transtate(float x, float y, float z)// 设置沿xyz轴移动
    {
        Matrix.translateM(mMMatrix, 0, x, y, z);
    }

    public static void rotate(float angle, float x, float y, float z)// 设置绕xyz轴转动
    {
        Matrix.rotateM(mMMatrix, 0, angle, x, y, z);
    }

    /**
     * 设置摄像机
     * 
     * @param cx
     *            摄像机位置x
     * @param cy
     *            摄像机位置y
     * @param cz
     *            摄像机位置z
     * @param tx
     *            摄像机目标点x
     * @param ty
     *            摄像机目标点y
     * @param tz
     *            摄像机目标点z
     * @param upx
     *            摄像机UP向量X分量
     * @param upy
     *            摄像机UP向量Y分量
     * @param upz
     *            摄像机UP向量Z分量
     */
    public static void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    /**
     * 设置透视投影参数
     * 
     * @param left
     *            near面的left
     * @param right
     *            near面的right
     * @param bottom
     *            near面的bottom
     * @param top
     *            near面的top
     * @param near
     *            near面距离to view point
     * @param far
     *            far面距离to view point
     */
    public static void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 设置正交投影参数
     * 
     * @param left
     *            near面的left
     * @param right
     *            near面的right
     * @param bottom
     *            near面的bottom
     * @param top
     *            near面的top
     * @param near
     *            near面距离
     * @param far
     *            far面距离
     */
    public static void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * 获取具体物体的总变换矩阵
     * 
     * @param spec
     *            变换矩阵
     * @return
     */
    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    public static float[] getFinalMatrix() {
        return getFinalMatrix(mMMatrix);
    }
}
