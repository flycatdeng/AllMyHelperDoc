package com.dandy.helper.java;

public class TimeHelper {

    public static String parseIntToStringTime(int num) {
        return num < 10 ? ("0" + num) : ("" + num);
    }

    public static float getCurrentSecondAngle() {
        return getSecondAngle(CalendarHelper.getSecond());
    }

    public static float getCurrentMenuteAngle() {
        return getMenuteAngle(CalendarHelper.getMinute());
    }

    public static float getCurrentHourAngle() {
        return getHourAngle(CalendarHelper.getHour());
    }

    public static float getSecondAngle(int second) {
        return second / 60.0f * 360.0f;
    }

    public static float getMenuteAngle(int menute) {
        return menute / 60.0f * 360.0f;
    }

    public static float getHourAngle(int hour) {
        return (hour % 12 + CalendarHelper.getMinute() / 60.0f) / 12.0f * 360.0f;
    }
}
