package com.dandy.gles.engine.android;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.dandy.gles.engine.Image;
import com.dandy.gles.engine.Stage;
import com.dandy.helper.gles.IGLActor;
import com.dandy.helper.gles.IGLESRenderer;
import com.dandy.helper.gles.eglconfigchooser.AntiAliasingEGLConfigChooser;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class StageView extends GLSurfaceView {
    protected Context mContext;
    protected Stage mStage;

    public StageView(Context context) {
        super(context);
        init(context);
    }

    public StageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mStage = new Stage(context);
        mStage.setRequestRenderListener(new IGLActor.RequestRenderListener() {
            @Override
            public void onRequestRenderCalled() {
                requestRender();
            }
        });
        this.setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
//        setEGLConfigChooser(new AntiAliasingEGLConfigChooser());
        setRenderer(mRenderer); // 设置渲染器
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public Stage getStage() {
        return mStage;
    }


    private Renderer mRenderer = new Renderer() {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            mStage.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mStage.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // 清除深度缓冲与颜色缓冲
            mStage.onDrawFrame();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mStage.onDestroy();
    }

}
