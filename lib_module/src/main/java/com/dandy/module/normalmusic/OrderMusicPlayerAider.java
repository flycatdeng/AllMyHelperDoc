package com.dandy.module.normalmusic;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/1.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class OrderMusicPlayerAider extends MusicPlayerAider {
    private ArrayList<BaseMusicInfo> mMusicLists = new ArrayList<BaseMusicInfo>();
    private OrderType mOrderType = OrderType.LOOP_GROUP;

    enum OrderType {
        LOOP_ONE, LOOP_GROUP, RANDOM
    }

    public void setOrderType(OrderType orderType) {
        mOrderType = orderType;
    }

    public void add(BaseMusicInfo musicInfo) {
        mMusicLists.add(musicInfo);
    }

    public void add(int index, BaseMusicInfo musicInfo) {
        mMusicLists.add(index, musicInfo);
    }

    public void startNext(Context context) {
        if (mCurMusicInfo != null) {
            int curIndex = mMusicLists.indexOf(mCurMusicInfo);
            int nextIndex = getNextIndex(curIndex);
            startMusic(context, mMusicLists.get(nextIndex), true, false);
        }
    }

    public void startPre(Context context) {
        if (mCurMusicInfo != null) {
            int curIndex = mMusicLists.indexOf(mCurMusicInfo);
            int nextIndex = getPreIndex(curIndex);
            startMusic(context, mMusicLists.get(nextIndex), true, false);
        }
    }

    private int getNextIndex(int curIndex) {
        if (mOrderType.equals(OrderType.LOOP_ONE)) {
            return curIndex;
        }
        if (mOrderType.equals(OrderType.RANDOM)) {
            return new Random(mMusicLists.size()).nextInt();
        }
        if (mOrderType.equals(OrderType.LOOP_GROUP)) {
            if (curIndex == mMusicLists.size() - 1) {
                return 0;
            } else {
                return curIndex + 1;
            }
        }
        return 0;
    }

    private int getPreIndex(int curIndex) {
        if (mOrderType.equals(OrderType.LOOP_ONE)) {
            return curIndex;
        }
        if (mOrderType.equals(OrderType.RANDOM)) {
            return new Random(mMusicLists.size()).nextInt();
        }
        if (mOrderType.equals(OrderType.LOOP_GROUP)) {
            if (curIndex == 0) {
                return mMusicLists.size() - 1;
            } else {
                return curIndex - 1;
            }
        }
        return 0;
    }
}
