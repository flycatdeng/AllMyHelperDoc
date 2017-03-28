package com.dandy.module.cleanaccelerate;

import java.util.ArrayList;

import android.content.Context;

public class AccelerWhiteListManager {
    private ArrayList<String> mOtherWhiteList = new ArrayList<String>();

    private AccelerWhiteListManager() {
    }

    private static AccelerWhiteListManager mAccelerWhiteListManager = new AccelerWhiteListManager();

    public static AccelerWhiteListManager getInstance() {
        return mAccelerWhiteListManager;
    }

    /**
     * <pre>
     * 获得除本身应用以外的应用清理白名单
     * </pre>
     * 
     * @return
     */
    public ArrayList<String> getOtherWhiteList() {
        return mOtherWhiteList;
    }

    /**
     * <pre>
     * 添加白名单应用包名
     * 可能用于手动添加
     * </pre>
     * 
     * @param packageName
     */
    public void addWhite(String packageName) {
        if (mOtherWhiteList.contains(packageName)) {
            return;
        }
        mOtherWhiteList.add(packageName);
    }

    /**
     * <pre>
     * 添加白名单应用包名
     * 可能用于手动添加
     * </pre>
     * 
     * @param packageName
     */
    public void addWhites(ArrayList<String> packageNames) {
        mOtherWhiteList.addAll(packageNames);
    }

    /**
     * <pre>
     * 删除白名单
     * </pre>
     * 
     * @param packageName
     */
    public void deleteWhite(String packageName) {
        if (!mOtherWhiteList.contains(packageName)) {
            return;
        }
        mOtherWhiteList.remove(packageName);
    }

    /**
     * <pre>
     * 删除白名单
     * </pre>
     * 
     * @param packageName
     */
    public void deleteWhites(ArrayList<String> packageNames) {
        mOtherWhiteList.removeAll(packageNames);
    }

    /**
     * <pre>
     * 初始化一些白名单，预留接口，可能实际项目中会从文件中获取，或者直接添加几个特定的，或者从网络获取都有可能
     * </pre>
     * 
     * @param context
     */
    public void initWhiteLists(Context context) {
        // TODO
    }
}
