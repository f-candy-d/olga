package com.f_candy_d.dutils;

import android.graphics.Color;

/**
 * Created by daichi on 9/28/17.
 */

public class ColorUtils {

    private ColorUtils() {}

    public static int manipulateBrightness(int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return Color.argb(a,
                Math.round(r * factor),
                Math.round(g * factor),
                Math.round(b * factor));
    }
}
