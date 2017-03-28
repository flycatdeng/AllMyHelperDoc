package com.dandy.gles.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.dandy.helper.android.AssetsHelper;
import com.dandy.helper.gles.TextureHelper;

public class Image extends Texture {
    private static final String TAG = "Image";


    private Image(Context context) {
        super(context);
        mVertexCount = 4;
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
        mRunOnDraw.addToPending(new Runnable() {
            @Override
            public void run() {
                if (mTextureID == -1) {
                    mTextureID = TextureHelper.initTextureID(mTextureBmp, true);
                } else {
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                    TextureHelper.changeTextureImage(mTextureBmp);
                }
            }
        });
    }


}
