package com.dandy.gles.engine;

import android.content.Context;

import com.dandy.helper.android.LogHelper;

public class Stage extends Group {
    private static final String TAG = "Stage";
//    private Image mImage;
    public Stage(Context context) {
        super(context);
//        mImage = Image.createFromAssets(mContext, "time_sensor_6.jpg");
    }

    @Override
    protected void onChildAdded(final Actor child) {
//        child.requestRender();
        if (child.mIsSurfaceCreated) {
            return;
        }
        child.runOnceBeforeDraw(new Runnable() {
            @Override
            public void run() {//不用担心没有值，因为这个是在onDraw的时候才会调用的
                child.onSurfaceCreated();
                child.onSurfaceChanged(mSurfaceWidth, mSurfaceHeight);
            }
        });
    }

    @Override
    public void onSurfaceCreated() {
        super.onSurfaceCreated();
//        mImage.onSurfaceCreated();
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onSurfaceChanged(width, height);
//        mImage.onSurfaceChanged(width, height);
    }

    //    @Override
    public void onDrawFrame() {
        LogHelper.d(TAG, LogHelper.getThreadName());
        for (final Actor child : mChildren) {
            child.onDrawFrame();
//            child.drawSelf();
        }
//        if (mImage != null) {
//            mImage.drawSelf();
//            mImage.onDrawFrame();
//        }
    }

    /**
     * Add the actors into this container.
     *
     * @param actors actors to add as children
     */
    public void add(Actor... actors) {
        addChild(actors);
    }

    @Override
    public void onDestroy() {
        for (final Actor child : mChildren) {
            child.onDestroy();
        }
        mChildren.clear();
    }
}
