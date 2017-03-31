package com.dandy.helper.android.graphics;

import android.graphics.Paint;

/**
 * <pre>
 *  android graphics paint help class
 * </pre>
 * Created by flycatdeng on 2017/3/31.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class PaintHelper {


    /**
     * <pre>
     *      得到要绘制的文字在某个容器的居中线的Y值
     *      get the mid base line Y of the text that we are going to draw
     * </pre>
     *
     * @param paint           paint对象，已经设置好了text,以及text size等其他属性
     * @param containerHeight 父容器的高
     * @return
     */
    public static float getTextMidBaseline(Paint paint, int containerHeight) {
        float baseline = containerHeight / 2 + paint.getTextSize() / 2 - paint.getFontMetrics().descent;
        return baseline;
    }

    /**
     * <pre>
     *      得到要绘制的文字的居中线的Y值
     *      get the mid base line Y of the text that we are going to draw
     * </pre>
     *
     * @param paint paint对象，已经设置好了text,以及text size等其他属性
     * @return
     */
    public static float getTextMidBaseline(Paint paint) {
        float baseline = paint.getTextSize() / 2 - paint.getFontMetrics().descent;
        return baseline;
    }
}
