package com.f_candy_d.dutils;

import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by daichi on 9/9/17.
 */

public class CalendarUtil {

    private CalendarUtil() {}

    /**
     * Check only YYYY/MM/DD
     */
    public static boolean isSameDate(Calendar cal1, Calendar cal2) {
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Check only hh:mm:ss
     */
    public static boolean isSameTime(Calendar cal1, Calendar cal2) {
        return (cal1.get(Calendar.HOUR_OF_DAY) == cal2.get(Calendar.HOUR_OF_DAY) &&
                cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) &&
                cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND));
    }

    public static CharSequence formatDateSimply(Calendar calendar) {
        return DateFormat.format("yyyy-MM-dd", calendar);
    }

    public static CharSequence formatTimeSimply(Calendar calendar, boolean is24HourFormat) {
        if (is24HourFormat) {
            return DateFormat.format("hh:mm", calendar);
        } else {
            return DateFormat.format("hh:mm a", calendar);
        }
    }

    public static CharSequence formatDatetimeSimply(Calendar calendar, boolean is24HourFormat) {
        if (is24HourFormat) {
            return DateFormat.format("yyyy-MM-dd hh:mm", calendar);
        } else {
            return DateFormat.format("yyyy-MM-dd hh:mm a", calendar);
        }
    }

    public static CharSequence formatDatetime(Calendar calendar, boolean is24HourFormat) {
        if (is24HourFormat) {
            return DateFormat.format("yyyy-MM-dd hh:mm:ss", calendar);
        } else {
            return DateFormat.format("yyyy-MM-dd hh:mm:ss a", calendar);
        }
    }

    /**
     * Used in #diff() method as a second parameter
     */
    public static final long DIFF_UNIT_MILLIS = 1;
    public static final long DIFF_UNIT_SECONDS = 1000;
    public static final long DIFF_UNIT_MINUTES = DIFF_UNIT_SECONDS * 60;
    public static final long DIFF_UNIT_HOURS = DIFF_UNIT_MINUTES * 60;
    public static final long DIFF_UNIT_DAYS = DIFF_UNIT_HOURS * 24;

    public static long diff(Calendar c1, Calendar c2, long unit) {
        long diff = c1.getTimeInMillis() - c2.getTimeInMillis();
        return Math.abs(diff / unit);
    }

    public static int diffInMonths(Calendar calendar1, Calendar calendar2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeInMillis(calendar1.getTimeInMillis());
        c2.setTimeInMillis(calendar2.getTimeInMillis());
        c2.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH));
        c2.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
        c2.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
        c2.set(Calendar.SECOND, c1.get(Calendar.SECOND));

        if (c1.after(c2)) {
            Calendar tmp = c2;
            c2 = c1;
            c1 = tmp;
        }

        int count = 0;
        while (c1.before(c2)) {
            ++count;
            c1.add(Calendar.MONTH, 1);
        }

        return count;
    }

    public static int diffInYears(Calendar calendar1, Calendar calendar2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTimeInMillis(calendar1.getTimeInMillis());
        c2.setTimeInMillis(calendar2.getTimeInMillis());
        c2.set(Calendar.MONTH, c1.get(Calendar.MONTH));
        c2.set(Calendar.DAY_OF_MONTH, c1.get(Calendar.DAY_OF_MONTH));
        c2.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
        c2.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
        c2.set(Calendar.SECOND, c1.get(Calendar.SECOND));

        if (c1.after(c2)) {
            Calendar tmp = c2;
            c2 = c1;
            c1 = tmp;
        }

        int count = 0;
        while (c1.before(c2)) {
            ++count;
            c1.add(Calendar.YEAR, 1);
        }

        return count;
    }
}
