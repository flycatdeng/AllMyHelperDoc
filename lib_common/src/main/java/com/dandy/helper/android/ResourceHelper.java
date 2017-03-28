package com.dandy.helper.android;

import android.content.Context;

public class ResourceHelper {
    /**
     * 得到某应用R文件中的资源id，这个方法和RResource中的getIDByNameFromR方法类似，不过这个不是通过反射的
     * 
     * @param context
     * @param rSubClassName
     *            资源类名 例如：drawable string等
     * @param name
     *            例如ic_launcher等图片的名称
     * @return
     */
    public static int getIdByIdNameFromR(Context context, String rSubClassName, String name) {
        int id = context.getResources().getIdentifier(name, rSubClassName, context.getPackageName());
        return id;
    }

    public static int getIdByIdNameFromDrawble(Context context, String name) {
        return getIdByIdNameFromR(context, "drawable", name);
    }

    public static int getIdByIdNameFromLayout(Context context, String name) {
        return getIdByIdNameFromR(context, "layout", name);
    }

    public static int getIdByIdNameFromStyleable(Context context, String name) {
        return getIdByIdNameFromR(context, "styleable", name);
    }
}
