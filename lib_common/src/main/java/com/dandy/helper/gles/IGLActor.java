package com.dandy.helper.gles;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/20.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public interface IGLActor {
    public void onSurfaceCreated();

    public void onSurfaceChanged(int width, int height);

    public void onDrawFrame();

    public boolean isSurfaceCreated();

    /**
     * 请求渲染
     */
    public void requestRender();

    public void setRequestRenderListener(RequestRenderListener listener);

    public RequestRenderListener getRequestRenderListener();

    interface RequestRenderListener {
        public void onRequestRenderCalled();
    }
}
