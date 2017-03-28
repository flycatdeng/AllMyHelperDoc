package com.dandy.module.livewallpaper;

import com.dandy.helper.android.LogHelper;

import android.view.MotionEvent;
import android.view.SurfaceHolder;

/**
 * 
 * @author dengchukun 2016年12月9日
 */
public class AndroidWallpaperService extends AWallpaperService {

    protected static final String TAG = AndroidWallpaperService.class.getSimpleName();

    @Override
    public Engine getWallpaperEngine() {
        return new Engine() {

            @Override
            public SurfaceHolder getSurfaceHolder() {
                LogHelper.d(TAG, LogHelper.getThreadName());
                return super.getSurfaceHolder();
            }

            @Override
            public void onCreate(SurfaceHolder surfaceHolder) {
                super.onCreate(surfaceHolder);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }

            @Override
            public void onDestroy() {
                LogHelper.d(TAG, LogHelper.getThreadName());
                super.onDestroy();
            }

            @Override
            public void onVisibilityChanged(boolean visible) {
                super.onVisibilityChanged(visible);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }

            @Override
            public void onTouchEvent(MotionEvent event) {
                super.onTouchEvent(event);
            }

            @Override
            public void onSurfaceCreated(SurfaceHolder holder) {
                super.onSurfaceCreated(holder);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }

            @Override
            public void onSurfaceDestroyed(SurfaceHolder holder) {
                super.onSurfaceDestroyed(holder);
                LogHelper.d(TAG, LogHelper.getThreadName());
            }

        };
    }

}
