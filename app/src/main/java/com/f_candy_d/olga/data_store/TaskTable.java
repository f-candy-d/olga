package com.f_candy_d.olga.data_store;

import android.provider.BaseColumns;

import com.f_candy_d.olga.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.olga.infra.sqlite.SqliteTableUtils;

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
    public static final String _IS_ACHIEVED = "is_achieved";

    /**
     * Table definition
     */
    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_DESCRIPTION, SqliteColumnDataType.TEXT)
                .put(_IS_ACHIEVED, SqliteColumnDataType.INTEGER);
    }

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

    public static boolean defaultIsAchieved() {
        return false;
    }
}
