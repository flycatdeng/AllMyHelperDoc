package com.dandy.module.gles.glsurfaceview;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.dandy.helper.gles.IGLESRenderer;

/**
 * GLSurfaceView优化类，必须调用{@link #setRenderer(IGLESRenderer)},并且实现IGLESRenderer
 *
 * @author flycatdeng
 */
public class GLESSurfaceView extends GLSurfaceView {
    protected Context  mContext;
    private IGLESRenderer mGLESRenderer;

    public GLESSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public GLESSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext=context;
        this.setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
    }

    /**
     * <pre>
     *  默认的设置透明背景的方法，根据实际情况，可能setEGLConfigChooser中的alpha可能要设置成0
     *  再者就是这个方法需要在setRenderer之前调用才有效
     * </pre>
     */
    public void setTranslucent() {
        // 设置背景透明，否则一般加载时间长的话会先黑一下，但是也有问题，就是在它之上无法再有View了，因为它是top的，用的时候需要注意，必要的时候将其设置为false
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }

    /**
     * <pre>
     *     设置IGLESRenderer，该render包含了GLSurfaceView.Renderer的操作，再者就是包含了onResume和onPause等的调用
     *     默认的渲染模式是RENDERMODE_WHEN_DIRTY
     * </pre>
     * @param renderer
     */
    public void setRenderer(IGLESRenderer renderer) {
        mGLESRenderer = renderer;
        setRenderer(mRenderer); // 设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private Renderer mRenderer = new Renderer() {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mGLESRenderer.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mGLESRenderer.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            mGLESRenderer.onDrawFrame();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        mGLESRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGLESRenderer.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        mGLESRenderer.onDestroy();
        super.onDetachedFromWindow();
    }
}
