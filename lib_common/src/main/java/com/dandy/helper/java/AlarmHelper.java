package com.dandy.helper.java;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 闹钟帮助类 依据秒或者小时分钟设置闹钟的具体时间返回格式为:yyyy-MM-dd HH:mm:ss 依据现在的时间和闹钟的时间得到剩余时间,返回如:0天0时47分5秒,可自定义格式
 * 
 * @author dengchukun
 * 
 */
public class AlarmHelper {

    /**
     * 设置在多少小时多少分钟之后为闹钟时间 返回闹钟的具体时间,格式为:yyyy-MM-dd HH:mm:ss
     * 
     * @param hourMinute
     *            :小时数和分钟数,格式为: mm:ss,如6:40
     */
    public static String getAlarmTime(String hourMinute) {
        String[] time = hourMinute.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        int second = hour * 60 * 60 + minute * 60;
        return getAlarmTime(second);
    }

    /**
     * 设置在多少秒之后为闹钟时间 返回闹钟的具体时间,格式为:yyyy-MM-dd HH:mm:ss
     * 
     * @param second
     *            :多少秒之后为闹钟时间
     */
    public static String getAlarmTime(int second) {
        String format = "yyyy-MM-dd HH:mm:ss";
        Date nowDate = new Date();
        System.out.println(nowDate.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.SECOND, second);
        System.out.println(cal.getTime().toString());
        Date alarmDate = cal.getTime();
        return getFormatDate(format, alarmDate);
    }

    /**
     * 返回剩余时间的字符串,可根据需要更改汉字,换成所需的字符串即可
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return,如:0天0时47分5秒
     */
    public static String remainStrTime(String alarmTime) {
        int remainSecondTime = remainSecondTime(alarmTime);
        StringBuffer result = new StringBuffer();
        result.append(remainDay(alarmTime, remainSecondTime));
        result.append("天");
        result.append(remainHour(alarmTime, remainSecondTime));
        result.append("时");
        result.append(remainMinute(alarmTime, remainSecondTime));
        result.append("分");
        result.append(remainSecond(alarmTime, remainSecondTime));
        result.append("秒");
        return result.toString();
    }

    /**
     * 返回计算日时分秒中的日的数值,如1天12小时30分钟8秒中的1
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    private static int remainDay(String alarmTime, int remainSecond) {
        int result = remainSecond / (24 * 60 * 60);
        return result;
    }

    /**
     * 返回计算日时分秒中的时的数值,如1天12小时30分钟8秒中的12
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    private static int remainHour(String alarmTime, int remainSecond) {
        int result = remainSecond % (24 * 60 * 60) / (60 * 60);
        return result;
    }

    /**
     * 返回计算日时分秒中的分的数值,如1天12小时30分钟8秒中的30
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    private static int remainMinute(String alarmTime, int remainSecond) {
        int result = remainSecond % (24 * 60 * 60) % (60 * 60) / 60;
        return result;
    }

    /**
     * 返回计算日时分秒中的秒的数值,如1天12小时30分钟8秒中的8
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    private static int remainSecond(String alarmTime, int remainSecondTime) {
        int result = remainSecondTime % 60;
        return result;
    }

    /**
     * 返回当前时间距离闹钟还剩下多少秒
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    private static int remainSecondTime(String alarmTime) {
        return (int) (remainLongTime(alarmTime) / 1000);
    }

    /**
     * 返回当前时间距离闹钟还剩下多少毫秒
     * 
     * @param alarmTime
     *            :闹钟的时间,格式为yyyy-MM-dd HH:mm:ss
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private static long remainLongTime(String alarmTime) {
        long result = 0;
        String format = "yyyy-MM-dd HH:mm:ss";
        Date nowDate = new Date();
        String currentTime = getFormatDate(format, nowDate);
        System.out.println("currentTime-" + currentTime);
        System.out.println("alarmTime-" + alarmTime);
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date currentDate = df.parse(currentTime);
            Date alarmDate = df.parse(alarmTime);
            result = alarmDate.getTime() - currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 得到格式化之后的日期时间,如2014年02月20日10:17
     * 
     * @param format
     *            ,特定的格式,自定义如:yyyy年MM月dd日HH:mm
     */
    @SuppressLint("SimpleDateFormat")
    private static String getFormatDate(String format, Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String formattedDate = formatter.format(date);
        return formattedDate;
    }
}
