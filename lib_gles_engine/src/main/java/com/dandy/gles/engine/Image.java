package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;

import com.dandy.helper.android.AssetsHelper;

public class Image extends Texture {
    private static final String TAG = "Image";


    private Image(Context context) {
        super(context);
        mVertexCount = 4;
    }

    public static Image createEmptyImage(Context context) {
        Image image = new Image(context);
        return image;
    }

    public static Image createFromAssets(Context context, String assetName) {
        Image image = new Image(context);
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
