package com.dandy.helper.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * 手机卡的帮助类
 * 
 * @author dengchukun
 * 
 */
public class SIMHelper {
    /**
     * 是否是电话号码
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 这只是提供一个公用的方法,具体区分是哪种还要重构一下
     * 
     * @param context
     *            :activity
     */
    public static boolean isMobile(Context context) {
        boolean isMobile = false;
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        /**
         * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber Identification Number）是区别移动用户的标志，
         * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成， 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
         * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
         */
        String imsi = telManager.getSubscriberId();
        if (imsi != null) {
            // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
            if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                isMobile = true;
                // 中国移动
            } else if (imsi.startsWith("46001")) {
                // 中国联通
            } else if (imsi.startsWith("46003")) {
                // 中国电信
            }
        }
        return isMobile;
    }
}
