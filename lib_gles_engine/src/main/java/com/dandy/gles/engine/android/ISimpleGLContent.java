package com.dandy.gles.engine.android;

import com.dandy.gles.engine.Stage;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/5/10.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public interface ISimpleGLContent {
    void onCreate(Stage stage);

    void onResume();

    void onPause();

    void onDestroy();
}
