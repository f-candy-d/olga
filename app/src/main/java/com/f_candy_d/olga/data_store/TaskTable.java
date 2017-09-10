package com.f_candy_d.olga.data_store;

import android.provider.BaseColumns;

import com.f_candy_d.olga.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.olga.infra.sqlite.SqliteTableUtils;

import java.util.Calendar;

/**
 * Created by daichi on 17/09/03.
 */

public class TaskTable implements BaseColumns {

    public static final String TABLE_NAME = "task";

    /**
     * Columns
     */
    public static final String _TITLE = "title";
    public static final String _DATE_TERM_START = "date_term_start";
    public static final String _DATE_TERM_END = "date_term_end";
    public static final String _IS_DONE = "is_done";
    public static final String _DO_THROUGHOUT_TERM = "do_throughout_term";
    public static final String _TYPE = "type";

    /**
     * Table definition
     */
    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK, false)
                .put(_TITLE, SqliteColumnDataType.TEXT, false)
                .put(_DATE_TERM_START, SqliteColumnDataType.INTEGER, false)
                .put(_DATE_TERM_END, SqliteColumnDataType.INTEGER, false)
                .put(_IS_DONE, SqliteColumnDataType.INTEGER, false)
                .put(_DO_THROUGHOUT_TERM, SqliteColumnDataType.INTEGER, false)
                .put(_TYPE, SqliteColumnDataType.INTEGER, false);
    }

    /**
     * Constant values for the column 'type'.
     * These values are must be the consecutive number.
     */

    public static final int TYPE_EVENT = 0;
    public static final int TYPE_REMINDER = 1;
    public static final int TYPE_TODO = 2;

    /**
     * Default values of columns
     */

    public static long defaultId() {
        return DbContract.NULL_ID;
    }

    public static String defaultTitle() {
        return "(No Title)";
    }

    public static long defaultDateTermStart() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long defaultDateTermEnd() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static boolean defaultIsDone() {
        return false;
    }

    public static boolean defaultDoThroughoutTerm() {
        return false;
    }

    public static int defaultType() {
        return -1;
    }

    public enum ValidationErrorCode {
        TITLE_ERROR, // 'title' is not able to be an empty text
        DATE_ERROR, // 'dateTermEnd' must be greater than or equals to 'dateTermStart'
        TYPE_ERROR, // 'type' is not able to be a null
    }

    /**
     * Data validations
     */

    public static ValidationErrorCode isTitleValid(String title) {
        return (title == null) ? ValidationErrorCode.TITLE_ERROR : null;
    }

    /**
     * @param dateTermStart in millis
     * @param dateTermEnd in millis
     */
    public static ValidationErrorCode isDateValid(long dateTermStart, long dateTermEnd) {
        return (dateTermStart > dateTermEnd) ? ValidationErrorCode.DATE_ERROR : null;
    }

    public static ValidationErrorCode isTypeValid(int type) {
        return (type < TYPE_EVENT || TYPE_TODO < type)
                ? ValidationErrorCode.TYPE_ERROR
                : null;
    }
}
