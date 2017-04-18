package com.dandy.module.gles.simple;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.dandy.helper.gles.ShaderHelper;
import com.dandy.helper.gles.TextureHelper;
import com.dandy.helper.java.PendingThreadAider;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class SimpleTexture {
    private Context mContext;
    private static final float POSITION[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    private static final float TEXTURE[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    private FloatBuffer mPositionBuffer;
    private FloatBuffer mTexCoorBuffer;
    private int aPositionHandler;
    private int mProgramId;
    private int mTextureID = -1;
    private int uResolutionHandler, uTextureRatioHandler;
    private int uSamplerTextureHandler;
    /**
     * GLSurfaceView的宽高
     */
    private int mSurfaceWidth, mSurfaceHeight;
    private PendingThreadAider mPendingThreadAider = new PendingThreadAider();

    public SimpleTexture(Context context) {
        mContext = context;
        mPositionBuffer = ByteBuffer.allocateDirect(POSITION.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPositionBuffer.put(POSITION).position(0);
        mTexCoorBuffer = ByteBuffer.allocateDirect(TEXTURE.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoorBuffer.put(TEXTURE).position(0);
    }

    public void init() {
        mProgramId = ShaderHelper.getProgramFromAsset(mContext, "module_gles_simple/simple_texture.vert", "module_gles_simple/simple_texture.frag");
        aPositionHandler = GLES20.glGetAttribLocation(mProgramId, "position");
        uResolutionHandler = GLES20.glGetUniformLocation(mProgramId, "u_resolution");
        uTextureRatioHandler = GLES20.glGetUniformLocation(mProgramId, "u_textureRatio");
        // samplers
        uSamplerTextureHandler = GLES20.glGetUniformLocation(mProgramId, "u_texture");
    }

    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        initAppendSize();
    }

    private boolean mIsInitAppendSizeOK = false;

    private void initAppendSize() {
        if (mIsInitAppendSizeOK) {
            return;
        }
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        mIsInitAppendSizeOK = true;
    }

    public void drawSelf() {
        mPendingThreadAider.runPendings();
        GLES20.glUseProgram(mProgramId);
        float resolutionData[] = {(float) mSurfaceWidth, (float) mSurfaceHeight};
        float textureRatioData = ((float) mSurfaceWidth) / mSurfaceHeight;

        GLES20.glVertexAttribPointer(aPositionHandler, 2, GLES20.GL_FLOAT, false, 0, mPositionBuffer);
        GLES20.glEnableVertexAttribArray(aPositionHandler);


        if (mTextureID != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureID);
            GLES20.glUniform1i(uSamplerTextureHandler, 0);
        }
        GLES20.glUniform2f(uResolutionHandler, resolutionData[0], resolutionData[1]);
        GLES20.glUniform1f(uTextureRatioHandler, textureRatioData);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(aPositionHandler);
    }

    public void setTexture(Bitmap bitmap) {
        setTexture(bitmap, true);
    }

    public void setTexture(final Bitmap bitmap, final boolean recycleBmp) {
        mPendingThreadAider.addToPending(new Runnable() {
            @Override
            public void run() {
                if (mTextureID == -1) {
                    mTextureID = TextureHelper.initTextureID(bitmap, recycleBmp);
                } else {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, bitmap);
                }
            }
        });

    }
}
