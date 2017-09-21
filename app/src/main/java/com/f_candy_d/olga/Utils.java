package com.f_candy_d.olga;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by daichi on 9/21/17.
 */

public class Utils {

    private Utils() {}

    // A method to find height of the status bar
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApp.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApp.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(MyApp.getAppContext(), id);
    }

    public static Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(MyApp.getAppContext(), id);
    }
}
