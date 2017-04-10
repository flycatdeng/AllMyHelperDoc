package com.dandy.helper.gles;

import com.dandy.helper.android.LogHelper;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author dengchukun 2016年11月23日
 */
public class GLCommonUtils {

    private static final String TAG = GLCommonUtils.class.getSimpleName();

    /**
     * <pre>
     * 检查设备是否支持OpenGL ES 2.0
     * check whether the device support OpenGL ES 2.0
     * </pre>
     *
     * @param context
     * @return
     */
    public static boolean isSupportEs2(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        LogHelper.d(TAG, LogHelper.getThreadName() + " supportsEs2=" + supportsEs2);
        return supportsEs2;
    }

    /**
     * 检查每一步操作是否有错误的方法
     *
     * @param op TAG
     */
    public static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            LogHelper.d("ES20_ERROR", LogHelper.getThreadName() + op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    /**
     * <pre>
     *     判断环境是否支持FBO
     * </pre>
     *
     * @param gl
     * @return
     */
    public static boolean checkIfContextSupportsFrameBufferObject(GL10 gl) {
        return checkIfContextSupportsExtension(gl, "GL_OES_framebuffer_object");
    }

    /**
     * <pre>
     * 判断环境是否支持某种扩展
     * This is not the fastest way to check for an extension, but fine if
     * we are only checking for a few extensions each time a context is created.
     * </pre>
     *
     * @param gl
     * @param extension
     * @return true if the extension is present in the current context.
     */
    public static boolean checkIfContextSupportsExtension(GL10 gl, String extension) {
        String extensions = " " + gl.glGetString(GL10.GL_EXTENSIONS) + " ";
        // The extensions string is padded with spaces between extensions, but not
        // necessarily at the beginning or end. For simplicity, add spaces at the
        // beginning and end of the extensions string and the extension string.
        // This means we can avoid special-case checks for the first or last
        // extension, as well as avoid special-case checks when an extension name
        // is the same as the first part of another extension name.
        return extensions.indexOf(" " + extension + " ") >= 0;
    }
}
