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
    public static final String _TYPE = "type";

    /**
     * Table definition
     */
    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_DATE_TERM_START, SqliteColumnDataType.INTEGER)
                .put(_DATE_TERM_END, SqliteColumnDataType.INTEGER)
                .put(_IS_DONE, SqliteColumnDataType.INTEGER)
                .put(_TYPE, SqliteColumnDataType.INTEGER);
    }

    /**
     * Constant values for the column 'type'.
     * These values are must be the consecutive number.
     */

    public static final int TYPE_TODO = 0;

    /**
     * Default values of columns
     */

    public static long defaultId() {
        return DbContract.NULL_ID;
    }

    public static String defaultTitle() {
        return null;
    }

    public static long defaultStartDate() {
        // Current date
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long defaultEndDate() {
        // Max date value of Calendar
        return Long.MAX_VALUE;
    }

    public static boolean defaultIsDone() {
        return false;
    }

    public static int defaultType() {
        return -1;
    }
}
