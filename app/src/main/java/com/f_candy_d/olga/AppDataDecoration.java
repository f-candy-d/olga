package com.f_candy_d.olga;

import android.content.Context;
import android.text.format.DateFormat;

import com.f_candy_d.dutils.CalendarUtil;

import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class AppDataDecoration {

    public static String formatDatetime(Calendar date, Context context) {
        return formatDate(date, context) + ", " + formatTime(date, context);
    }

    public static String formatDate(Calendar date, Context context) {
        java.text.DateFormat format = DateFormat.getMediumDateFormat(context);
        return format.format(date.getTime());
    }

    public static String formatTime(Calendar time, Context context) {
        java.text.DateFormat format = DateFormat.getTimeFormat(context);
        return format.format(time.getTime());
    }

    public static String formatCalendarDiff(Calendar c1, Calendar c2) {
        final long diff = CalendarUtil.diff(c1, c2, CalendarUtil.DIFF_UNIT_MILLIS);
        long tmp1, tmp2;

        tmp1 = diff / CalendarUtil.DIFF_UNIT_SECONDS;
        if (tmp1 == 0) {
            return String.valueOf(diff) + ((diff == 1) ? " milli" : " millis");
        } else if ((tmp2 = diff / CalendarUtil.DIFF_UNIT_MINUTES) == 0) {
            return String.valueOf(tmp1) + ((tmp1 == 1) ? " second" : " seconds");
        } else if ((tmp1 = diff / CalendarUtil.DIFF_UNIT_HOURS) == 0) {
            return String.valueOf(tmp2) + ((tmp2 == 1) ? " minute" : " minutes");
        } else if ((tmp2 = diff / CalendarUtil.DIFF_UNIT_DAYS) == 0) {
            return String.valueOf(tmp1) + ((tmp1 == 1) ? " hour" : " hours");
        } else if ((tmp1 = CalendarUtil.diffInMonths(c1, c2)) == 0) {
            return String.valueOf(tmp2) + ((tmp2 == 1) ? " day" : " days");
        } else if ((tmp2 = CalendarUtil.diffInYears(c1, c2)) == 0) {
            return String.valueOf(tmp1) + ((tmp1 == 1) ? " month" : " months");
        } else {
            return String.valueOf(tmp2) + ((tmp2 == 1) ? " year" : " years");
        }
    }
}
