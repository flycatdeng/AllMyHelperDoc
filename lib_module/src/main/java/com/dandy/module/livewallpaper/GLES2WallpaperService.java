package com.dandy.module.livewallpaper;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.gles.CommonUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.view.SurfaceHolder;

/**
 * <pre>
 * OpenGL动态壁纸Service，基本框架。
 * 真正的动态壁纸还需要实现其具体方法 {@link #getGLSurfaceViewRenderer()}这个方法主要的就是平时用的GLSurfaceView中的Renderer了
 * {@link #getRenderLifeRecycleCallback()}主要是用于在Render中会使用的一些其他工具需要的生命周期
 * </pre>
 * 
 * @author dengchukun 2016年12月9日
 */
@SuppressLint("NewApi")
public abstract class GLES2WallpaperService extends AWallpaperService {
    @Override
    public Engine getWallpaperEngine() {
        return new OpenGLES2Engine();
    }

    /**
     * 实现的是Engine，返回该对象 动态壁纸就可以渲染了
     * 
     * @author dengchukun 2016年12月9日
     */
    class OpenGLES2Engine extends Engine {
        private final String TAG = OpenGLES2Engine.class.getSimpleName();
        private ProxyGLSurfaceView mProxyGLSurfaceView;
        private IRenderLifeRecycleCallback mRenderLifeRecycleCallback;

        /**
         * <pre>
         * 开始一直很纠结，为什么会用到这么一个GLSurfaceView，而且没有添加这个View的痕迹，
         * 细心的我终于还是发现了这个天大的秘密，其最主要的作用就是getHolder()方法，里面得到的是mEngine.getSurfaceHolder()
         * 什么意思呢？就是Engine初始化完之后就会有一个SurfaceHolder对象，那只要在这个对象上绘制就行了；
         * 所以呢，GLSurfaceView的作用就是将Engine的SurfaceHolder对象拿到，然后用自身的Render去绘制；
         * 也就是说GLSurfaceView的Render绘制的东西在Engine的SurfaceHolder上
         * </pre>
         * <p>
         * 我开始还纠结另一个问题，就是用户触摸怎么办？岂不是要重新写这个类？后来想了想记得Engine中的onOffsetsChanged已经有这个功效了，也就不用担心了
         * <p style="color:red">
         * 超级邪门，这个类放在外面还不行，硬是要放到OpenGLES2Engine类里边才能有用，否则ProxyGLSurfaceView的TAG都得不到（null）
         * </p>
         * 
         * @author dengchukun 2016年12月9日
         */
        class ProxyGLSurfaceView extends GLSurfaceView {
            private final String TAG = ProxyGLSurfaceView.class.getSimpleName();

            public ProxyGLSurfaceView(Context context) {
                super(context);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }

            public void init() {
                LogHelper.d(TAG, LogHelper.getThreadName());
                setEGLContextClientVersion(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    setPreserveEGLContextOnPause(true);
                }
                setRenderer(getGLSurfaceViewRenderer());
            }

            @Override
            public SurfaceHolder getHolder() {
                LogHelper.d(TAG, LogHelper.getThreadName());
                return getSurfaceHolder();
            }

            public void onDestroy() {
                super.onDetachedFromWindow();
            }
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            boolean isSupportEs2 = CommonUtils.isSupportEs2(GLES2WallpaperService.this);
            if (!isSupportEs2) {
                return;
            }
            mProxyGLSurfaceView = new ProxyGLSurfaceView(GLES2WallpaperService.this);
            LogHelper.d(TAG, LogHelper.getThreadName());
            mProxyGLSurfaceView.init();
            mRenderLifeRecycleCallback = getRenderLifeRecycleCallback();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            LogHelper.d(TAG, LogHelper.getThreadName());
            if (mProxyGLSurfaceView != null) {
                mProxyGLSurfaceView.onDestroy();
            }
            if (mRenderLifeRecycleCallback != null) {
                mRenderLifeRecycleCallback.onDestroy();
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            LogHelper.d(TAG, LogHelper.getThreadName());
            if (mProxyGLSurfaceView == null) {
                return;
            }
            if (visible) {
                mProxyGLSurfaceView.onResume();
                if (mRenderLifeRecycleCallback != null) {
                    mRenderLifeRecycleCallback.onResume();
                }
            } else {
                mProxyGLSurfaceView.onPause();
                if (mRenderLifeRecycleCallback != null) {
                    mRenderLifeRecycleCallback.onPause();
                }
            }
        }
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
