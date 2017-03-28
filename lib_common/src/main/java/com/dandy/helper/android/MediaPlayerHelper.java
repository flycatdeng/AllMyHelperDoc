package com.dandy.helper.android;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class MediaPlayerHelper {

	private static final String TAG = "MediaPlayerHelper";

	@SuppressLint("NewApi")
	/**
	 * 获取帧缩略图，根据容器的高宽进行缩放
	 * @param videoPath
	 * @return
	 */
	public static Bitmap getVideoThumbnail(String videoPath) {
		Bitmap bitmap = null;
		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		try {
			retriever.setDataSource(videoPath);
			bitmap = retriever.getFrameAtTime(-1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} finally {
			try {
				retriever.release();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		LogHelper.d(TAG, LogHelper.getThreadName() + " bitmap=" + bitmap);
		return bitmap;
	}
}
