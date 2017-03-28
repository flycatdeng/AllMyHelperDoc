package com.dandy.android;

import com.dandy.helper.android.WindowHelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * 
 * @author dengchukun 2016年11月21日
 */
public class BaseActiVity extends Activity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowHelper.initWindowWH(this);
        mContext = this;
        BaseApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
    }

}
