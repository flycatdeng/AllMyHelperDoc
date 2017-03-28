package com.dandy.module.cleanaccelerate;

import java.util.ArrayList;

import android.content.Context;

public class AccelerWhiteListUtil {

    /**
     * <pre>
     * 获得清理白名单包名列表
     * get the list of packages that we won't clean
     * </pre>
     * 
     * @param context
     *            当前应用的context
     * @return
     */
    public static ArrayList<String> getWhitePackageList(Context context, boolean ignoreTopApp) {
        ArrayList<String> whiteList = new ArrayList<String>();
//        if (context != null) {
//            whiteList.add(context.getPackageName());
//        }
        whiteList.addAll(AccelerWhiteListManager.getInstance().getOtherWhiteList());
//        if (ignoreTopApp) {
//            whiteList.add(PackageHelper.getTopActivityPackageName(context));
//        }
        return whiteList;
    }
}
