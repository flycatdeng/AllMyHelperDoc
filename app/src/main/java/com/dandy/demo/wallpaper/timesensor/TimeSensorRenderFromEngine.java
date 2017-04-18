package com.dandy.demo.wallpaper.timesensor;

import android.content.Context;

import com.dandy.gles.engine.Image;
import com.dandy.module.gles.simple.SimpleGLESRenderer;

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
        mImage = Image.createFromAssets(mContext, "time_sensor_6.jpg");
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
//        mImage.init();
        mImage.onSurfaceCreated();
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
//            mImage.onDrawFrame();
        }
    }
}
