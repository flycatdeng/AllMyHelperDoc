package com.dandy.gles.engine.android;

import android.content.Context;
import android.view.SurfaceHolder;

import com.dandy.gles.engine.Stage;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/5/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public abstract class SimpleGLWallpaperService extends GLWallpaperService {
    @Override
    public Engine onCreateEngine() {
        return new SimpleGLEngine();
    }

    public abstract ISimpleGLContent getSimpleGLContent(Context context);

    public class SimpleGLEngine extends GLEngine {
        private ISimpleGLContent mContent;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mContent = getSimpleGLContent(mContext);
            Stage stage = getStage();
            mContent.onCreate(stage);
        }

        @Override
        protected void onResume() {
            super.onResume();
            mContent.onResume();
        }

        @Override
        protected void onPause() {
            super.onPause();
            mContent.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mContent.onDestroy();
        }
    }
}
