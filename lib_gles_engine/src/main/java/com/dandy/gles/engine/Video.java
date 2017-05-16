package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.view.Surface;

import com.dandy.helper.android.media.SurfaceVideoPlayerAider;
import com.dandy.helper.gles.TextureHelper;
import com.dandy.helper.java.nio.ArrayToBufferHelper;

import java.io.File;
import java.nio.FloatBuffer;

/**
 * <pre>
 *      视频Actor,用法类似Image对象
 *      该Actor的纹理ID，其绑定的对象是GLES11Ext.GL_TEXTURE_EXTERNAL_OES而不再是之前的GLES20.GL_TEXTURE_2D，因为视频流是YUV格式，而opengl使用的是RGB格式，使用这种纹理可以自动转换YUV为RGB
 *
 * </pre>
 * Created by flycatdeng on 2017/5/15.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class Video extends Actor implements SurfaceTexture.OnFrameAvailableListener {
    private static final String TAG = "Video";
    //    private MediaPlayer mMediaPlayer = null;
    protected static final float POSITION[] = {
            -1.0f, -1.0f, 0f,
            1.0f, -1.0f, 0f,
            -1.0f, 1.0f, 0f,
            1.0f, 1.0f, 0f,
    };
    protected static final float TEXTURE[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    protected FloatBuffer mPositionBuffer;
    protected FloatBuffer mTexCoorBuffer;
    private int muSTMatrixHandler = -1;
    private int maPositionHandler = -1;
    private int maTextureCoordHandler = -1;
    private int msTextureHandler = -1;
    private SurfaceTexture mSurface;
    private boolean updateSurface = false;
    private float[] mSTMatrix = new float[16];
    private SurfaceVideoPlayerAider mSurfaceVideoPlayerAider;

    private Video(Context context) {
        super(context);
        mSurfaceVideoPlayerAider = new SurfaceVideoPlayerAider(context);
        init();
    }

    /**
     * 从asserts获取视频资源
     *
     * @param context
     * @param filePath
     * @return
     */
    public static Video createFromAssert(Context context, String filePath) {
        Video video = new Video(context);
        video.mSurfaceVideoPlayerAider.setResFromAsserts(filePath);
        return video;
    }

    /**
     * 使用视频文件对象来创建Video对象
     *
     * @param context
     * @param file
     * @return
     */
    public static Video createFromFile(Context context, File file) {
        Video video = new Video(context);
        video.mSurfaceVideoPlayerAider.setResFromFile(file);
        return video;
    }

    private void init() {
        mVertexCount = 4;
        mDefaultTextureOptions = null;
        mPositionBuffer = ArrayToBufferHelper.floatArrayToBuffer(POSITION);
        mTexCoorBuffer = ArrayToBufferHelper.floatArrayToBuffer(TEXTURE);
        mDefaultMaterialName = "gles_engine_video/video.mat";
    }

    @Override
    public void onResume() {
        super.onResume();
        mSurfaceVideoPlayerAider.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mSurfaceVideoPlayerAider.onPause();
    }

    @Override
    protected void onShaderLocationInit() {
        super.onShaderLocationInit();
        muSTMatrixHandler = getMaterialHandler("uSTMatrix");
        maPositionHandler = getMaterialHandler("aPosition");
        maTextureCoordHandler = getMaterialHandler("aTextureCoord");
        msTextureHandler = getMaterialHandler("sTexture");
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        mTextureID = TextureHelper.getExternalOESTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        Surface surface = new Surface(mSurface);
        mSurfaceVideoPlayerAider.onCreate(surface);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame() {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        super.onDrawFrame();
    }

    @Override
    protected void onDraw() {
        super.onDraw();
        synchronized (this) {
            if (updateSurface) {
                mSurface.updateTexImage();
                mSurface.getTransformMatrix(mSTMatrix);
                updateSurface = false;
            } else {
                return;
            }
        }

        if (msTextureHandler != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureID);
            GLES20.glUniform1i(msTextureHandler, 0);
        }
        if (maPositionHandler != -1) {
            GLES20.glVertexAttribPointer(maPositionHandler, 3, GLES20.GL_FLOAT,
                    false, 12,//3*4
                    mPositionBuffer);
            GLES20.glEnableVertexAttribArray(maPositionHandler);
        }
        if (maTextureCoordHandler != -1) {
            GLES20.glVertexAttribPointer(maTextureCoordHandler, 2, GLES20.GL_FLOAT,
                    false, 8,//2*4
                    mTexCoorBuffer);
            GLES20.glEnableVertexAttribArray(maTextureCoordHandler);
        }
        if (muSTMatrixHandler != -1) {
            GLES20.glUniformMatrix4fv(muSTMatrixHandler, 1, false, mSTMatrix, 0);
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
    }

    @Override
    public void onDestroy() {
        if(mSurfaceVideoPlayerAider!=null){
            mSurfaceVideoPlayerAider.onDestroy();
        }
        if(mSurface!=null){
            mSurface.release();
            mSurface=null;
        }
        if (mPositionBuffer != null) {
            mPositionBuffer.clear();
            mPositionBuffer = null;
        }
        if (mTexCoorBuffer != null) {
            mTexCoorBuffer.clear();
            mTexCoorBuffer = null;
        }
        super.onDestroy();
    }

    public SurfaceVideoPlayerAider getSurfaceVideoPlayerAider() {
        return mSurfaceVideoPlayerAider;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateSurface = true;
        requestRender();
    }
}
