package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.IActorMatrixOperation;
import com.dandy.helper.gles.IGLActor;
import com.dandy.helper.gles.MVPMatrixAider;
import com.dandy.helper.gles.Material;
import com.dandy.helper.gles.TextureHelper;
import com.dandy.helper.gles.Vec3;
import com.dandy.helper.java.PendingThreadAider;

public class Actor implements IGLActor, IActorMatrixOperation {
    private static final String TAG = "Actor";
    protected Context mContext;
    /**
     * GLSurfaceView的宽高
     */
    protected int mSurfaceWidth, mSurfaceHeight;
    protected PendingThreadAider mRunOnceOnDraw = new PendingThreadAider();
    protected PendingThreadAider mRunOnceBeforeDraw = new PendingThreadAider();
    private MVPMatrixAider mMatrixAider = new MVPMatrixAider();
    protected Material mMaterial;
    protected int mVertexCount = 0;
    protected int mProgramID = -1;// 自定义渲染管线着色器程序id
    protected int mTextureID = -1;
    protected String mDefaultMaterialName = "gles_engine_material/default_simple.mat";
    private Actor mParentActor;
    private boolean mIsMaterialSetFromOutside = false;
    private boolean mIsSurfaceCreated = false;
    private RequestRenderListener mRequestRenderListener;

    public Actor(Context context) {
        mContext = context;
        mMatrixAider.setInitStack();//一定要在构造器初始化的时候就初始化矩阵帮助类，否则可能在其他地方调用setTranslate等方法时如果没有初始化，那就无效了
    }

    public Actor getParentActor() {
        return mParentActor;
    }

    public void setParent(Actor parent) {
        mParentActor = parent;
    }

    public void onDestroy() {
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
                mMaterial = new Material(mContext, materialFile);
                mProgramID = mMaterial.getProgramID();
                LogHelper.d(TAG, "setMaterialFromAssets materialFile=" + materialFile + " mProgramID=" + mProgramID);
            }
        });
    }

    protected int getMaterialHandler(String propertyName) {
        if (mMaterial != null) {
            Integer result = mMaterial.getHandlerByPropertyName(propertyName);
            return result == null ? -1 : result;
        }
        return -1;
    }

    public void runOnceBeforeDraw(final Runnable runnable) {
        mRunOnceBeforeDraw.addToPending(runnable);
    }

    public void runOnceOnDraw(Runnable runnable) {
        mRunOnceOnDraw.addToPending(runnable);
    }

    //*************************************************IGLActor 实现*****************************************************************************************************************************************
    @Override
    public void onSurfaceCreated() {
        mIsSurfaceCreated = true;
    }

    /**
     * 界面变更尺寸，需要重新设置投影矩阵和相机位置
     *
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(final int width, final int height) {
        LogHelper.d(TAG, "onSurfaceChanged width=" + width + " height=" + height);
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }

    /**
     * <pre>
     *      如果不是从外面设置的材质则优先加载默认材质，否则的话就是从外面设置的材质，如果出现问题，接下来还有一次设置默认材质的方式来保底
     * </pre>
     */
    @Override
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
     * <pre>
     *     onSurfaceCreated是否已经被调用了
     * </pre>
     *
     * @return
     */
    @Override
    public boolean isSurfaceCreated() {
        return mIsSurfaceCreated;
    }

    @Override
    public void requestRender() {
        if (mRequestRenderListener != null) {
            mRequestRenderListener.onRequestRenderCalled();
        }
    }

    @Override
    public void setRequestRenderListener(RequestRenderListener listener) {
        mRequestRenderListener = listener;
    }

    @Override
    public RequestRenderListener getRequestRenderListener() {
        return mRequestRenderListener;
    }

    //*************************************************IActorMatrixOperation 实现*****************************************************************************************************************************************
   private Vec3 mPreLocation=new Vec3();
    @Override
    public void translate( float x,float y, float z) {
        mMatrixAider.translate(x, y, z);
        mPreLocation=mPreLocation.add(x,y,z);
    }

    @Override
    public void translate(Vec3 offset) {
        mMatrixAider.translate(offset.x, offset.y, offset.z);
        mPreLocation = mPreLocation.add(offset);
    }

    @Override
    public void setTranslation(float x, float y, float z) {
        mMatrixAider.translate(x - mPreLocation.x, y - mPreLocation.y, z - mPreLocation.z);
        mPreLocation = new Vec3(x, y, z);
    }

    @Override
    public void rotate(float angle, float x, float y, float z) {
        mMatrixAider.rotate(angle, x, y, z);
    }

    @Override
    public void scale(float x, float y, float z) {
        mMatrixAider.scale(x, y, z);
    }

    @Override
    public void scale(Vec3 scale) {
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

    @Override
    public float[] getMVPMatrix() {
        return mMatrixAider.getMVPMatrix();
    }

    @Override
    public float[] getModelMatrix() {
        return mMatrixAider.getModelMatrix();
    }

    @Override
    public float[] getViewMatrix() {
        return mMatrixAider.getViewMatrix();
    }

    @Override
    public float[] getModelViewMatrix() {
        return mMatrixAider.getModelViewMatrix();
    }

    @Override
    public float[] getProjectMatrix() {
        return mMatrixAider.getProjectMatrix();
    }
}
