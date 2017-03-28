package com.dandy.helper.android;

import android.content.Context;
import android.view.KeyEvent;

/**
 * 
 * @author dengchukun 2016年11月26日
 */
public class BackExitApplicationHelper {
    private static final String TAG = BackExitApplicationHelper.class.getSimpleName();
    private static long sLastClickBackTime = 0L;
    private static final long BACK_EXIT_DELTA_TIME = 2000;

    public static enum ExitType {
        TOAST
    }

    /**
     * <pre>
     * 按返回键退出程序，用户可选择是toast显示还是其他方式提示用户，该方法需要在主Activity的onKeyDown中使用，而且如果返回true则拦截调用地方的返回值
     * </pre>
     * 
     * @param context
     * @param keyCode
     * @param event
     * @param type
     * @return
     */
    public static boolean onBackExitKeyDown(Context context, int keyCode, KeyEvent event, ExitType type) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (type.equals(ExitType.TOAST)) {
                return onBackExitByToast(context, keyCode, event);
            }
        }
        return false;
    }

    private static boolean onBackExitByToast(Context context, int keyCode, KeyEvent event) {
        if ((System.currentTimeMillis() - sLastClickBackTime) > BACK_EXIT_DELTA_TIME) {
            LogHelper.showToast(context, "再按一次退出程序");
            sLastClickBackTime = System.currentTimeMillis();
        } else {
            LogHelper.d(TAG, LogHelper.getThreadName());
            ExitApplication.getInstance().exit();
        }
        return true;
    }
}
