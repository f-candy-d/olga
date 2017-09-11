package com.f_candy_d.dutils;

import android.support.annotation.NonNull;

import java.util.Calendar;

/**
 * Created by daichi on 17/09/03.
 *
 * Helper class for {@link java.util.Calendar}
 */

public class InstantDate implements Comparable<InstantDate> {

    // 1 hour = 60 minutes
    private static final int HOUR_IN_MINUTES = 60;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private int mDayOfWeek;
    private int mHourOfDay;
    private int mMinute;
    private int mSecond;

    public InstantDate() {
        set(Calendar.getInstance());
    }

    public InstantDate(Calendar calendar) {
        set(calendar);
    }

    public InstantDate(long timeInMillis) {
        set(timeInMillis);
    }

    public InstantDate(InstantDate instantDate) {
        if (instantDate != null) {
            set(instantDate.asCalendar());

        } else {
            set(Calendar.getInstance());
        }
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDayOfMonth() {
        return mDayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        mDayOfMonth = dayOfMonth;
    }

    public int getDayOfWeek() {
        return mDayOfWeek;
    }

    public int getHourOfDay() {
        return mHourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        mHourOfDay = hourOfDay;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        this.mMinute = minute;
    }

    public int getSecond() {
        return mSecond;
    }

    public void setSecond(int second) {
        this.mSecond = second;
    }

    public void set(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        set(calendar);
    }

    public void set(InstantDate date) {
        set(date.asCalendar());
    }

    public void set(Calendar calendar) {
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        mDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        mHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
        mSecond = calendar.get(Calendar.SECOND);
    }

    public int getTimeOfDaySinceMidnightInMinutes() {
        return mHourOfDay * HOUR_IN_MINUTES + mMinute;
    }

    public Calendar asCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDayOfMonth, mHourOfDay, mMinute, mSecond);
        return calendar;
    }

    public long getTimeInMillis() {
        return asCalendar().getTimeInMillis();
    }

    @Override
    public String toString() {
        return CalendarUtil.formatDatetimeSimply(asCalendar(), false).toString();
    }

    @Override
    public int compareTo(@NonNull InstantDate instantDate) {
        Calendar c1 = asCalendar();
        Calendar c2 = instantDate.asCalendar();

        if (c1.before(c2)) {
            return -1;
        } else if (c1.after(c2)) {
            return 1;
        } else {
            return 0;
        }
    }
}
