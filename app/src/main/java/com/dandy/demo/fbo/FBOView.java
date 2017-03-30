package com.dandy.demo.fbo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.dandy.module.gles.glsurfaceview.GLESSurfaceView;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/3/30.
 */

public class FBOView extends GLESSurfaceView {
    private Context mContext;

    public FBOView(Context context) {
        super(context);
        init(context);
    }

    public FBOView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setRenderer(new FBORender(context));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
