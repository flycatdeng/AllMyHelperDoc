package com.dandy.helper.gles;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.dandy.helper.android.LogHelper;
import com.dandy.helper.java.ObjectHelper;

@SuppressLint("NewApi")
public class TextureHelper {
    private static final String TAG = TextureHelper.class.getSimpleName();

    public static int initTexture(Context context, int drawableId) {
        return initTexture(context, drawableId, true);
    }

    public static int initTextureID(Bitmap bitmap) {
        return initTextureID(bitmap, true);
    }

    public static int initTextureID(InputStream picIs) {
        return initTextureID(picIs, true);
    }
    
    /**
     * 得到该图片生成纹理之后的ID
     * 
     * @param picIs
     *            该图片的输入流
     * @return
     */
    public static int initTextureID(InputStream picIs, boolean recycleBmp) {
        LogHelper.d(TAG, "initTextureID(InputStream picIs) start");
        if (ObjectHelper.isNull(picIs)) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "InputStream is null");
        } else {
            LogHelper.d(TAG, LogHelper.getThreadName() + "InputStream is not null");
        }
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(picIs);
            if (ObjectHelper.isNull(bitmapTmp)) {
                LogHelper.d(TAG, LogHelper.getThreadName() + "Bitmap is null");
            } else {
                LogHelper.d(TAG, LogHelper.getThreadName() + "Bitmap is not null");
            }
        } finally {
            try {
                picIs.close();
            } catch (IOException e) {
                LogHelper.d(TAG, LogHelper.getThreadName() + " " + e.getMessage());
                e.printStackTrace();
            }
        }
        LogHelper.d(TAG, "initTextureID(InputStream picIs) end");
        return initTextureID(bitmapTmp, recycleBmp);
    }

    /**
     * 得到该图片生成纹理之后的ID
     * 
     * @param picIs
     *            该图片的输入流
     * @return
     */
    public static int initTextureID(Bitmap bitmap, boolean recycleBmp) {
        LogHelper.d(TAG, "initTextureID(Bitmap bitmap)");
        int textureId;
        // 生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(1, // 产生的纹理id的数量
                textures, // 纹理id的数组
                0 // 偏移量
        );
        textureId = textures[0];// 获取产生的纹理ID
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);// 绑定纹理ID,这里是非常关键的，因为之后不会用到这个ID,直到取图片的时候才用到
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);// 采用MIN采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);// 采用MAG采样方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);// 设置S轴拉伸方式
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);// 设置T轴拉伸方式
        // 实际加载纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型，在OpenGL
                                                 // ES中必须为GL10.GL_TEXTURE_2D
                0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
                bitmap, // 纹理图像
                0 // 纹理边框尺寸
        );
        if (recycleBmp) {
            bitmap.recycle(); // 纹理加载成功后释放图片，否则在纹理较多的项目中可能导致内存崩溃
        }
        return textureId;
    }

    public static int initTexture(Context context, int drawableId, boolean recycleBmp)// textureId
    {
        LogHelper.d(TAG, "initTextureID(Context context, int drawableId)");
        // 生成纹理ID
        int[] textures = new int[1];
        GLES20.glGenTextures(1, // 产生的纹理id的数量
                textures, // 纹理id的数组
                0 // 偏移量
        );
        int textureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        // 通过输入流加载图片===============begin===================
        InputStream is = context.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try {
            bitmapTmp = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 通过输入流加载图片===============end=====================
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型
                0, GLUtils.getInternalFormat(bitmapTmp), bitmapTmp, // 纹理图像
                GLUtils.getType(bitmapTmp), 0 // 纹理边框尺寸
        );
        if (recycleBmp) {
            bitmapTmp.recycle(); // 纹理加载成功后释放图片
        }
        return textureId;
    }

    /**
     * <pre>
     * 初始绑定图片到对应纹理ID
     * </pre>
     * 
     * @param bitmap
     */
    public static void setTextureOriginImage(Bitmap bitmap) {
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型
                0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
                GLUtils.getInternalFormat(bitmap), bitmap, // 纹理图像
                GLUtils.getType(bitmap), 0 // 纹理边框尺寸
        );
    }

    /**
     * <pre>
     * 初始绑定图片到对应纹理ID
     * </pre>
     * 
     * @param bitmap
     */
    public static void setTextureSimpleOriginImage(Bitmap bitmap) {
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, // 纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
                bitmap, // 纹理图像
                0 // 纹理边框尺寸
        );
    }

    /**
     * <pre>
     * 切换纹理切图
     * </pre>
     * 
     * @param bitmap
     */
    public static void changeTextureImage(Bitmap bitmap) {
        GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D,// 纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
                0,// 纹理的层次，0表示基本图像层，可以理解为直接贴图
                0, // x偏移量
                0, // y偏移量
                bitmap// 纹理图像
        );
    }
}
