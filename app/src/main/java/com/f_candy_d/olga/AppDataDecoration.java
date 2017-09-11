package com.f_candy_d.olga;

import android.text.format.DateFormat;

import com.f_candy_d.dutils.CalendarUtil;

import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class AppDataDecoration {

    public static String formatDatetime(Calendar date, boolean use24HourFormat) {
        return formatDate(date) + " at " + formatTime(date, use24HourFormat);
    }

    public static String formatDate(Calendar date) {
        return DateFormat.format("MMM. dd, yyyy", date).toString();
    }

    public static String formatTime(Calendar time, boolean use24HourFormat) {
        if (use24HourFormat) {
            return DateFormat.format("hh:mm", time).toString();
        } else {
            return DateFormat.format("hh:mm a", time).toString();
        }
    }

    public static String formatDateShortly(Calendar date) {
        return DateFormat.format("MMM. dd", date).toString();
    }

    public static String formatDatetimeShortly(Calendar date, boolean use24HourFormat) {
        return formatDateShortly(date) + " at " + formatTime(date, use24HourFormat);
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
