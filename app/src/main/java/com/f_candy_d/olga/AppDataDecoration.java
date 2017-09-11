package com.f_candy_d.olga;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class AppDataDecoration {

    public static String formatDatetime(Calendar date, boolean use24HourFormat, Context context) {
        java.text.DateFormat format = DateFormat.getMediumDateFormat(context);
        String dateStr = format.format(date.getTime());
        format = DateFormat.getTimeFormat(context);
        return dateStr + " " + format.format(date.getTime());
    }
}
