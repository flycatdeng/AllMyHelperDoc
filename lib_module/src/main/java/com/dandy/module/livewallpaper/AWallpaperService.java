package com.dandy.module.livewallpaper;

import android.service.wallpaper.WallpaperService;

/**
 * 
 * @author dengchukun 2016年12月9日
 */
public abstract class AWallpaperService extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        return getWallpaperEngine();
    }

    public abstract Engine getWallpaperEngine();
}
