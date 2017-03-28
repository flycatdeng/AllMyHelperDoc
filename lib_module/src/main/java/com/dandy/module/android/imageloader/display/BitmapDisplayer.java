package com.dandy.module.android.imageloader.display;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Displays {@link Bitmap} . Implementations can apply some changes to Bitmap or any animation for displaying Bitmap.<br />
 * Implementations have to be thread-safe.
 */
public interface BitmapDisplayer {
	
	void display(Bitmap bitmap, ImageView imageView);
	void display(int resouceID, ImageView imageView);
}
