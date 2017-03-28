package com.dandy.module.livewallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * 这个view是用来帮助在activity中查看壁纸效果的，一些简单的效果可以直接看，直接继承该类实现接口即可，如果是有触碰之类的交互则需要重写一些方法了
 * 
 * @author dengchukun 2016年12月12日
 */
@SuppressLint("NewApi")
public abstract class AWallpaperActivityView extends GLSurfaceView {
    private Renderer mRenderer;
    private IRenderLifeRecycleCallback mRenderLifeRecycleCallback;

    public AWallpaperActivityView(Context context) {
        super(context);
        init(context);
    }

    public AWallpaperActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // 设置背景透明，否则一般加载时间长的话会先黑一下，但是也有问题，就是在它之上无法再有View了，因为它是top的，用的时候需要注意，必要的时候将其设置为false
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);

        mRenderer = getGLSurfaceViewRenderer();
        mRenderLifeRecycleCallback = getRenderLifeRecycleCallback();

        this.setEGLContextClientVersion(2); // 设置使用OPENGL ES2.0
        setRenderer(mRenderer); // 设置渲染器
    }

    @Override
    public void onPause() {
        super.onPause();
        mRenderLifeRecycleCallback.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mRenderLifeRecycleCallback.onResume();
    }

    public abstract Renderer getGLSurfaceViewRenderer();

    /**
     * <pre>
     * 渲染生命周期回调
     * may be null
     * </pre>
     * 
     * @return
     */
    public abstract IRenderLifeRecycleCallback getRenderLifeRecycleCallback();
}
