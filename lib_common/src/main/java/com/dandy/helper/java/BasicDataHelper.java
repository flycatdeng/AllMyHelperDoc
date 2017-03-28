package com.dandy.helper.java;

/**
 * 基本数据类型帮助类
 * 
 * @author dengchukun
 * 
 */
public class BasicDataHelper {

    /**
     * 判断一个数是否为偶数
     */
    public static boolean isIntNumEven(int num) {
        if (num % 2 == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个数是否为奇数
     */
    public static boolean isIntNumOdd(int num) {
        if (num % 2 == 0) {
            return false;
        }
        return true;
    }
}
