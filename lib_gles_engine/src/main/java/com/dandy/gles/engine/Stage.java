package com.dandy.gles.engine;

import android.content.Context;

public class Stage extends Group {

    public Stage(Context context) {
        super(context);
    }

    @Override
    protected void onChildAdded(final Actor child) {
//        child.requestRender();
        if (child.mIsSurfaceCreated) {
            return;
        }
        child.mRunOnDraw.addToPending(new Runnable() {
            @Override
            public void run() {
                child.onSurfaceCreated();
            }
        });
    }

    public void onSurfaceCreated() {
        super.onSurfaceCreated();
        for (final Actor child : mChildren) {
            if (child.mIsSurfaceCreated) {
                continue;
            }
            child.mRunOnDraw.addToPending(new Runnable() {
                @Override
                public void run() {
                    child.onSurfaceCreated();
                }
            });
        }
    }

    public void onSurfaceChanged(final int width, final int height) {
        for (final Actor child : mChildren) {
            child.mRunOnDraw.addToPending(new Runnable() {
                @Override
                public void run() {
                    child.onSurfaceChanged(width, height);
                }
            });
        }
    }

    public void onDrawFrame() {
    }

    /**
     * Add the actors into this container.
     *
     * @param actors actors to add as children
     */
    public void add(Actor... actors) {
        addChild(actors);
    }
}
