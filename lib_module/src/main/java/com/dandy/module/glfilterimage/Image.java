package com.dandy.module.glfilterimage;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.ShaderHelper;

public class Image {
    private static final String TAG = "Image";
    private Context mContext;
    protected boolean mIsInitialized;
    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    //textureCoordinate = inputTextureCoordinate.xy;\n" +
            "    textureCoordinate.x = inputTextureCoordinate.x;\n" +
            "    textureCoordinate.y = 1.0-inputTextureCoordinate.y;\n" +
            "}";
    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "precision highp float;\n" +
            " \n" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     vec4 color = texture2D(inputImageTexture, textureCoordinate);\n" +
            "     //gl_FragColor = vec4(tColor.r,tColor.g,tColor.b,tColor.a);\n" +
            "     //if(color.r>=0.5){\n" +
            "     //   gl_FragColor=vec4(0.0,0.5,0.0,color.a);\n" +
            "     //   // discard;\n" +
            "     //}else{\n" +
            "         gl_FragColor=color;\n" +
            "     //}\n" +
            "}";

    private final String mVertexShader;
    private final String mFragmentShader;
    public static final float POSITION[] = {
        -1.0f, -1.0f,
        1.0f, -1.0f,
        -1.0f, 1.0f,
        1.0f, 1.0f,
    };
    public static final float TEXTURE[] = {
        0.0f, 0.0f,
        1.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
    };
    private FloatBuffer mPositionBuffer;
    private FloatBuffer mTexCoorBuffer;
    private int mProgramId;
    private int aPositionHandler;
    private int aTexCoorHandler;
    private int uImageTextureHandler;
    /**
     * GLSurfaceView的宽高
     */
    protected int mSurfaceWidth, mSurfaceHeight;

    /**
     * 图像宽高
     */
    protected int mImageWidth, mImageHeight;

    public Image(Context context) {
        this(context,NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public Image(Context context, String vertexShader, String fragmentShader) {
        mContext = context;
        mVertexShader = vertexShader;
        mFragmentShader = fragmentShader;
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
        mProgramId = ShaderHelper.getProgram(mVertexShader, mFragmentShader);
        aPositionHandler = GLES20.glGetAttribLocation(mProgramId, "position");
        aTexCoorHandler = GLES20.glGetAttribLocation(mProgramId, "inputTextureCoordinate");
        uImageTextureHandler = GLES20.glGetUniformLocation(mProgramId, "inputImageTexture");
    }

    protected void onInitialized() {

    }

    public void onSurfaceChanged(int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        float ratioWidth = 1.0f;
        float ratioHeight = 1.0f;
        if (mImageWidth < mSurfaceWidth && mImageHeight < mSurfaceHeight) {
            ratioWidth = (float) mImageWidth / (float) mSurfaceWidth;
            ratioHeight = (float) mImageHeight / (float) mSurfaceHeight;
        } else {
            float ratio1 = (float) mImageWidth / mSurfaceWidth;
            float ratio2 = (float) mImageHeight / mSurfaceHeight;
            float ratioMax = Math.max(ratio1, ratio2);
            int imageWidthNew = Math.round(mImageWidth / ratioMax);
            int imageHeightNew = Math.round(mImageHeight / ratioMax);

            ratioWidth = imageWidthNew / (float) mSurfaceWidth;
            ratioHeight = imageHeightNew / (float) mSurfaceHeight;
        }
        LogHelper.d(TAG, LogHelper.getThreadName() + " ratioWidth=" + ratioWidth + " ratioHeight=" + ratioHeight);
        float[] position = new float[] {
                POSITION[0] * ratioWidth, POSITION[1] * ratioHeight,
                POSITION[2] * ratioWidth, POSITION[3] * ratioHeight,
                POSITION[4] * ratioWidth, POSITION[5] * ratioHeight,
                POSITION[6] * ratioWidth, POSITION[7] * ratioHeight,
        };
        mPositionBuffer.clear();
        mPositionBuffer.put(position).position(0);
    }

    public void onDrawSelf(final int textureId, final FloatBuffer positionBuffer, final FloatBuffer texCoorBuffer) {
        GLES20.glUseProgram(mProgramId);
        GLES20.glVertexAttribPointer(aPositionHandler, 2, GLES20.GL_FLOAT, false, 0, positionBuffer);
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glVertexAttribPointer(aTexCoorHandler, 2, GLES20.GL_FLOAT, false, 0, texCoorBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoorHandler);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uImageTextureHandler, 0);
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(aPositionHandler);
        GLES20.glDisableVertexAttribArray(aTexCoorHandler);
        onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
    public void onDrawSelf(int textureId) {
        GLES20.glUseProgram(mProgramId);
        GLES20.glVertexAttribPointer(aPositionHandler, 2, GLES20.GL_FLOAT, false, 0, mPositionBuffer);
        GLES20.glEnableVertexAttribArray(aPositionHandler);
        GLES20.glVertexAttribPointer(aTexCoorHandler, 2, GLES20.GL_FLOAT, false, 0, mTexCoorBuffer);
        GLES20.glEnableVertexAttribArray(aTexCoorHandler);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uImageTextureHandler, 0);
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(aPositionHandler);
        GLES20.glDisableVertexAttribArray(aTexCoorHandler);
        onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    private void onDrawArraysPre() {

    }

    private void onDrawArraysAfter() {

    }

    public void setImageWH(int width, int height) {
        mImageWidth = width;
        mImageHeight = height;
    }

}
