package com.dandy.helper.android;

import android.content.Context;
import android.media.AudioManager;

public class AudioManagerHelper {

    private static AudioManagerHelper sAudioManagerHelper;
    private AudioManager mAudioManager;

    private AudioManagerHelper(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static AudioManagerHelper getInstance(Context context) {
        if (sAudioManagerHelper == null) {
            sAudioManagerHelper = new AudioManagerHelper(context);
        }
        return sAudioManagerHelper;
    }

    public void setNoVoice() {
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
    }

    public void recoverVoice() {
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    /**
     * <pre>
     * 是否正在播放音乐
     * </pre>
     * 
     * @param context
     * @return
     */
    public boolean isPlayMusic(Context context) {
        return mAudioManager.isMusicActive();
    }
}
