package com.dandy.gles.engine.extra;

public class Color {

    /**
     * Transparent.
     */
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
    /**
     * Color of Black
     */
    public static final Color BLACK = new Color();
    /**
     * Color of White
     */
    public static final Color WHITE = new Color(255, 255, 255);
    /**
     * Color of Blue
     */
    public static final Color BLUE = new Color(0, 0, 255);
    /**
     * Color of Red
     */
    public static final Color RED = new Color(255, 0, 0);
    /**
     * Color of Green
     */
    public static final Color GREEN = new Color(0, 255, 0);
    /**
     * Color of Yellow
     */
    public static final Color YELLOW = new Color(255, 255, 0);
    /**
     * Color of Cyan
     */
    public static final Color CYAN = new Color(0, 255, 255);
    /**
     * Color of Magenta
     */
    public static final Color MAGENTA = new Color(255, 0, 255);

    /**
     * R setting of RGB
     */
    public int red;
    /**
     * G setting of RGB
     */
    public int green;
    /**
     * B setting of RGB
     */
    public int blue;
    /**
     * Alpha setting
     */
    public int alpha = 255;
    private static final double FACTOR = 0.7;

    /**
     * Initialize color to opaque black by default.
     */
    public Color() {
        // Do nothing by default
    }

    /**
     * <pre>
     * Initialize color with RGB and A value.
     * 初始化对象
     * </pre>
     * 
     * @param r
     *            red argument
     * @param g
     *            green argument
     * @param b
     *            blue argument
     * @param a
     *            alpha argument
     */
    public Color(int r, int g, int b, int a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
    }

    /**
     * <pre>
     * Initialize opaque color with specified R, G, B values.
     * 初始化一个不透明的颜色对象
     * </pre>
     * 
     * @param r
     *            R 0-255
     * @param g
     *            B 0-255
     * @param b
     *            G 0-255
     */
    public Color(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * <pre>
     * Initialize opaque color with RGB combination value.
     * 初始化一个不透明的有RGBA值的颜色对象
     * </pre>
     * 
     * @param rgb
     *            RGB combination value
     */
    public Color(int rgb) {
        setRgb(rgb);
    }

    /**
     * <pre>
     * Get the RGBA combination value.
     * 获得的是一个RGBA移位得到的结果
     * </pre>
     * 
     * @return RGBA combination value
     */
    public int getRgb() {
        return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF) << 0);
    }

    /**
     * <pre>
     * Convert RGB combination value to normal RGB and Alpha value.
     *  将一个RGB int值转换成能得到详细的rgba值的方法
     * </pre>
     * 
     * @param rgb
     *            RGB combination value
     */
    public final void setRgb(int rgb) {
        red = (rgb >> 16) & 0xFF;
        green = (rgb >> 8) & 0xFF;
        blue = (rgb >> 0) & 0xFF;
        alpha = (rgb >> 24) & 0xFF;
    }

    /**
     * <pre>
     * Get the unitized R G B A value
     * 得到单位化的RGBA的值，例如R=255,得到的值是R=1.0f
     * </pre>
     * 
     * @return
     */
    public float[] getUnitizedValue() {
        float[] value = new float[4];
        value[0] = red / (float) 255;
        value[1] = green / (float) 255;
        value[2] = blue / (float) 255;
        value[3] = alpha / (float) 255;
        return value;
    }

    /**
     * <pre>
     * Copy a color setting
     * 复制一个颜色对象
     * </pre>
     * 
     * @return a new color object
     */
    public Color copy() {
        try {
            return (Color) clone();
        } catch (CloneNotSupportedException e) {
            return new Color(red, green, blue, alpha);
        }
    }

    /**
     * Set the red value of the color.
     * 
     * @param r
     *            red argument
     * @return this color object
     */
    public Color red(int r) {
        this.red = r;
        return this;
    }

    /**
     * Set the green value of the color.
     * 
     * @param g
     *            green argument
     * @return this color object
     */
    public Color green(int g) {
        this.green = g;
        return this;
    }

    /**
     * Set the blue value of the color.
     * 
     * @param b
     *            blue argument
     * @return this color object
     */
    public Color blue(int b) {
        this.blue = b;
        return this;
    }

    /**
     * Set the alpha value of the color.
     * 
     * @param a
     *            alpha argument
     * @return this color object
     */
    public Color alpha(int a) {
        this.alpha = a;
        return this;
    }

    /**
     * Set HIS color setting
     * 
     * @param hue
     *            hue argument
     * @param luminance
     *            luminance argument
     * @param saturation
     *            saturation argument
     */
    public void setHls(float hue, float luminance, float saturation) {
        if (saturation == 0) {
            red = (int) (luminance * 255.0f + 0.5f);
            green = red;
            blue = red;
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) Math.floor(h);
            float p = luminance * (1.0f - saturation);
            float q = luminance * (1.0f - saturation * f);
            float t = luminance * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    red = (int) (luminance * 255.0f + 0.5f);
                    green = (int) (t * 255.0f + 0.5f);
                    blue = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    red = (int) (q * 255.0f + 0.5f);
                    green = (int) (luminance * 255.0f + 0.5f);
                    blue = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    red = (int) (p * 255.0f + 0.5f);
                    green = (int) (luminance * 255.0f + 0.5f);
                    blue = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    red = (int) (p * 255.0f + 0.5f);
                    green = (int) (q * 255.0f + 0.5f);
                    blue = (int) (luminance * 255.0f + 0.5f);
                    break;
                case 4:
                    red = (int) (t * 255.0f + 0.5f);
                    green = (int) (p * 255.0f + 0.5f);
                    blue = (int) (luminance * 255.0f + 0.5f);
                    break;
                case 5:
                    red = (int) (luminance * 255.0f + 0.5f);
                    green = (int) (p * 255.0f + 0.5f);
                    blue = (int) (q * 255.0f + 0.5f);
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * Make the color setting of this color object darker
     * 
     * @return a new color object
     */
    public Color darker() {
        return new Color(Math.max((int) (red * FACTOR), 0), Math.max((int) (green * FACTOR), 0), Math.max((int) (blue * FACTOR), 0));
    }

    /**
     * Make the color setting of this color object brighter
     * 
     * @return a new color object
     */
    public Color brighter() {
        int r = red;
        int g = green;
        int b = blue;

        /*
         * From 2D group: 1. black.brighter() should return grey 2. applying
         * brighter to blue will always return blue, brighter 3. non pure color
         * (non zero rgb) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - FACTOR));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i);
        }
        if (r > 0 && r < i)
            r = i;
        if (g > 0 && g < i)
            g = i;
        if (b > 0 && b < i)
            b = i;

        return new Color(Math.min((int) (r / FACTOR), 255), Math.min((int) (g / FACTOR), 255), Math.min((int) (b / FACTOR), 255));
    }

    /**
     * Check if the input object is equal to this color object or compare their property if they are different objects.
     * 
     * @param o
     *            the object to be compared
     * @return true if the object is the same as this color object
     */
    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Color) {
            Color c = (Color) o;
            return this.red == c.red && this.green == c.green && this.blue == c.blue && this.alpha == c.alpha;
        }
        return false;
    }

    /**
     * Create a new hash code.
     * 
     * @return a new hash code
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + red;
        result = 37 * result + green;
        result = 37 * result + blue;
        result = 37 * result + alpha;
        return result;
    }

    /**
     * Return the color setting of this color object.
     * 
     * @return a string of color setting
     */
    public String toString() {
        return "Color:[" + red + "," + green + "," + blue + "," + alpha + "]";
    }
}
