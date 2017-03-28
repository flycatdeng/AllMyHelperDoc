package com.dandy.helper.android;

import android.content.Context;

/**
 * get the resource from file R.java dynamically
 * 
 * @author dandy
 * 
 */
public class RResource {

    private static final String TAG = "RResource";

    /**
     * get the id by the ID name
     * 
     * @param context
     * @param rSubClassName
     *            a class name under class R like layout/id/raw...
     * @param idName
     *            the real name that you set in the file
     *            <p>
     *            you will set the id name main if there is a file main.xml under layout
     *            <p>
     *            you'll set the id name text_view is there is a id of a component named text_view in a xml file
     * @return a ID like R.layout.main,R.id.textview etc.
     */
    public static int getIDByNameFromR(Context context, String rSubClassName, String idName) {
        int id = 0;
        String packageName = context.getPackageName();
        LogHelper.d(TAG, LogHelper.getThreadName() + "packageName-" + packageName);
        Class clazz = null;
        try {
            clazz = Class.forName(packageName + ".R");
            Class[] classes = clazz.getClasses();
            Class desireClass = null;
            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(rSubClassName)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(idName).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "ClassNotFoundException-" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "IllegalArgumentException-" + e.getMessage());
            e.printStackTrace();
        } catch (SecurityException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "SecurityException-" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "IllegalAccessException-" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + "NoSuchFieldException-" + e.getMessage());
            e.printStackTrace();
        }
        return id;
    }
}
