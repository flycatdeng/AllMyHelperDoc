package com.dandy;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.dandy.demo.fbo.FBOView;
import com.dandy.demo.wallpaper.timesensor.TimeSensorView;
import com.dandy.gles.engine.Image;
import com.dandy.gles.engine.Stage;
import com.dandy.gles.engine.android.StageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TimeSensorView view=new TimeSensorView(this);
//        setContentView(view);
//        FBOView view=new FBOView(this);

        StageView view=new StageView(this);
//        view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(view);
        Stage stage=view.getStage();

        Image testImage=Image.createFromAssets(this,"time_sensor_6.jpg");
        testImage.setMaterialFromAssets("gles_engine/simple.mat");
//        testImage.init();
        stage.add(testImage);
        view.requestRender();
    }
}
