package com.dandy.module.livewallpaper;

/**
 * gles2 render 中可能会使用到的生命周期
 * 
 * @author dengchukun 2016年12月9日
 */
public interface IRenderLifeRecycleCallback {

    public void onResume();

    public void onPause();

    public void onDestroy();
}
