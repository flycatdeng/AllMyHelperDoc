package com.dandy.helper.android;

import com.dandy.helper.java.ObjectHelper;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

/**
 * string.xml的一些帮助性质的类
 * 
 * @author dengchukun
 * 
 */
public class StringXmlHelper {
    private static String TAG = ObjectHelper.getClassName(StringXmlHelper.class);

    /**
     * 该方法得到的Spanned主要用于View的setText,从而显示不同的颜色
     * 
     * @param context
     *            :上下文
     * @param stringID
     *            :整体字符串的ID
     * @param args
     *            :位置的参数,个数不限,依据整体字符串需要的个数填写
     * @return 详见demo的testColor()
     */
    public static Spanned getSpannedFromXML(Context context, int stringID, Object... args) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        Spanned source = Html.fromHtml(getStringFromXMLWithArgs(context, stringID, args));
        return source;
    }

    /**
     * 获取string.xml中的字符串,带有未知参数的字符串
     * 
     * @param context
     *            :上下文
     * @param stringID
     *            :整体字符串的ID
     * @param args
     *            :位置的参数,个数不限,依据整体字符串需要的个数填写 如"%1$s一些文字%2$d"表示该处需要第一个参数(%1),该参数为字符串($s),第二个参数为整数($d)
     */
    public static String getStringFromXMLWithArgs(Context context, int stringID, Object... args) {
        LogHelper.d(TAG, LogHelper.getThreadName());
        return String.format(context.getString(stringID), args);
    }

    /**
     * 获取string.xml中的字符串,
     * 
     * @param context
     *            :上下文
     * @param stringID
     *            :整体字符串的ID
     */
    public static String getStringFromXML(Context context, int stringID) {
        return context.getString(stringID);
    }

}
