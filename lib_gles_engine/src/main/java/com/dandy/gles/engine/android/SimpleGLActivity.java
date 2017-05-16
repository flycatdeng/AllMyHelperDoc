package com.dandy.gles.engine.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.dandy.gles.engine.Stage;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/5/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public abstract class SimpleGLActivity extends Activity {
    private ISimpleGLContent mContent;
    private StageView mStageView;

    public abstract ISimpleGLContent getSimpleGLContent(Context context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getSimpleGLContent(this);
        mStageView = new StageView(this);
        mStageView.initRenderer();
        setContentView(mStageView);
        Stage stage = mStageView.getStage();
        mContent.onCreate(stage);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mStageView != null) {
            mStageView.onResume();
        }
        mContent.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mStageView != null) {
            mStageView.onPause();
        }
        mContent.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mContent != null) {
            mContent.onDestroy();
            mContent = null;
        }
        if (mStageView != null) {
            mStageView.onDestroy();
            mStageView = null;
        }
        super.onDestroy();
    }
}
