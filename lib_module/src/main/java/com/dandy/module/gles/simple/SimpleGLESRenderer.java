package com.dandy.module.gles.simple;

import android.content.Context;
import android.opengl.GLES20;

import com.dandy.helper.gles.IGLESRenderer;

/**
 * <pre>
 *  一个简单的IGLESRenderer实现类，用于显示GL操作
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class SimpleGLESRenderer implements IGLESRenderer {
//    private SimpleTexture mSimpleTexture;
    private Context mContext;

    public SimpleGLESRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated() {
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        // 打开背面剪裁
        GLES20.glEnable(GLES20.GL_CULL_FACE);
//        mSimpleTexture = new SimpleTexture(mContext);
//        mSimpleTexture.init();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        // 设置视窗大小及位置
        GLES20.glViewport(0, 0, width, height);
//        mSimpleTexture.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        // 清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
//        if (mSimpleTexture != null) {
//            mSimpleTexture.drawSelf();
//        }
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onDestroy() {

    }
}
