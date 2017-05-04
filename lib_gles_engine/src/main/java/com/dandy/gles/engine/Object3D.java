package com.dandy.gles.engine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.Obj3DLoadAider;
import com.dandy.helper.gles.Vec3;
import com.dandy.helper.java.nio.ArrayToBufferHelper;

import java.io.InputStream;
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
    private int muMVPMatrixHandle = -1;// 总变换矩阵引用
    private int muModelMatrixHandle = -1;// 位置、旋转变换矩阵
    private int muViewMatrixHandle = -1;
    private int muModelViewMatrixHandle = -1;
    private int maPositionHandle = -1; // 顶点位置属性引用
    private int maNormalHandle = -1;// 顶点法向量属性引用
    private int maTexCoorHandle = -1; // 顶点纹理坐标属性引用
    private int muTextureHandle = -1;
    private boolean isDataOK = false;
    private int vboId[] = new int[3];

    public Object3D(Context context) {
        super(context);
        mDefaultMaterialName = "gles_engine_object3d/default_simple_object3d.mat";
    }

    public void loadFromData(final float[] vertices, final float[] normals, final float texCoors[]) {
        init(vertices, normals, texCoors);
    }

    public void loadFromObjLoadResult(Obj3DLoadAider.LoadResult loadResult) {
        loadFromData(loadResult.getVertexXYZ(), loadResult.getNormalVectorXYZ(), loadResult.getTextureVertexST());
    }

    public void loadFromInputStream(InputStream objIns) {
        Obj3DLoadAider obj = new Obj3DLoadAider();
        obj.loadFromInputStream(objIns, onLoadListener);
    }

    public void loadFromAssets(final String filePath) {
        Obj3DLoadAider obj = new Obj3DLoadAider();
        obj.loadFromAsset(mContext, filePath, onLoadListener);
    }

    Obj3DLoadAider.OnLoadListener onLoadListener = new Obj3DLoadAider.OnLoadListener() {
        @Override
        public void onLoadOK(Obj3DLoadAider.LoadResult loadResult) {
            loadFromObjLoadResult(loadResult);
            LogHelper.d(TAG, LogHelper.getThreadName() + " loadResult=" + loadResult.toString());
        }

        @Override
        public void onLoadFailed(String failedMsg) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " failedMsg=" + failedMsg);
        }
    };

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
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                GLES20.glGenBuffers(3, vboId, 0);//申请一个缓冲区
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[0]);//绑定缓冲区
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mVertexBuffer.capacity() * 4, mVertexBuffer, GLES20.GL_STATIC_DRAW //GL_STATIC_DRAW ：数据不会或几乎不会改变。
                        //GL_DYNAMIC_DRAW：数据会被改变很多。
                        //GL_STREAM_DRAW ：数据每次绘制时都会改变。
                );//绑定数据，放runOnDraw中，因为这个时候mVertexBuffer已经有数据了
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[1]);//绑定缓冲区
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mNormalBuffer.capacity() * 4, mNormalBuffer, GLES20.GL_STATIC_DRAW);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[2]);//绑定缓冲区
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mTexCoorBuffer.capacity() * 4, mTexCoorBuffer, GLES20.GL_STATIC_DRAW);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);//现在不使用这个缓冲区
            }
        });
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        // 调用此方法计算产生透视投影矩阵
        setProjectFrustum(-ratio, ratio, -1, 1, 2, 1111);
        // 调用此方法产生摄像机9参数位置矩阵
        setCamera(0f,0f, 50.0f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 0.0f);
    }

    @Override
    protected void onShaderLocationInit() {
        muMVPMatrixHandle = getMVPMatrixHandle();
        muModelMatrixHandle = getModelMatrixHandle();
        muViewMatrixHandle = getViewMatrixHandle();
        muModelViewMatrixHandle = getModelViewMatrixHandle();
        maPositionHandle = getPositionHandle();
        maNormalHandle = getNormalHandle();
        maTexCoorHandle = getTexCoorHandle();
        muTextureHandle = getTextureHandle();
    }

    protected int getMVPMatrixHandle() {
        return getMaterialHandler("uMVPMatrix");
    }

    protected int getModelMatrixHandle() {
        return getMaterialHandler("uModelMatrix");
    }

    protected int getViewMatrixHandle() {
        return getMaterialHandler("uViewMatrix");
    }

    protected int getModelViewMatrixHandle() {
        return getMaterialHandler("uModelViewMatrix");
    }

    protected int getPositionHandle() {
        return getMaterialHandler("aPosition");
    }

    protected int getNormalHandle() {
        return getMaterialHandler("aNormal");
    }

    protected int getTexCoorHandle() {
        return getMaterialHandler("aTexCoor");
    }

    protected int getTextureHandle() {
        return getMaterialHandler("uTexture");
    }

    @Override
    public void onDrawFrame() {
        if (!isDataOK) {//如果数据都还没好就onDrawFrame，就会调用到onSurfaceCreated，此时mVertexBuffer还是null
            return;
        }
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        super.onDrawFrame();
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        // 将最终变换矩阵传入着色器程序
        if (muMVPMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, getMVPMatrix(), 0);
        }
        if (muModelMatrixHandle != -1) {
            // 将位置、旋转变换矩阵传入着色器程序
            GLES20.glUniformMatrix4fv(muModelMatrixHandle, 1, false, getModelMatrix(), 0);
        }
        if (muViewMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muViewMatrixHandle, 1, false, getViewMatrix(), 0);
        }
        if (muModelViewMatrixHandle != -1) {
            GLES20.glUniformMatrix4fv(muModelViewMatrixHandle, 1, false, getModelViewMatrix(), 0);
        }
        if (maPositionHandle != -1) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[0]);//绑定存在的VBO
            GLES20.glVertexAttribPointer(maPositionHandle, //要配置的顶点属性句柄
                    3,//指定顶点属性的大小。顶点属性是一个vec3，它由3个值组成，所以大小是3
                    GLES20.GL_FLOAT, false, 0, //步长(Stride)，它告诉我们在连续的顶点属性组之间的间隔。由于下个组位置数据在3个GLfloat之后，我们把步长设置为3 * sizeof(GLfloat)。要注意的是由于我们知道这个数组是紧密排列的（在两个顶点属性之间没有空隙）我们也可以设置为0来让OpenGL决定具体步长是多少（只有当数值是紧密排列时才可用）。一旦我们有更多的顶点属性，我们就必须更小心地定义每个顶点属性之间的间隔
                    0//它表示位置数据在缓冲中起始位置的偏移量(Offset)。由于位置数据在数组的开头，所以这里是0
            );//顶点XYZ,三个点，使用GPU中的缓冲数据，不再从RAM中取数据，所以后面的2个参数都是0
            GLES20.glEnableVertexAttribArray(maPositionHandle);//启用顶点属性；顶点属性默认是禁用的
        }
        if (maNormalHandle != -1) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[1]);//绑定存在的VBO
            GLES20.glVertexAttribPointer(maNormalHandle, 3, GLES20.GL_FLOAT, false, 0, 0);//顶点XYZ,三个点，使用GPU中的缓冲数据，不再从RAM中取数据，所以后面的2个参数都是0
            GLES20.glEnableVertexAttribArray(maNormalHandle);//开启顶点
        }
        if (maTexCoorHandle != -1) {
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vboId[2]);//绑定存在的VBO
            GLES20.glVertexAttribPointer(maTexCoorHandle, 2, GLES20.GL_FLOAT, false, 0, 0);//顶点XYZ,三个点，使用GPU中的缓冲数据，不再从RAM中取数据，所以后面的2个参数都是0
            GLES20.glEnableVertexAttribArray(maTexCoorHandle);//开启顶点
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);//数据已经得到 就可以不再使用这个绑定了

        onDrawArraysPre();
        // 绘制加载的物体
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,//希望绘制的是一个三角形
                0, //顶点数组的起始索引
                mVertexCount//绘制多少个顶点
        );
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
        if (muTextureHandle != -1 && mTextureID != -1) {//之所以放这里是因为可能子类需要绑定不同的纹理ID，
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(muTextureHandle, 0);
        }
    }

    protected void onDrawArraysAfter() {
    }
}
