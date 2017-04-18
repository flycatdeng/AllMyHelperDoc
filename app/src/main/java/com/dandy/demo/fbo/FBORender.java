package com.dandy.demo.fbo;

import android.content.Context;

import com.dandy.gles.engine.Image;
import com.dandy.helper.android.LogHelper;
import com.dandy.module.gles.fbo.FBOAider;
import com.dandy.module.gles.simple.SimpleGLESRenderer;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/30.
 */

public class FBORender extends SimpleGLESRenderer {
    private static final String TAG = "FBORender";
    private Image mImage;
    private FBOAider mFBOAider;
    private Image mImageFBOTest;

    public FBORender(Context context) {
        super(context);
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        LogHelper.d(TAG, LogHelper.getThreadName());
        mFBOAider = new FBOAider();
        mImage = Image.createEmptyImage(mContext);
        mImage.init();

        mImageFBOTest = Image.createFromAssets(mContext, "time_sensor_6.jpg");
        mImageFBOTest.init();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
        mImage.onSurfaceChanged(width, height);
        mFBOAider.init(mContext, width, height);
    }

    @Override
    public void onDrawFrame() {
        super.onDrawFrame();
        if (mFBOAider != null) {
            mFBOAider.drawSelf(mImageFBOTest);
        }
        if (mImage != null) {
            mImage.initTexture(mFBOAider.getFBOTextureId());
            mImage.drawSelf();
//            mImage.onDrawFrame();
        }
    }
}
