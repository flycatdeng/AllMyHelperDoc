package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.IMVPMatrixOperation;
import com.dandy.helper.gles.MVPMatrixAider;
import com.dandy.helper.gles.Material;
import com.dandy.helper.gles.TextureHelper;
import com.dandy.helper.gles.Vec3;
import com.dandy.helper.java.PendingThreadAider;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/18.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public abstract class Actor2{
    private static final String TAG = "Actor";
    protected Context mContext;
    protected PendingThreadAider mRunOnceOnDraw = new PendingThreadAider();
    protected PendingThreadAider mRunOnceBeforeDraw = new PendingThreadAider();
    protected int mVertexCount = 0;
    protected int mProgramID = -1;// 自定义渲染管线着色器程序id
    protected int mTextureID = -1;
    protected String mDefaultMaterialName = "gles_engine_material/default_simple.mat";
    protected Material mMaterial;

    public void onDestroy() {
        GLES20.glDeleteProgram(mProgramID);
    }

    public void onDrawFrame() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        GLES20.glClearColor(1.0f,1.0f,0.0f,1.0f);
        mRunOnceBeforeDraw.runPendings();
        if (mProgramID == -1) {//如果没有设置材质，则使用默认的材质
            setMaterialFromAssets(mDefaultMaterialName);
        }
        if (mProgramID == -1) {
            throw new RuntimeException("mProgramID == -1");
        }
        onDraw();
//        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);
        // 清除深度缓冲与颜色缓冲
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0.0f,1.0f,1.0f,1.0f);
    }

    private void onDraw() {
        LogHelper.d(TAG, LogHelper.getThreadName()+mVertexCount);
        GLES20.glUseProgram(mProgramID);
        mRunOnceOnDraw.runPendings();
        onEnableVertexAttribArray();
        onDrawOthers();
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mVertexCount);
        onDisableVertexAttribArray();
        onDrawArraysAfter();
    }

    /**
     * 绘制时设置顶点坐标和纹理坐标数据等操作
     */
    protected abstract void onEnableVertexAttribArray();

    /**
     * 绘制其他内容
     */
    protected abstract void onDrawOthers();

    protected abstract void onDrawArraysPre();

    /**
     * 绘制完数据之后将顶点坐标数据属性关闭
     */
    protected abstract void onDisableVertexAttribArray();

    protected abstract void onDrawArraysAfter();


    public void setMaterialFromAssets(final String materialFile) {
        runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {
                LogHelper.d(TAG, "setMaterialFromAssets materialFile=" + materialFile);
                mMaterial = new Material(mContext, materialFile);
                mProgramID = mMaterial.getProgramID();
            }
        });
}



    public void runOnceBeforeDraw(Runnable runnable) {
        mRunOnceBeforeDraw.addToPending(runnable);
    }

    public void runOnceOnDraw(Runnable runnable) {
        mRunOnceOnDraw.addToPending(runnable);
    }
}
