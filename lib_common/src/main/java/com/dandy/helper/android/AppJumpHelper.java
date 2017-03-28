package com.dandy.helper.android;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class AppJumpHelper {
    public static void jumpToPictures(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setPackage("xxxx");// xxxx是具体报名，系统默认的一般是com.android.xx
        activity.startActivity(intent);
    }

    /**
     * <pre>
     * 可以酱紫解析
     * if (resultCode == RESULT_OK) {
     *     try {
     *         Uri mUri = data.getData();
     *         InputStream inputStream;
     *         if (mUri.getScheme().startsWith(&quot;http&quot;) || mUri.getScheme().startsWith(&quot;https&quot;)) {
     *             inputStream = new URL(mUri.toString()).openStream();
     *         } else {
     *             inputStream = ImageActivity.this.getContentResolver().openInputStream(mUri);
     *         }
     *         Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
     *     } catch (Exception e) {
     *         e.printStackTrace();
     *     }
     * 
     * }
     * </pre>
     * 
     * @param activity
     * @return
     */
//　　MediaStore.ACTION_IMAGE_CAPTURE 拍照；
//　　MediaStore.ACTION_VIDEO_CAPTURE录像。
    public static Intent spikPictures(Activity activity) {
        /* 开启Pictures画面Type设定为image */
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /* 使用Intent.ACTION_GET_CONTENT这个Action */
//        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        activity.startActivityForResult(intent, 1);
        // (在onActivityResult方法里，返回的意图里获取图片uri，在通过uri，结合内容提供者在查出图片的路径)
        return intent;
    }

    public static Intent spikCamera(Activity activity) {
        // 相片保存地址
        String path = "";
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        // 从这
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(path);
        if (file.exists()) {
            file.mkdirs();
        }
        // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 到这不用设置的话会在onActivityResult方法里，在意图获取一个处理过的bitmap
        activity.startActivityForResult(intent, 0);
        return intent;
    }
}
