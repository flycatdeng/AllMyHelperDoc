package com.dandy.helper.java;

/**
 * 日期时间帮助类，主要为String类型的数据
 * 
 * @author dengchukun
 * 
 */
public class CalendarUtil {

    private static final String TAG = "CalendarUtil";
    private static String[] WEEK_ZHOU = { "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private static String[] WEEK_XINGQI = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};

    /**
     * 得到周几
     * 
     * @return ”周一“格式
     */
    public static String getWeekNamedByZhou() {
        return WEEK_ZHOU[CalendarHelper.getWeek() - 1];
    }

    /**
     * 得到星期几
     * 
     * @return ”星期一“格式
     */
    public static String getWeekNamedByXingQi() {
        return WEEK_XINGQI[CalendarHelper.getWeek() - 1];
    }

    public static String getHour() {
        int hour = CalendarHelper.getHour();
        String hourStr = "" + hour;
        if (hour < 10) {
            hourStr = "0" + hour;
        }
        return hourStr;
    }

    public static String getMinute() {
        int minute = CalendarHelper.getMinute();
        String minuteStr = "" + minute;
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        return minuteStr;
    }

    public static String getDay() {
        int day = CalendarHelper.getDay();
        String dayStr = "" + day;
        if (day < 10) {
            dayStr = "0" + day;
        }
        return dayStr;
    }

    /**
     * 得到当前是上午还是下午，返回字符串为AM或PM
     * 
     * @return
     */
    public static String getAMPM() {
        int amPm = CalendarHelper.getAMPM();
        String amOrPm = "";
        if (amPm == 0) {
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
        }
        return amOrPm;
    }

    /**
     * 得到当前是上午还是下午，返回字符串为上午或下午
     * 
     * @return
     */
    public static String getShangXiaWu() {
        int amPm = CalendarHelper.getAMPM();
        String amOrPm = "";
        if (amPm == 0) {// AM
            amOrPm = "上午";
        } else {
            amOrPm = "下午";
        }
        LogHelper.d(TAG, LogHelper.getThreadName() + " ShangXiaWu-" + amOrPm);
        return amOrPm;
    }

    public static String getDate() {
        return CalendarHelper.getFormatDate("yyyy.MM.dd");
    }

    public static String getDate1() {
        return CalendarHelper.getFormatDate("yyyy/MM/dd");
    }
}
