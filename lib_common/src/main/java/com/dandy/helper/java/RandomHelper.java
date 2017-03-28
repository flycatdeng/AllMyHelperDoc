package com.dandy.helper.java;

import java.util.Random;

/**
 * 随机帮助类
 * 
 * @author dengchukun
 * 
 */
public class RandomHelper {

    /**
     * <pre>
     * 得到一个0到1的随机float数
     * <p style="color:red">这个在for循环中调用的话会呈现一定的规律，因为for的两次执行中先后时间差很小</p>
     * </pre>
     * 
     * @return
     */
    public static float getRandomFloat() {
        return new Random().nextFloat();
    }

    /**
     * 得到range2之间的一个随机float数
     */
    public static float getRandomFloat(float[] range2) {
        if (range2 == null || range2.length != 2) {
            return new Random().nextFloat();
        }
        return getRandomFloat(range2[0], range2[1]);
    }

    /**
     * 得到range2之间的一个随机float数
     */
    public static float getRandomFloat(Random random, float[] range2) {
        if (range2 == null || range2.length != 2) {
            return random.nextFloat();
        }
        return getRandomFloat(random, range2[0], range2[1]);
    }

    /**
     * 得到从from到to之间的一个随机float数
     */
    public static float getRandomFloat(Random random, float from, float to) {
        float big = from > to ? from : to;
        float small = from < to ? from : to;
        float ran = random.nextFloat() * (big - small) + small;// 小->大
        if (to < from) {
            return big + small - ran;// 大->小
        }
        return ran;
    }

    /**
     * 得到从from到to之间的一个随机float数
     */
    public static float getRandomFloat(float from, float to) {
        return getRandomFloat(new Random(), from, to);
    }

    /**
     * 得到从from到to之间的一个随机int数
     */
    public static int getRandomInt(int from, int to) {
        int big = from > to ? from : to;
        int small = from < to ? from : to;
        int ran = new Random().nextInt(big + 1 - small) + small;// 小->大
        if (to < from) {
            return big + small - ran;// 大->小
        }
        return ran;
    }
}
