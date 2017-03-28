package com.dandy.android;

import com.dandy.helper.android.ExitApplication;
import com.dandy.module.crashhandle.CrashHandleManager;

/**
 * <pre>
 * Notice:
 * 0.如果要使用这个application的话则要将manifest中的application中的name设置为：
 *  android:name="com.dandy.android.BaseApplication"
 * </pre>
 * 
 * <pre>
 * Function:
 * 1.用于完全退出程序，关闭所有页面的类 需要继承ExitApplication
 * 需要在每个activity的oncreate方法中加上ExitApplication.getInstance().addActivity( this)方法, 
 * 退出则调用ExitApplication.getInstance().exit();循环将activity给finish掉
 * </pre>
 * <p>
 * 2.监听全局未处理的报错
 * <p>
 * 
 * 
 * @author dengchukun 2016年11月23日
 */
public class BaseApplication extends ExitApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandleManager crashManager = CrashHandleManager.getInstance();
        if (crashManager.isModuleTurnedOn()) {
            crashManager.init(getApplicationContext());
        }
    }

    /************** the code below is used to exit application with closing all of the Activity ***********/

    // TODO
    /**
     * <pre>
     * 如果你要使用这个application作用于整个应用，就要将这里设置为public，否则编译不过，因为manifest中会找到这个类来new. 
     * If you want to use this class in the hole application ,you should set
     * the constructor public or it won't be compiled successfully
     * </pre>
     */
    public BaseApplication() {
    }
}
