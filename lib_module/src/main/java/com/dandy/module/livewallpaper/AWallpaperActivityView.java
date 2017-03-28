package com.dandy.module.livewallpaper;
import android.content.Context;
import android.util.AttributeSet;

import com.dandy.helper.gles.IGLESRenderer;
import com.dandy.module.gles.glsurfaceview.GLESSurfaceView;

/**
 * 这个view是用来帮助在activity中查看壁纸效果的，一些简单的效果可以直接看，直接继承该类实现接口即可，如果是有触碰之类的交互则需要重写一些方法了
 *
 * @author dengchukun 2016年12月12日
 */
public abstract class AWallpaperActivityView extends GLESSurfaceView {

    public AWallpaperActivityView(Context context) {
        super(context);
        init(context);
    }

    public AWallpaperActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setTranslucent();
        setRenderer(getGLESRender()); // 设置渲染器
    }

    public abstract IGLESRenderer getGLESRender();
}
