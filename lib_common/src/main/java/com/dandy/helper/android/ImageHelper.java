package com.dandy.helper.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * 图片帮助类
 * 
 * @author dengchukun
 * 
 */
public class ImageHelper {
    /**
     * byte[]转Drawable
     * 
     * @param bytes
     *            字节数组
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable byteArrayDrawable(byte[] bytes) {
        Bitmap bitmap = byteArrayToBitmap(bytes);
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return new BitmapDrawable(bitmap);
    }

    /**
     * Drawable转byte[]
     * 
     * @param drawable
     *            original Drawable object
     * @return
     */
    public static byte[] drawableToByteArray(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return bitmapToByteArray(bitmap);
    }

    /**
     * Bitmap转Drawable
     * 
     * @param drawable
     *            original Drawable object
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 从assets目录下获取Drawable对象格式的图片
     * 
     * @param context
     * @param name
     * @return
     */
    public static Drawable getDrawableFromAssets(Context context, String name) {
        return bitmapToBrawable(getBitmapFromAssets(context, name));
    }

    /**
     * bitmap转drawable
     * 
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToBrawable(Bitmap bitmap) {
        @SuppressWarnings("deprecation")
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * 从assets目录下获得图片bitmap
     * 
     * @param context
     * @param name
     *            图片路径
     * @return
     */
    public static Bitmap getBitmapFromAssets(Context context, String name) {
        Bitmap resultBitmap = null;
        try {
            InputStream ins = context.getAssets().open(name);
            resultBitmap = BitmapFactory.decodeStream(ins);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBitmap;
    }

    public static Bitmap getBitmapFromPath(Context context, String fullName) {
        Bitmap resultBitmap = null;
        FileInputStream ins;
        try {
            ins = new FileInputStream(fullName);
            resultBitmap = BitmapFactory.decodeStream(ins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resultBitmap;
    }

    /**
     * 由bitmap对象转成输入流
     * 
     * @param bitmap
     * @return
     */
    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        InputStream ins = new ByteArrayInputStream(bitmapToByteArray(bitmap));
        return ins;
    }

    /**
     * 处理图片 放大、缩小到合适位置
     */
    public static Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth / bitmap.getWidth(), newHeight / bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    /**
     * bitmap转成字节数组
     * 
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 从drawable或raw等中获取bitmap格式的图片
     * 
     * @param context
     *            上下文
     * @param resID
     *            id
     * @return
     */
    public static Bitmap getBitmapFromRes(Context context, int resID) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        return bitmap;
    }

    /**
     * 字节数组转bitmap
     * 
     * @param data
     * @return
     */
    public static Bitmap byteArrayToBitmap(byte[] data) {
        if (data.length != 0) {
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            return null;
        }
    }
}
