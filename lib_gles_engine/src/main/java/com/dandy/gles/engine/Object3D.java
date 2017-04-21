package com.dandy.gles.engine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.Obj3DLoadAider;
import com.dandy.helper.gles.Vec3;
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
    private int muMVPMatrixHandle= -1;// 总变换矩阵引用
    private int muModelMatrixHandle= -1;// 位置、旋转变换矩阵
    private int muViewMatrixHandle= -1;
    private int muModelViewMatrixHandle= -1;
    private int maPositionHandle= -1; // 顶点位置属性引用
    private int maNormalHandle= -1;// 顶点法向量属性引用
    private int maTexCoorHandle= -1; // 顶点纹理坐标属性引用
    private int muLightLocation= -1;
    private int muCameraLocation= -1;
    private boolean isDataOK = false;
    private int muTextureHandle = -1;

    public Object3D(Context context) {
        super(context);
        mDefaultMaterialName = "gles_engine_object3d/default_simple_object3d.mat.mat";
    }

    public void loadFromData(final float[] vertices, final float[] normals, final float texCoors[]) {
        init(vertices, normals, texCoors);
    }

    public void loadFromObjLoadResult(Obj3DLoadAider.LoadResult loadResult) {
        loadFromData(loadResult.getVertexXYZ(), loadResult.getNormalVectorXYZ(), loadResult.getTextureVertexST());
    }

    public void loadFromAssets(final String filePath) {
        Obj3DLoadAider obj = new Obj3DLoadAider();
        obj.loadFromAsset(mContext, filePath, new Obj3DLoadAider.OnLoadListener() {
            @Override
            public void onLoadOK(Obj3DLoadAider.LoadResult loadResult) {
//                loadFromData(aider);
                loadFromObjLoadResult(loadResult);
                LogHelper.d(TAG, LogHelper.getThreadName() + " loadResult=" + loadResult.toString());
            }

            @Override
            public void onLoadFailed(String failedMsg) {
                LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
            }
        });
    }

    private void init(float[] vertices, float[] normals, float[] texCoors) {
        // 初始化顶点坐标与着色数据
        initVertexData(vertices, normals, texCoors);
        isDataOK = true;
        requestRender();
    }

    // 初始化顶点坐标与着色数据的方法
    public void initVertexData(float[] vertices, float[] normals, float texCoors[]) {
        LogHelper.d(TAG, LogHelper.getThreadName() + "count vertices=" + vertices.length + " normals=" + normals.length + " texCoors=" + texCoors.length);
        mVertexCount = vertices.length / 3;
        mVertexBuffer = ArrayToBufferHelper.floatArrayToBuffer(vertices);
        mNormalBuffer = ArrayToBufferHelper.floatArrayToBuffer(normals);
        mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(texCoors);
    }

    @Override
    public void setMaterialFromAssets(String materialFile) {
        super.setMaterialFromAssets(materialFile);
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                initHandles();
            }
        });
    }

    @Override
    public void onDrawFrame() {
        if (!isDataOK) {
            return;
        }
        super.onDrawFrame();
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        //清除深度缓冲与颜色缓冲
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        LogHelper.d(TAG, LogHelper.getThreadName() + " maPositionHandle" + maPositionHandle
                + " maNormalHandle" + maNormalHandle + " maTexCoorHandle" + maTexCoorHandle
                + " muMVPMatrixHandle" + muMVPMatrixHandle + " muModelMatrixHandle" + muModelMatrixHandle
                + " muViewMatrixHandle" + muViewMatrixHandle + " muModelViewMatrixHandle" + muModelViewMatrixHandle
                + " muTextureHandle" + muTextureHandle + " muLightLocation" + muLightLocation
                + " muCameraLocation" + muCameraLocation);
        if(muMVPMatrixHandle!=-1){
            // 将最终变换矩阵传入着色器程序
            GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getMVPMatrix(), 0);
        }
        if(muModelMatrixHandle!=-1){
            // 将位置、旋转变换矩阵传入着色器程序
            GLES20.glUniformMatrix4fv(muModelMatrixHandle, 1, false, getModelMatrix(), 0);
        }
        if(muViewMatrixHandle!=-1){
            GLES20.glUniformMatrix4fv(muViewMatrixHandle, 1, false, getViewMatrix(), 0);
        }
        if(muModelViewMatrixHandle!=-1){
            GLES20.glUniformMatrix4fv(muModelViewMatrixHandle, 1, false, getModelViewMatrix(), 0);
        }
        if(maPositionHandle!=-1){
            /**
             * <pre>
             * 为什么顶点坐标数据，纹理坐标数据需要使用glVertexAttribPointer和glEnableVertexAttribArray？
             * 而其他的不需要？是不是因为是attribute？你看上面的uniform属性就不需要
             * 是不是顶点着色器里的attribute属性都要这样去得到和使用？
             * </pre>
             */
            // 将顶点位置数据传入渲染管线
            GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mVertexBuffer);
            // 启用顶点位置、法向量、纹理坐标数据
            GLES20.glEnableVertexAttribArray(maPositionHandle);
        }
        if(maNormalHandle!=-1){
            // 将顶点法向量数据传入渲染管线
            GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 3 * 4, mNormalBuffer);
            GLES20.glEnableVertexAttribArray(maNormalHandle);
        }
        if(maTexCoorHandle!=-1){
            // 为画笔指定顶点纹理坐标数据
            GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 2 * 4, mTexCoorBuffer);
            GLES20.glEnableVertexAttribArray(maTexCoorHandle);
        }
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
            GLES20.glUniform1f(muTextureHandle, 0);
        } else {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
        if (muLightLocation != -1) {
            GLES20.glVertexAttrib3f(muLightLocation, mLightLocation.x, mLightLocation.y, mLightLocation.z);
        }
        if (muCameraLocation != -1) {
            GLES20.glVertexAttrib3f(muCameraLocation, mCameraLocation.x, mCameraLocation.y, mCameraLocation.z);
        }
        onDrawArraysPre();
        // 绘制加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mVertexCount);
        onDrawArraysAfter();
    }

    protected void initHandles() {
        maPositionHandle = getMaterialHandler("aPosition");
        maNormalHandle = getMaterialHandler("aNormal");
        // 获取程序中顶点纹理坐标属性引用
        maTexCoorHandle = getMaterialHandler("aTexCoor");
        // 获取程序中总变换矩阵引用
        muMVPMatrixHandle = getMaterialHandler("uMVPMatrix");
//        // 获取位置、旋转变换矩阵引用
        muModelMatrixHandle = getMaterialHandler("uModelMatrix");
        muViewMatrixHandle = getMaterialHandler("uViewMatrix");
        muModelViewMatrixHandle = getMaterialHandler("uModelViewMatrix");
        muTextureHandle = getMaterialHandler("uTexture");
        muLightLocation = getMaterialHandler("uLightLocation");
        muCameraLocation = getMaterialHandler("uCameraLocation");
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        //打开深度检测,记得在draw的时候要GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT）；
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
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
        super.onSurfaceChanged(width, height);
//        translate(-2,2,-40f);
        GLES20.glViewport(0, 0, width, height);
        // 计算GLSurfaceView的宽高比
        float ratio = (float) width / height;
        // 调用此方法计算产生透视投影矩阵
        setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
//        MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
        // 调用此方法产生摄像机9参数位置矩阵
        setCamera(0f, 0.f, 30.0f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 0.0f);
        setLightLocation(40, -40, 40);
    }

    private Vec3 mCameraLocation = new Vec3();
    private Vec3 mLightLocation = new Vec3();

    @Override
    public void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        super.setCamera(cx, cy, cz, tx, ty, tz, upx, upy, upz);
        mCameraLocation.x = cx;
        mCameraLocation.y = cy;
        mCameraLocation.z = cz;
    }

    public void setLightLocation(float x, float y, float z) {
        mLightLocation.x = x;
        mLightLocation.y = y;
        mLightLocation.z = z;
    }

    public void setLightLocation(Vec3 vec) {
        mLightLocation = vec;
    }
}
