package com.dandy.gles.engine.extra;

/**
 * A class that responsible for storing image source reference and information.
 */
public class ImageSource {
    public static final int FILE = 1;
    public static final int BITMAP = 2;
    public static final int RES_ID = 3;
    public static final int BITMAP_GENERATOR = 4;
    public static final int VIDEO_TEXTURE = 5;
    public static final int ASSET = 6;

    /**
     * Initialize this class with image source type and its data
     * @param srcType  image source type
     * @param srcInfo   image data
     */
    public ImageSource(int srcType, Object srcInfo) {
        this(srcType, srcInfo, 0);
    }

    /**
     *  Initialize this class with image source type, its data, and the option variable
     * @param srcType   image source type
     * @param srcInfo   image data
     * @param options  option variable
     */
    public ImageSource(int srcType, Object srcInfo, int options) {
        this.srcType = srcType;
        this.srcInfo = srcInfo;
        this.options = options;
    }

    public int srcType;
    public Object srcInfo;

    public static final int RECYCLE_AFTER_USE = 1 << 0;     // only valid for bitmap source
    public int options;

    /**
     * Convert the image source property to string for output
     * @return   output string
     */
    @Override
    public String toString() {
        return String.format("ImageSource: {type:%d, info:%s}", srcType, srcInfo);
    }
}
