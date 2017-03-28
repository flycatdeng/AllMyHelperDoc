package com.dandy.helper.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

/**
 * 文件帮助类
 * 
 * @author flycatdeng 1.创建文件夹的方法； 2.复制文件的方法 3.将raw目录下的文件复制到sdcard
 */
public class FileCopyHelper {

    private static final String TAG = "FileCopyHelper";

    /**
     * 将raw中的文件复制到sdcard中的方法
     * 
     * @param mContext
     *            :上下文
     * @param rawID
     *            ：raw中文件的ID
     * @param myDir
     *            ：我自己的目录
     * @param fileName
     *            :目标文件名
     * @return
     */
    public static String fileFromRaw2SD(Context mContext, int rawID, String myDir, String fileName) {
        String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + myDir + "/";
        return fileFromRaw2Path(mContext, rawID, filePath, fileName);
    }

    /**
     * 将raw中的文件复制到指定的路径（无目录则会自动创建）
     * 
     * @param mContext上下文
     * @param rawID
     *            ：raw内的文件的ID
     * @param fileDirPath
     *            ：指定的目录
     * @param fileName
     *            ：文件名
     * @return：返回文件名的绝对路径
     */
    public static String fileFromRaw2Path(Context mContext, int rawID, String fileDirPath, String fileName) {
        String filePath = fileDirPath + "/" + fileName;
        boolean isDirExist = isOrCreateDir(fileDirPath);// 判断是否存在或创建目录是否成功
        // 如果目录存在，即使之前不存在也会创建这个时候就会存在了，如果在上一步创建目录失败，那也没有必要再将文件写入了
        if (isDirExist) {
            LogHelper.d(TAG, "要存储的目录" + filePath + "存在");
            copyFile(mContext, rawID, filePath);
        }
        return filePath;
    }

    /**
     * 判断是否存在或创建目录是否成功的方法
     * 
     * @param fileDirPath
     *            :要创建的目录
     * @return true表示存在或者不存在但是创建成功了
     */
    public static boolean isOrCreateDir(String fileDirPath) {
        File dir = new File(fileDirPath);// 目录
        if (!dir.exists()) {// 如果目录不存在
            LogHelper.d(TAG, "所要存储的目录" + fileDirPath + "不存在，准备创建");
            if (dir.mkdirs()) {
                LogHelper.d(TAG, "已经创建文件存储目录");
                return true;
            } else {
                LogHelper.d(TAG, "创建目录失败，不再写入文件");
                return false;
            }
        }
        return true;
    }

    /**
     * 将raw内的文件复制到指定的目录下的方法
     * 
     * @param mContext上下文
     * @param rawID
     *            ：raw内的文件的ID
     * @param filePath
     *            ：指定要复制到的绝对路径 如果文件不存在则创建文件，存在则返回true
     */
    public static boolean copyFile(Context mContext, int rawID, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {// 文件不存在则写入
                InputStream ins = mContext.getResources().openRawResource(rawID);// 通过rawid得到raw里的文件流
                LogHelper.d(TAG, "开始读入");
                FileOutputStream fos = new FileOutputStream(file);
                LogHelper.d(TAG, "开始写出");
                byte[] buffer = new byte[8192];
                int count = 0;
                LogHelper.d(TAG, "准备循环了");
                while ((count = ins.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                LogHelper.d(TAG, "已经创建该文件");
                fos.close();
                ins.close();
                return true;
            } else {
                LogHelper.d(TAG, "该文件已经存在");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
