package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;

import com.dandy.helper.android.res.AssetsHelper;

/**
 * <pre>
 *      直接显示纹理的方式，无需借助Matrix的一种方式，因为这里没有用到坐标变换
 * </pre>
 * Created by flycatdeng on 2017/4/18.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class SimpleImage extends SimpleTexture {
    private static final String TAG = "SimpleImage";

    private SimpleImage(Context context) {
        super(context);
        mVertexCount = 4;
    }

    public static SimpleImage createEmptyImage(Context context) {
        SimpleImage image = new SimpleImage(context);
        return image;
    }

    public static SimpleImage createFromAssets(Context context, String assetName) {
        SimpleImage image = new SimpleImage(context);
        image.setImageFromAsset(assetName);
        return image;
    }

    private Bitmap mTextureBmp;

    public void setImageFromAsset(String assetName) {
        if (assetName == null) {
            throw new NullPointerException("asset name cannot be null");
        }
        //TODO 建议做成可以缓存的
        mTextureBmp = AssetsHelper.getBitmap(mContext, assetName);
        setTexture(mTextureBmp);
    }


}
