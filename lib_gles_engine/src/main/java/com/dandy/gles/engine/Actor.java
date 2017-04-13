package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.gles.MatrixAider;
import com.dandy.helper.gles.TextureHelper;

public class Actor extends Base {
    protected Context mContext;
    protected int vCount = 0;
    protected int mProgramID = -1;// 自定义渲染管线着色器程序id
    protected int mTextureID = -1;
    protected MatrixAider mMatrixAider = new MatrixAider();
    private Actor mParentActor;

    public Actor(Context context) {
        mContext = context;
    }

    public Actor getParentActor() {
        return mParentActor;
    }

    public void setParent(Actor parent) {
        mParentActor = parent;
    }

    public final void destroy() {
        GLES20.glDeleteProgram(mProgramID);
        onDestroy();
    }

    protected void onDestroy() {
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
        mRunOnDraw.addToPending(new Runnable() {
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
    }

    protected int mSurfaceWidth;
    protected int mSurfaceHeight;

    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }
}
