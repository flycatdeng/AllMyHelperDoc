package com.dandy.helper.android;

import android.graphics.Typeface;

/**
 * 一些参数的帮助类，大多数可以使用ClockUtils类或者Configs类就可以了
 * <p>
 * 这个主要是用于解决utils包和helper包之间的依赖以及时钟与桌面之间的依赖
 * <p>
 * 其实反正这个包就相当于框架层的代码咯！
 * 
 * @author dandy
 * 
 */
public class ConfigHelper {
    /* to judge whether we can use the FlashLight or not */
    private static boolean sCanUseFlashLight = true;
    /*
     * to judge whether we have created a DragWidgetLayer object,we can set its
     * value true in the DragWidgetLayer construct
     */
    private static boolean sIsDragWidgetLayerCreated = false;
    /* to judge whether we are using the widget */
    private static boolean sIsMotionEventOnDragWidgetLayer = false;
    /* to judge whether we can intercept the scroll from launcher */
    private static boolean sCanDragWidgetLayerInterceptScroll = false;
    /* to judge whether we can wave a creative plugin when we click a  DragWidgetLayer if it is supposed to*/
    private static boolean sCanCreativePluginRotaryBodyWave = true;
    /* to judge whether a creative plugin can use gravity sensor  if it is supposed to*/
    private static boolean sCanCreativePluginGravitySensor = true;
    /*default Typeface we use for the text*/
    private static Typeface sDefaultTypeface = null;
    // TODO if we need to print the log,use in LogHelper
    private static boolean sIsLogDebug = true;

    public static boolean isLogDebug() {
        return sIsLogDebug;
    }

    public static void setIsLogDebug(boolean isLogDebug) {
        ConfigHelper.sIsLogDebug = isLogDebug;
    }

    /**
     * get the default Typeface we use for the text
     * <p>
     * we'd better
     * 
     * @return
     */
    public static Typeface getDefaultTypeface() {
        return sDefaultTypeface;
    }

    /**
     * set the default Typeface we use for the text
     * <p>
     * we'd better set the default Typeface when we initialize the DragWidgetLayer
     * 
     * @param defaultTypeface
     */
    public static void setDefaultTypeface(Typeface defaultTypeface) {
        ConfigHelper.sDefaultTypeface = defaultTypeface;
    }

    /**
     * to judge whether we can wave a creative plugin when we click a DragWidgetLayer if it is supposed to
     */
    public static boolean isCanCreativePluginRotaryBodyWave() {
        return sCanCreativePluginRotaryBodyWave;
    }

    /**
     * set whether we can wave a creative plugin when we click a DragWidgetLayer if it is supposed to
     */
    public static void setCanCreativePluginRotaryBodyWave(boolean canCreativePluginRotaryBodyWave) {
        ConfigHelper.sCanCreativePluginRotaryBodyWave = canCreativePluginRotaryBodyWave;
    }

    /**
     * to judge whether a creative plugin can use gravity sensor if it is supposed to
     */
    public static boolean isCanCreativePluginGravitySensor() {
        return sCanCreativePluginGravitySensor;
    }

    /**
     * set whether a creative plugin can use gravity sensor if it is supposed to
     */
    public static void setCanCreativePluginGravitySensor(boolean canCreativePluginGravitySensor) {
        ConfigHelper.sCanCreativePluginGravitySensor = canCreativePluginGravitySensor;
    }

    /**
     * to judge whether we can intercept the scroll from launcher
     * 
     * @return
     */
    public static boolean canDragWidgetLayerInterceptScroll() {
        return sCanDragWidgetLayerInterceptScroll;
    }

    /**
     * set whether we can intercept the scroll from launcher
     * 
     * @param canDragWidgetLayerInterceptScroll
     */
    public static void setCanDragWidgetLayerInterceptScroll(boolean canDragWidgetLayerInterceptScroll) {
        ConfigHelper.sCanDragWidgetLayerInterceptScroll = canDragWidgetLayerInterceptScroll;
    }

    /**
     * to judge whether we have created a DragWidgetLayer object
     * <p>
     * we can set its value true in the DragWidgetLayer construct
     * 
     * @return
     */
    public static boolean isDragWidgetLayerCreated() {
        return sIsDragWidgetLayerCreated;
    }

    /**
     * set whether we have created a DragWidgetLayer object
     * <p>
     * we can set its value true in the DragWidgetLayer construct
     * 
     * @return
     */
    public static void setDragWidgetLayerCreated(boolean isDragWidgetLayerCreated) {
        ConfigHelper.sIsDragWidgetLayerCreated = isDragWidgetLayerCreated;
    }

    /**
     * to judge whether we are using the widget
     * 
     * @return
     */
    public static boolean isMotionEventOnDragWidgetLayer() {
        return sIsMotionEventOnDragWidgetLayer;
    }

    /**
     * set whether we are using the widget
     * 
     * @return
     */
    public static void setMotionEventOnDragWidgetLayer(boolean isMotionEventOnDragWidgetLayer) {
        ConfigHelper.sIsMotionEventOnDragWidgetLayer = isMotionEventOnDragWidgetLayer;
    }

    /**
     * to judge whether we can use the FlashLight or not
     * 
     * @return
     */
    public static boolean isCanUseFlashLight() {
        return sCanUseFlashLight;
    }

    /**
     * set whether we can use the FlashLight or not
     * 
     * @return
     */
    public static void setCanUseFlashLight(boolean canUseFlashLight) {
        sCanUseFlashLight = canUseFlashLight;
    }
}
