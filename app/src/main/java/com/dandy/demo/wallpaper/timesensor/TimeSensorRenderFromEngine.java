package com.dandy.demo.wallpaper.timesensor;

import android.content.Context;

import com.dandy.gles.engine.Image;
import com.dandy.helper.android.AssetsHelper;
import com.dandy.module.gles.simple.SimpleGLESRenderer;
import com.dandy.module.gles.simple.SimpleTexture;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class TimeSensorRenderFromEngine extends SimpleGLESRenderer {
    private Context mContext;
    private Image mImage;

    public TimeSensorRenderFromEngine(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        mImage = Image.createFromAssets(mContext, "time_sensor_6.jpg");
        mImage.init();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        mImage.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        if (mImage != null) {
            mImage.drawSelf();
        }
    }
}
