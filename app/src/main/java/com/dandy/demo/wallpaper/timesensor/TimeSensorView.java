package com.dandy.demo.wallpaper.timesensor;

import android.content.Context;
import android.util.AttributeSet;

import com.dandy.helper.gles.IGLESRenderer;
import com.dandy.module.livewallpaper.AWallpaperActivityView;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class TimeSensorView extends AWallpaperActivityView {
    public TimeSensorView(Context context) {
        super(context);
    }

    public TimeSensorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public IGLESRenderer getGLESRender() {
//        return new TimeSensorRender(getContext());
        return new TimeSensorRenderFromEngine(getContext());
    }
}
