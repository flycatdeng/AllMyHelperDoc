package com.dandy.module.normalmusic;

/**
 * <pre>
 *
 * </pre>
 * Created by flycatdeng on 2017/4/1.
 * Email:dengchukun@qq.com
 * Wechat:flycatdeng
 */

public class BaseMusicInfo {
    private static final int INT_NO_VALUE = -1;
    private static final String STR_NO_VALUE = "";

    enum SourceType {
        FILE, ASSETS, RAW
    }

    public SourceType sourceType = SourceType.RAW;
    //以下三个必须初始化一个one of them should be initialed
    public int rawId = -1;
    public String assetsName = "";
    public String filePath = "";
    //默认值为unknow
    public String songName = "unknow";
    public String songSinger = "unknow";

    private BaseMusicInfo(Builder builder) {
        this.sourceType = builder.sourceType;
        this.rawId = builder.rawId;
        this.assetsName = builder.assetsName;
        this.filePath = builder.filePath;
        this.songName = builder.songName;
        this.songSinger = builder.songSinger;
    }

    public static class Builder {
        private SourceType sourceType = SourceType.RAW;
        //以下三个必须初始化一个one of them should be initialed
        private int rawId = -1;
        private String assetsName = "";
        private String filePath = "";
        //默认值为unknow
        private String songName = "unknow";
        private String songSinger = "unknow";

        public Builder setSourceType(SourceType sourceType) {
            this.sourceType = sourceType;
            return this;
        }

        public Builder setRawId(int rawId) {
            this.rawId = rawId;
            return this;
        }

        public Builder setAssetsName(String assetsName) {
            this.assetsName = assetsName;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setSongName(String songName) {
            this.songName = songName;
            return this;
        }

        public Builder setSongSinger(String songSinger) {
            this.songSinger = songSinger;
            return this;
        }

        public BaseMusicInfo build() {
            switch (sourceType) {
                case RAW:
                    if (rawId == INT_NO_VALUE) {
                        throw new RuntimeException("SourceType=RAW,you should initial the rawid by call setRawId");
                    }
                    break;
                case ASSETS:
                    if (assetsName.equals(STR_NO_VALUE)) {
                        throw new RuntimeException("SourceType=ASSETS,you should initial the assetsName by call setAssetsName");
                    }
                    break;
                case FILE:
                    if (filePath.equals(STR_NO_VALUE)) {
                        throw new RuntimeException("SourceType=FILE,you should initial the filePath by call setFilePath");
                    }
                    break;
            }
            return new BaseMusicInfo(this);
        }
    }
}
