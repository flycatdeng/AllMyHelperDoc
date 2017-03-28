package com.dandy.helper.android;

/**
 * activity公共实现的方法
 * 
 * @author flycat
 * 
 */
public interface IActivityCommonFun {

    /**
     * 实例化组件
     */
    public void findViews();

    /**
     * 给个组件设置值，可在findViews之后 也可以在onResume中调用
     */
    public void setValues();

    /**
     * 给各组建设置监听器
     */
    public void setListeners();
}
