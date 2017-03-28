package com.dandy.helper.renderscript;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by flycatdeng on 2017/3/14.
 */

public class BlurBitmapHelper {
    /**
     * <pre>
     *     get a blurred Bitmap instance
     *     得到一个模糊过的Bitmap
     * </pre>
     * 
     * @param context
     * @param bmp
     *            源Bitmap
     * @param targetWidth
     *            目标Bitmap宽度
     * @param targetHeight
     *            目标Bitmap高度
     * @param blurRadius
     *            模糊程度0 < radius <= 25
     * @param recycleBmp
     *            是否释放源Bitmap
     * @return
     */
    public static Bitmap getBlurBitmap(Context context, Bitmap bmp, int targetWidth, int targetHeight,float blurRadius,boolean recycleBmp){
        Bitmap outBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        RenderScript rs = RenderScript.create(context.getApplicationContext());
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bmp);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // Set the radius of the blur: 0 < radius <= 25
        blurScript.setRadius(blurRadius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
//        mBitmap.recycle();
        rs.destroy();

        if (outBitmap != null) {
            return outBitmap;
        }
        if (recycleBmp) {
            bmp.recycle();
            return null;
        }
        return bmp;
    }
}
