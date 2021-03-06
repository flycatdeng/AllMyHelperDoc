package com.dandy.module.simplemusic;

import static android.content.ContentValues.TAG;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.dandy.helper.android.LogHelper;

/**
 * Created by flycatdeng on 2017/3/3.
 * <pre>
 *
 * </pre>
 */

public class MusicPlayerAider {

    private IMusicProgressListener mMusicProgressListener;// 音乐进度监听，不需要的可以不用监听
    private CountDownTimer mCountDownTimer;// 计时器，用于监听进度
    private String mPreSongPath = "";// 上一首歌曲的路劲（用于判断用于要播放的音乐是不是同一首歌）

    public MusicPlayerAider() {

    }

    /**
     * 带音乐进度监听构造器，如果使用的构造器是没有该监听器的则不会创建计时器
     * 
     * @param musicProgressListener
     */
    public MusicPlayerAider(IMusicProgressListener musicProgressListener) {
        mMusicProgressListener = musicProgressListener;
    }

    private MediaPlayer mMediaPlayer = null;

    /**
     * 从assets目录播放音乐
     * 
     * @param context
     *            上下文
     * @param fileName
     *            音乐文件在assets目录的文件
     * @param loop
     *            是否循环播放
     * @param restartIgnorePath
     *            是否重新播放不管要播放的歌曲是否和上一首一样
     */
    public void startMusicFromAssets(final Context context, String fileName, final boolean loop, boolean restartIgnorePath) {
        if (!restartIgnorePath && fileName.equals(mPreSongPath)) {
            return;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context.getAssets().openFd(fileName).getFileDescriptor());// 获取assets文件设置为要播放的
            mPreSongPath = fileName;
        } catch (IOException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " IOException=" + e.getMessage());
            e.printStackTrace();
        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                LogHelper.d(TAG, LogHelper.getThreadName());
                mIsPaused = false;
                mIsStarted = true;
                mMediaPlayer.setLooping(loop);// 设置是否循环
                mMediaPlayer.start();
                setProgress();// 设置进度监听
            }
        });
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " IllegalStateException=" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " IOException2=" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从raw目录播放音乐
     * 
     * @param context
     *            上下文
     * @param resID
     *            音乐文件在raw目录的文件
     * @param loop
     *            是否循环播放
     * @param restartIgnorePath
     *            是否重新播放不管要播放的歌曲是否和上一首一样
     */
    public void startMusicFromRaws(final Context context, int resID, final boolean loop, boolean restartIgnorePath) {
        if (!restartIgnorePath && (resID + "").equals(mPreSongPath)) {
            return;
        }
        LogHelper.d(TAG, LogHelper.getThreadName());
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = MediaPlayer.create(context, resID);
        mPreSongPath = resID + "";
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                LogHelper.d(TAG, LogHelper.getThreadName());
                mIsStarted = true;
                mIsPaused = false;
                mMediaPlayer.setLooping(loop);// 设置是否循环
                mMediaPlayer.start();
                setProgress();// 设置进度监听
            }
        });
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            LogHelper.d(TAG, LogHelper.getThreadName() + " IllegalStateException=" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置进度监听
     */
    private void setProgress() {
        if (mMusicProgressListener == null) {// 如果没有监听器，就不需要计数器了
            return;
        }
        if (mCountDownTimer != null) {
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(mMediaPlayer.getDuration(), 10) {
            @Override
            public void onTick(long l) {
                if (mMusicProgressListener != null) {// 将结果回调
                    mMusicProgressListener.onProgress(mMediaPlayer.getCurrentPosition());
                }
            }

            @Override
            public void onFinish() {

            }
        };
        mCountDownTimer.start();
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
            mIsPaused = true;
        }
    }

    private boolean mIsPaused = false;

    public boolean isPaused() {
        return mIsPaused;
    }

    private boolean mIsStarted = false;

    public boolean isStarted() {
        return mIsStarted;
    }

    /**
     * 继续播放
     */
    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mIsPaused = false;
        }
    }

    /**
     * 结束播放
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mIsPaused = true;
            mIsStarted = false;
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}
