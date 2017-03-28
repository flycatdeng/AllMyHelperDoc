package com.dandy.module.crashhandle;

import android.content.Context;
import android.os.Looper;

import com.dandy.helper.android.LogHelper;
import com.dandy.module.IModuleInterface;

/**
 * 
 * use the class in an application instance like this:
 * 
 * <pre>
 * MainApplication onCreate():
 * CrashHandleManager crashManager = CrashHandleManager.getInstance();
 * if (crashManager.isModuleTurnedOn()) {
 *     crashManager.init(getApplicationContext());
 * }
 * </pre>
 * 
 * @author dengchukun 2016年11月23日
 */
public class CrashHandleManager implements IModuleInterface, Thread.UncaughtExceptionHandler {

    private static final String TAG = CrashHandleManager.class.getSimpleName();
    private static Context mContext;

    public void init(Context applicationContext) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        mContext = applicationContext;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /************************** instance *****************************/
    private static CrashHandleManager sCrashHandleManager;

    private CrashHandleManager() {
    }

    public static CrashHandleManager getInstance() {
        if (sCrashHandleManager == null) {
            sCrashHandleManager = new CrashHandleManager();
        }
        return sCrashHandleManager;
    }

    /************************** IModuleInterface *****************************/
    /* (non-Javadoc)
     * @see com.dandy.module.IModuleInterface#isModuleTurnedOn()
     */
    @Override
    public boolean isModuleTurnedOn() {
        return true;
    }

    /************************** UncaughtExceptionHandler *****************************/
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        final String errStr = ex.getCause().toString();
        LogHelper.d(TAG, LogHelper.getThreadName() + " errStr=" + errStr);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // 这里的Looper.prepare和Looper.loop方法.网上的说法Toast要想在子线中显示，
                // 就必须在当前线程中存在一个消息队列（Looper）。 具体Handler的操作
                LogHelper.showToast(mContext, TAG + " application error=" + errStr);
//                new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false).setMessage("程序崩溃了...")
//                        .setNeutralButton("我知道了", new OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                System.exit(0);
//                            }
//                        }).create().show();
//                AppHelper.restartApp(mContext);
                Looper.loop();
            }
        }.start();
    }

}
