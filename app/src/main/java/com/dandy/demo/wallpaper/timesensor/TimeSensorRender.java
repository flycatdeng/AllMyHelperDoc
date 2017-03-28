package com.dandy.demo.wallpaper.timesensor;

import android.content.Context;

import com.dandy.helper.android.AssetsHelper;
import com.dandy.module.gles.simple.SimpleGLESRenderer;
import com.dandy.module.gles.simple.SimpleTexture;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/28.
 */

public class TimeSensorRender extends SimpleGLESRenderer {
        private SimpleTexture mSimpleTexture;
    private Context mContext;

    public TimeSensorRender(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        mSimpleTexture = new SimpleTexture(mContext);
        mSimpleTexture.init();
        mSimpleTexture.setTexture(AssetsHelper.getBitmap(mContext, "time_sensor_6.jpg"));
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        mSimpleTexture.onSurfaceChanged(width, height);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        if (mSimpleTexture != null) {
            mSimpleTexture.drawSelf();
        }
    }
}
