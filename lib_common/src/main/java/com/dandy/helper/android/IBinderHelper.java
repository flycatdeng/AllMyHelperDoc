package com.dandy.helper.android;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.IBinder;

public class IBinderHelper {
    public static IBinder getServiceIBinder(String descriptor) {
        Object service = null;
        try {
            Method getService = Class.forName("android.os.ServiceManager").getMethod("getService", String.class);
            service = getService.invoke(null, descriptor);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return (IBinder) service;
    }
}
