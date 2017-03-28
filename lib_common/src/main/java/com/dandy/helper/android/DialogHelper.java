package com.dandy.helper.android;

import android.app.Dialog;

public class DialogHelper {
    /**
     * 取消对话框,并且致空
     * 
     * @param dialog
     *            :对话框
     */
    public static void dialogCancel(Dialog dialog) {
        try {
            if (null != dialog) {
                dialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dialog = null;
        }

    }
}
