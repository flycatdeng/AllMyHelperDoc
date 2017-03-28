package com.dandy.helper.android;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dandy.helper.java.JFileHelper;

/**
 * 
 * @author dengchukun 2016年11月18日
 */
public class AssetsHelper {
    private static final String TAG = "AssetsHelper";

    /**
     * <pre>
     * 从Assets获取图片
     * </pre>
     * 
     * @param fileName
     * @return
     */
    public static Bitmap getBitmap(Context context, String fileName) {
        return BitmapFactory.decodeStream(FileHelper.getInputStreamFromAsset(context, fileName));
    }

    /**
     * <pre>
     * 得到assets目录下的文件的绝对路劲
     * </pre>
     * 
     * @param assetsName
     * @return
     */
    public static String getFileAbsolutePath(String assetsName) {
        return "file:///android_asset/" + assetsName;
    }

    public static String getResourcePath(Context conetxt, final String resourceName) {
        try {
            final String cacheFilePath = conetxt.getCacheDir() + resourceName;
            JFileHelper.removeFile(cacheFilePath);
            AssetManager assetManager = conetxt.getAssets();
            InputStream in = assetManager.open(resourceName);
            OutputStream out = new FileOutputStream(cacheFilePath);
            // do copy
            byte[] buffer = new byte[65536 * 2];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            LogHelper.d(TAG, "File Copied in storage");
            return cacheFilePath;
        } catch (Exception e) {
            LogHelper.d(TAG, "ERROR : " + e.toString());
        }
        return "";
    }
}
