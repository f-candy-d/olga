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
    public static final String _DESCRIPTION = "description";
    public static final String _START_DATE = "start_date";
    public static final String _END_DATE = "end_date";
    public static final String _IS_DONE = "is_done";
    public static final String _TYPE = "type";

    /**
     * Table definition
     */
    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_DESCRIPTION, SqliteColumnDataType.TEXT)
                .put(_START_DATE, SqliteColumnDataType.INTEGER)
                .put(_END_DATE, SqliteColumnDataType.INTEGER)
                .put(_IS_DONE, SqliteColumnDataType.INTEGER)
                .put(_TYPE, SqliteColumnDataType.INTEGER);
    }

    /**
     * Constant values for the column 'type'.
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

    public static String defaultDescription() {
        return null;
    }

    public static Calendar defaultStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        return calendar;
    }

    public static Calendar defaultEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.MAX_VALUE);
        return calendar;
    }

    public static boolean defaultIsDone() {
        return false;
    }

    public static int defaultType() {
        return -1;
    }
}
