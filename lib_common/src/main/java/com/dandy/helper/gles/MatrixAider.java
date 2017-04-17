package com.dandy.helper.gles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.SuppressLint;
import android.opengl.Matrix;

/**
 * @author dengchukun 2016年11月28日
 * @deprecated
 */
@SuppressLint("NewApi")
public class MatrixAider {
    private static final String TAG = MatrixAider.class.getSimpleName();
    private float[] mProjMatrix = new float[16];// 4x4矩阵 投影用
    private float[] mVMatrix = new float[16];// 摄像机位置朝向9参数矩阵
    private float[] mMMatrix = new float[16];// 具体物体的移动旋转矩阵，旋转、平移
    private float[] mMVPMatrix;// 最后起作用的总变换矩阵
    private FloatBuffer mCameraFB = null;
    private FloatBuffer mLightPositionFB = null;
    private float[] mLightLocation = new float[]{0, 0, 0};// 定位光光源位置

    public float[] getFinalMatrix() {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }

    /**
     * <pre>
     * 获取不变换初始矩阵
     * </pre>
     */
    public void setInitStack() {
        Matrix.setRotateM(mMMatrix, 0, 0, 1, 0, 0);
    }

    /**
     * <pre>
     * 设置沿xyz轴移动
     * </pre>
     */
    public void translate(float x, float y, float z) {
        Matrix.translateM(mMMatrix, 0, x, y, z);
    }

    /**
     * <pre>
     * 设置绕xyz轴转动
     * </pre>
     */
    public void rotate(float angle, float x, float y, float z) {
        Matrix.rotateM(mMMatrix, 0, angle, x, y, z);
    }

    /**
     * 缩放
     *
     * @param x
     * @param y
     * @param z
     */
    public void scale(float x, float y, float z)//设置沿xyz轴缩放
    {
        Matrix.scaleM(mMMatrix, 0, x, y, z);
    }

    /**
     * 设置摄像机
     *
     * @param needFBToShader 是否需要将相机数据传给shader
     * @param cx             摄像机位置x
     * @param cy             摄像机位置y
     * @param cz             摄像机位置z
     * @param tx             摄像机目标点x
     * @param ty             摄像机目标点y
     * @param tz             摄像机目标点z
     * @param upx            摄像机UP向量X分量
     * @param upy            摄像机UP向量Y分量
     * @param upz            摄像机UP向量Z分量
     */
    public void setCamera(boolean needFBToShader, float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
        if (!needFBToShader) {
            return;
        }

        float[] cameraLocation = new float[3];// 摄像机位置
        cameraLocation[0] = cx;
        cameraLocation[1] = cy;
        cameraLocation[2] = cz;

        ByteBuffer llbb = ByteBuffer.allocateDirect(3 * 4);
        llbb.order(ByteOrder.nativeOrder());// 设置字节顺序
        mCameraFB = llbb.asFloatBuffer();
        mCameraFB.put(cameraLocation);
        mCameraFB.position(0);
    }

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
    public void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

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
    public void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    /**
     * <pre>
     * 设置灯光位置的方法
     * </pre>
     */
    public void setLightLocation(float x, float y, float z) {
        mLightLocation[0] = x;
        mLightLocation[1] = y;
        mLightLocation[2] = z;
        if (mLightPositionFB == null) {
            ByteBuffer llbb = ByteBuffer.allocateDirect(mLightLocation.length * 4);
            llbb.order(ByteOrder.nativeOrder());// 设置字节顺序
            mLightPositionFB = llbb.asFloatBuffer();
        }
    }

    public FloatBuffer getLightPosFloatBuffer() {
        if (mLightPositionFB == null) {
            throw new RuntimeException(TAG + " getLightPosFloatBuffer mLightPositionFB is null");
        }
        mLightPositionFB.clear();
        mLightPositionFB.put(mLightLocation);
        mLightPositionFB.position(0);
        return mLightPositionFB;
    }

    public FloatBuffer getCameraFloatBuffer() {
        if (mCameraFB == null) {
            throw new RuntimeException(TAG + " getCameraFloatBuffer mCameraFB is null");
        }
        return mCameraFB;
    }

    public float[] getMMatrix() {
        return mMMatrix;
    }
}
