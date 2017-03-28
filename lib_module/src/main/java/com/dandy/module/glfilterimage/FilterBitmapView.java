package com.dandy.module.glfilterimage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.View;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.TextureHelper;

public class FilterBitmapView extends GLSurfaceView {

    private static final String TAG = FilterBitmapView.class.getSimpleName();
    private Context mContext;
    private Bitmap mOriginBitmap;
    private SceneRenderer mRenderer;
    private int mTextureId = -1;
    /**
     * GLSurfaceView的宽高
     */
    protected int mSurfaceWidth, mSurfaceHeight;

    /**
     * 图像宽高
     */
    protected int mImageWidth, mImageHeight;
    public FilterBitmapView(Context context) {
        super(context);
        init(context);
    }

    public FilterBitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    Image image;

    private void init(Context context) {
        mContext = context;

        this.setEGLContextClientVersion(2);
        mRenderer = new SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        requestFocus();
        setFocusableInTouchMode(true);

        setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    protected void takePhoto() {
        this.queueEvent(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, LogHelper.getThreadName());
                int width = mImageWidth;
                int height = mImageHeight;
                int[] mFrameBuffers = new int[1];
                int[] mFrameBufferTextures = new int[1];
                GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
                GLES20.glGenTextures(1, mFrameBufferTextures, 0);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);
                GLES20.glViewport(0, 0, width, height);
                int textureId = mTextureId;
//                int textureId = TextureHelper.initTextureID(mOriginBitmap);
                image.onSurfaceChanged(width, height);// 上下方向还要对换一下
                image.onDrawSelf(textureId);
                IntBuffer ib = IntBuffer.allocate(width * height);
                GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);
                Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                mBitmap.copyPixelsFromBuffer(IntBuffer.wrap(ib.array()));
                GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
                GLES20.glDeleteTextures(1, mFrameBufferTextures, 0);
                GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
                image.onSurfaceChanged(mSurfaceWidth, mSurfaceHeight);
                onGetBitmapFromGL(mBitmap);
            }
        });
    }

    protected void onGetBitmapFromGL(Bitmap mBitmap) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/sdcard/screen5.png");// 注意app的sdcard读写权限问题
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(CompressFormat.PNG, 100, fos);// 压缩成png,100%显示效果
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SceneRenderer implements Renderer {

        public SceneRenderer() {
            image = new Image(mContext);
        }

        public void onDrawFrame(GL10 gl) {
            GLES20.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
            // 清除深度缓冲与颜色缓冲
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            image.onDrawSelf(mTextureId);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // 设置视窗大小及位置
            GLES20.glViewport(0, 0, width, height);
            image.onSurfaceChanged(width, height);
            mSurfaceWidth = width;
            mSurfaceHeight = height;
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // 设置屏幕背景色RGBA
            GLES20.glClearColor(1f, 0, 0f, 0.0f);
            GLES20.glDisable(GL10.GL_DITHER);
            GLES20.glClearColor(0, 0, 0, 0);
            GLES20.glEnable(GL10.GL_CULL_FACE);
            GLES20.glEnable(GL10.GL_DEPTH_TEST);
            image.init();
            if (mTextureId == -1) {
                try {
//                    InputStream openIns = mContext.getAssets().open("eyeball_eyeball.png");
                    InputStream openIns = mContext.getAssets().open("texture-rain-fg.png");
                    Bitmap bitmap = BitmapFactory.decodeStream(openIns);
                    mOriginBitmap = BitmapFactory.decodeStream(openIns);
                    mImageWidth = bitmap.getWidth();
                    mImageHeight = bitmap.getHeight();
                    image.setImageWH(bitmap.getWidth(), bitmap.getHeight());
                    mTextureId = TextureHelper.initTextureID(bitmap, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LogHelper.d(TAG, LogHelper.getThreadName());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.d(TAG, LogHelper.getThreadName());
    }

}
