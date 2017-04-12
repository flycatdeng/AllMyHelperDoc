package com.dandy.gles.engine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.FileHelper;
import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.ObjLoadAider;
import com.dandy.helper.gles.ShaderHelper;
import com.dandy.helper.java.nio.ArrayToBufferHelper;

import java.nio.FloatBuffer;

/**
 * <pre>
 *      3D模型的父类
 *      基本的shader属性为(扩展的需要自己添加，但是别更改以下信息)：
 *          1.顶点位置信息vec3 aPosition
 *          2.法线向量vec2 aNormal
 *          3.顶点纹理坐标vec2 aTexCoor
 *          4.总变换矩阵mat4 uMVPMatrix
 *          5.变换矩阵mat4 uMMatrix
 *          6.光源位置信息vec3 uLightLocation
 *          7.摄像机位置vec3 uCamera
 * </pre>
 * Created by flycatdeng on 2017/4/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Object3D extends Actor {
    private static final String TAG = "Object3D";
    private FloatBuffer mVertexBuffer;// 顶点坐标数据缓冲
    private FloatBuffer mNormalBuffer;// 顶点法向量数据缓冲
    private FloatBuffer mTexCoorBuffer;// 顶点纹理坐标数据缓冲
    private int muMVPMatrixHandle;// 总变换矩阵引用
    private int muMMatrixHandle;// 位置、旋转变换矩阵
    private int maPositionHandle; // 顶点位置属性引用
    private int maNormalHandle; // 顶点法向量属性引用
    private int maLightLocationHandle;// 光源位置属性引用
    private int maCameraHandle; // 摄像机位置属性引用
    private int maTexCoorHandle; // 顶点纹理坐标属性引用

    public Object3D(Context context) {
        super(context);
    }

    public void loadFromData(final float[] vertices, final float[] normals, final float texCoors[]) {
        mRunOnDraw.addToPending(new Runnable() {
            @Override
            public void run() {
                init(vertices, normals, texCoors);
            }
        });
    }

    public void loadFromObjLoadAider(ObjLoadAider helper) {
        loadFromData(helper.getVertexXYZ(), helper.getNormalVectorXYZ(), helper.getTextureVertexST());
    }

    public void loadFromAssets(final String filePath) {
        ObjLoadAider obj = new ObjLoadAider(mContext, FileHelper.getInputStreamFromAsset(mContext, filePath), new ObjLoadAider.OnLoadListener() {
            @Override
            public void onLoadOK(ObjLoadAider aider) {
                loadFromObjLoadAider(aider);
            }

            @Override
            public void onLoadFailed(String failedMsg) {
                LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg="+failedMsg);
            }
        });
    }

    public void setMaterialFromAssets(final String vertexFileName, final String fragmentFileName) {
        mRunOnDraw.addToPending(new Runnable() {
            @Override
            public void run() {
                // 初始化shader
                initShader(vertexFileName, fragmentFileName);
            }
        });
    }

    private void init(float[] vertices, float[] normals, float[] texCoors) {
        // 初始化顶点坐标与着色数据
        initVertexData(vertices, normals, texCoors);
    }

    // 初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        LogHelper.d(TAG, LogHelper.getThreadName() + "count vertices=" + vertices.length + " normals=" + normals.length + " texCoors=" + texCoors.length);
        vCount = vertices.length / 3;
        mVertexBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertices);
        mNormalBuffer = ArrayToBufferHelper.floatArrayToBuffer(normals);
        mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(texCoors);
    }

    // 初始化shader
    private void initShader(String vertexFileName, String fragmentFileName) {
        // 基于顶点着色器与片元着色器创建程序
        mProgramID = ShaderHelper.getProgramFromAsset(mContext, vertexFileName, fragmentFileName);
        // 获取程序中顶点位置属性引用
        maPositionHandle = GLES20.glGetAttribLocation(mProgramID, "aPosition");
        // 获取程序中顶点颜色属性引用
        maNormalHandle = GLES20.glGetAttribLocation(mProgramID, "aNormal");
        // 获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramID, "uMVPMatrix");
        // 获取位置、旋转变换矩阵引用
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgramID, "uMMatrix");
        // 获取程序中光源位置引用
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgramID, "uLightLocation");
        // 获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = GLES20.glGetAttribLocation(mProgramID, "aTexCoor");
        // 获取程序中摄像机位置引用
        maCameraHandle = GLES20.glGetUniformLocation(mProgramID, "uCamera");
        onInitExtraLocations();
    }

    /**
     * <pre>
     *  初始化Shader中基本的属性以外的属性
     *  基本的属性是指
     * </pre>
     */
    protected void onInitExtraLocations() {
    }

    public void drawSelf() {
        mRunOnDraw.runPendings();
        if (mProgramID == -1) {
            //TODO 使用默认路劲
            initShader("gles_engine_object3d/object3d_simple.vert", "gles_engine_object3d/object3d_simple.frag");
        }
        // 制定使用某套着色器程序
        GLES20.glUseProgram(mProgramID);

        // 将最终变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMatrixAider.getFinalMatrix(), 0);
        // 将位置、旋转变换矩阵传入着色器程序
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, mMatrixAider.getMMatrix(), 0);
        // 将光源位置传入着色器程序
        GLES20.glUniform3fv(maLightLocationHandle, 1, mMatrixAider.getLightPosFloatBuffer());
        // 将摄像机位置传入着色器程序
        GLES20.glUniform3fv(maCameraHandle, 1, mMatrixAider.getCameraFloatBuffer());
        /**
         * <pre>
         * 为什么顶点坐标数据，纹理坐标数据需要使用glVertexAttribPointer和glEnableVertexAttribArray？
         * 而其他的不需要？是不是因为是attribute？你看上面的uniform属性就不需要
         * 是不是顶点着色器里的attribute属性都要这样去得到和使用？
         * </pre>
         */
        // 将顶点位置数据传入渲染管线
        GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
        // 将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);
        // 为画笔指定顶点纹理坐标数据
        GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
        // 启用顶点位置、法向量、纹理坐标数据
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        if (mTextureID != -1) {
            /**
             * <pre>
             * 这里也是非常奇怪的，这里绑定了，fragment shader里就会有一个uniform sampler2D sTexture;//纹理内容数据
             * 为什么不需要像上面那样得到其对应的handle？
             * </pre>
             */
            // 绑定纹理
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
        } else {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }

        onDrawArraysPre();
        // 绘制加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vCount);
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }

    /**
     * <pre>
     *
     * </pre>
     *
     * @param width
     * @param height
     */
    public void onSurfaceChanged(int width, int height) {
        // 计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        // 调用此方法计算产生透视投影矩阵
        mMatrixAider.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
//        MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
        // 调用此方法产生摄像机9参数位置矩阵
        mMatrixAider.setCamera(true, 0f, 0.f, 50.0f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 0.0f);
    }

    public void onSurfaceCreated() {
        mMatrixAider.setInitStack();
        // 初始化光源位置
        // 绕Y轴、Z轴旋转
//        mMatrixAider.translate(-1f, 2f, -40f); // ch.obj 14.5f
//        mMatrixAider.rotate(270f, 0, 1, 0);
//        mMatrixAider.translate(-0.5f, 0f, -18f); // ch.obj 14.5f
//        mMatrixAider.rotate( 320f, 0, 1, 0);
//        mMatrixAider.rotate( 180f, 0, 1, 0);
//        mMatrixAider.rotate( -90f, 0, 1, 1);

        mMatrixAider.translate(-1, 2f, -40f); // ch.obj 14.5f
        mMatrixAider.rotate( -90, 0, 1, 0);
        mMatrixAider.setLightLocation(40, -10, 20);
    }
}
