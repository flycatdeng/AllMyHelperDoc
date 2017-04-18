package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.IMVPMatrixOperation;
import com.dandy.helper.gles.MVPMatrixAider;
import com.dandy.helper.gles.Material;
import com.dandy.helper.gles.TextureHelper;
import com.dandy.helper.gles.Vec3;
import com.dandy.helper.java.PendingThreadAider;

public abstract class Actor implements IMVPMatrixOperation {
    private static final String TAG = "Actor";
    protected Context mContext;
    /**
     * GLSurfaceView的宽高
     */
    protected int mSurfaceWidth, mSurfaceHeight;
    protected PendingThreadAider mRunOnceOnDraw = new PendingThreadAider();
    protected PendingThreadAider mRunOnceBeforeDraw = new PendingThreadAider();
    protected MVPMatrixAider mMatrixAider = new MVPMatrixAider();
    protected Material mMaterial;
    protected int mVertexCount = 0;
    protected int mProgramID = -1;// 自定义渲染管线着色器程序id
    protected int mTextureID = -1;
    protected String mDefaultMaterialName = "gles_engine_material/default_simple.mat";
    private Actor mParentActor;
    private boolean mIsMaterialSetFromOutside = false;

    public Actor(Context context) {
        mContext = context;
    }

    public Actor getParentActor() {
        return mParentActor;
    }

    public void setParent(Actor parent) {
        mParentActor = parent;
    }

    protected void onDestroy() {
        GLES20.glDeleteProgram(mProgramID);
    }

    /**
     * 设置纹理，回收Bitmap对象
     *
     * @param bitmap
     */
    public void setTexture(Bitmap bitmap) {
        setTexture(bitmap, true);
    }

    /**
     * 设置纹理
     *
     * @param bitmap     纹理Bitmap
     * @param recycleBmp 是否回收该bitmap
     */
    public void setTexture(final Bitmap bitmap, final boolean recycleBmp) {
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                if (mTextureID == -1) {
                    mTextureID = TextureHelper.initTextureID(bitmap, recycleBmp);
                } else {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    TextureHelper.changeTextureImage(bitmap);
                }
            }
        });
    }

    /**
     * 设置纹理
     */
    public void initTexture(int textureId) {
        mTextureID = textureId;
    }

    boolean mIsSurfaceCreated = false;

    public void onSurfaceCreated() {
        mIsSurfaceCreated = true;
        mMatrixAider.setInitStack();
    }

    public void onDrawFrame() {
        if (mProgramID == -1 && !mIsMaterialSetFromOutside) {
            setMaterialFromAssets(mDefaultMaterialName);
        }
        mRunOnceBeforeDraw.runPendings();
        if (mProgramID == -1) {//如果没有设置材质，则使用默认的材质,双重保证
            setMaterialFromAssets(mDefaultMaterialName);
        }
        if (mProgramID == -1) {
            throw new RuntimeException("mProgramID == -1");
        }
        GLES20.glUseProgram(mProgramID);
        mRunOnceOnDraw.runPendings();
        onDraw();
    }

    /**
     * 绘制，此时的mProgramID已经得到了，而且已经调用了GLES20.glUseProgram(mProgramID);
     * 子类需要重写这个方法去实现自己的绘制
     */
    protected void onDraw() {
    }

    public void setMaterialFromAssets(final String materialFile) {
        mIsMaterialSetFromOutside = true;
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setMaterialFromAssets materialFile=" + materialFile);
                mMaterial = new Material(mContext, materialFile);
                mProgramID = mMaterial.getProgramID();
            }
        });
    }

    protected int getMaterialHandler(String propertyName) {
        if (mMaterial != null) {
            return mMaterial.getHandlerByPropertyName(propertyName);
        }
        return -1;
    }

    /**
     * 界面变更尺寸，需要重新设置投影矩阵和相机位置
     *
     * @param width
     * @param height
     */
    public void onSurfaceChanged(final int width, final int height) {
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "onSurfaceChanged");
                mSurfaceWidth = width;
                mSurfaceHeight = height;
            }
        });

//        // 计算GLSurfaceView的宽高比
//        float ratio = (float) width / height;
//        // 调用此方法计算产生透视投影矩阵
//        setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
////        MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
//        // 调用此方法产生摄像机9参数位置矩阵
//        setCamera(0f, 0.f, 50.0f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 0.0f);
    }

    protected void runOnceBeforeDraw(final Runnable runnable) {
        mRunOnceBeforeDraw.addToPending(runnable);
    }

    public void runOnceOnDraw(Runnable runnable) {
        mRunOnceOnDraw.addToPending(runnable);
    }

    //*************************************************IMVPMatrixOperation 实现*****************************************************************************************************************************************
    @Override
    public void setTranslate(float x, float y, float z) {
        mMatrixAider.translate(x, y, z);
    }

    @Override
    public void setTranslate(Vec3 offset) {
        mMatrixAider.translate(offset.x, offset.y, offset.z);
    }

    @Override
    public void setRotation(float angle, float x, float y, float z) {
        mMatrixAider.rotate(angle, x, y, z);
    }

    @Override
    public void setScale(float x, float y, float z) {
        mMatrixAider.scale(x, y, z);
    }

    @Override
    public void setScale(Vec3 scale) {
        mMatrixAider.scale(scale.x, scale.y, scale.z);
    }

    @Override
    public void setCamera(float cx, float cy, float cz, float tx, float ty, float tz, float upx, float upy, float upz) {
        mMatrixAider.setCamera(cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    @Override
    public void setProjectFrustum(float left, float right, float bottom, float top, float near, float far) {
        mMatrixAider.setProjectFrustum(left, right, bottom, top, near, far);
    }

    @Override
    public void setProjectOrtho(float left, float right, float bottom, float top, float near, float far) {
        mMatrixAider.setProjectOrtho(left, right, bottom, top, near, far);
    }
}
