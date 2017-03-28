package com.dandy.helper.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by flycatdeng on 2017/3/20.
 */
public class BlurBitmapAider {

    private BlurBitmapAider() {

    }

    private static BlurBitmapAider mBlurBitmapAider;

    public static BlurBitmapAider getInstance() {
        if (mBlurBitmapAider == null) {
            synchronized (BlurBitmapAider.class) {
                if (mBlurBitmapAider == null) {
                    mBlurBitmapAider = new BlurBitmapAider();
                }
            }
        }
        return mBlurBitmapAider;
    }

    private ScriptIntrinsicBlur mBlurScript;
    private RenderScript mScript;


    public void init(Context context) {
        mScript = RenderScript.create(context.getApplicationContext());
        mBlurScript = ScriptIntrinsicBlur.create(mScript, Element.U8_4(mScript));
    }

    public Bitmap getBlurBitmap(Bitmap bmp, int targetWidth, int targetHeight, float blurRadius, boolean recycleBmp) {
        Bitmap outBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Allocation allIn = Allocation.createFromBitmap(mScript, bmp);
        Allocation allOut = Allocation.createFromBitmap(mScript, outBitmap);

        // Set the radius of the blur: 0 < radius <= 25
        mBlurScript.setRadius(blurRadius);

        // Perform the Renderscript
        mBlurScript.setInput(allIn);
        mBlurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
//        mBitmap.recycle();
        mScript.destroy();

        if (recycleBmp) {
            bmp.recycle();
        }
        if (outBitmap != null) {
            return outBitmap;
        }
        return null;
    }
}
