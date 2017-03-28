package com.dandy.gles.engine;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class Texture extends Base {
    private static final String TAG = "Texture";
    protected Context mContext;
    protected FloatBuffer mPositionBuffer;
    protected FloatBuffer mTexCoorBuffer;
    protected int aPositionHandler;
    protected int aTexcoorHandler;
    protected int mProgramId;
    protected int mTextureID = -1;
    protected int uSamplerTextureHandler;
    protected static final float POSITION[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    protected static final float TEXTURE[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    protected boolean mIsInitialized;
    protected int mVertexCount = 4;

    public Texture(Context context) {
        mContext = context;
        mPositionBuffer = ByteBuffer.allocateDirect(POSITION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPositionBuffer.put(POSITION).position(0);
        mTexCoorBuffer = ByteBuffer.allocateDirect(TEXTURE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoorBuffer.put(TEXTURE).position(0);
    }

    public void init() {
        onInit();
        mIsInitialized = true;
        onInitialized();
    }

    protected void onInit() {
        mProgramId = ShaderHelper.getProgramFromAsset(mContext, "gles_engine/simple.vert", "gles_engine/simple.frag");
        aPositionHandler = GLES20.glGetAttribLocation(mProgramId, "position");
        uSamplerTextureHandler = GLES20.glGetUniformLocation(mProgramId, "inputImageTexture");
        aTexcoorHandler = GLES20.glGetAttribLocation(mProgramId,
                "inputTextureCoordinate");
    }

    protected void onInitialized() {
    }

    public final void destroy() {
        mIsInitialized = false;
        GLES20.glDeleteProgram(mProgramId);
        onDestroy();
    }

    protected void onDestroy() {
    }

    public void onSurfaceChanged(int width, int height) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mSurfaceWidth = width;
        mSurfaceHeight = height;
    }

    public void drawSelf() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        GLES20.glUseProgram(mProgramId);
        mRunOnDraw.runPendings();

        GLES20.glVertexAttribPointer(aPositionHandler,//指定要修改的顶点属性的索引值,句柄
                2,//指定每个顶点属性的组件数量。必须为1、2、3或者4。初始值为4。（如position是由3个（x,y,z）组成，而颜色是4个（r,g,b,a））,这里我们没有用到Z轴
                GLES20.GL_FLOAT, //指定数组中每个组件的数据类型
                false, //指定当被访问时，固定点数据值是否应该被归一化（GL_TRUE）或者直接转换为固定点值（GL_FALSE）
                0, //stride 指定连续顶点属性之间的偏移量。如果为0，那么顶点属性会被理解为：它们是紧密排列在一起的。初始值为0
                mPositionBuffer//指定第一个组件在数组的第一个顶点属性中的偏移量。该数组与GL_ARRAY_BUFFER绑定，储存于缓冲区中。初始值为0
        );
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glVertexAttribPointer(aTexcoorHandler, 2, GLES20.GL_FLOAT, false, 0, mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(aTexcoorHandler);

        if (mTextureID != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(uSamplerTextureHandler, 0);
        }

        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
        GLES20.glDisableVertexAttribArray(aPositionHandler);
        GLES20.glDisableVertexAttribArray(aTexcoorHandler);
        onDrawArraysAfter();
    }

    protected void onDrawArraysPre() {
    }

    protected void onDrawArraysAfter() {
    }


}
