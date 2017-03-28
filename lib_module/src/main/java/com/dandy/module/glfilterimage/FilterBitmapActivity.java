package com.dandy.module.glfilterimage;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.dandy.android.BaseActiVity;
import com.dandy.helper.R;
import com.dandy.helper.android.ActivityHelper;

public class FilterBitmapActivity extends BaseActiVity {
    protected static final String TAG = FilterBitmapActivity.class.getSimpleName();
    private FrameLayout mRootView;
    FilterBitmapView filterBitmapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelper.setActivityFullScreen(this);
        setContentView(R.layout.activity_viewable);
        mRootView = (FrameLayout) findViewById(R.id.viewable_root);

        filterBitmapView = new FilterBitmapView(mContext);
        mRootView.addView(filterBitmapView);
//        filterBitmapView.resetSize();
    }

    interface OnBitmapCallBack {
        void onBitmapCallBack(Bitmap bitmap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterBitmapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        filterBitmapView.onPause();
    }

}
