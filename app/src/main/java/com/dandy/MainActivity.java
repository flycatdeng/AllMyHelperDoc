package com.dandy;

import android.app.Activity;
import android.os.Bundle;

import com.dandy.demo.wallpaper.timesensor.TimeSensorView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TimeSensorView view=new TimeSensorView(this);
        setContentView(view);
    }
}
