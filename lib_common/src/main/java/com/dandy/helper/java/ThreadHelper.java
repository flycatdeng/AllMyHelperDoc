package com.dandy.helper.java;

public class ThreadHelper {
    /**
     * 从堆栈中取得必要方法信息等
     */
    public static String getMethodInfo() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (ObjectHelper.isNull(sts)) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(ThreadHelper.class.getName())) {
                continue;
            }
            return "[Thread-" + Thread.currentThread().getName() + ": " + st.getFileName() + ":" + st.getLineNumber() + " " + st.getMethodName()
                    + "()]";
        }
        return null;
    }
}
