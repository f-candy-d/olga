package com.f_candy_d.olga.data_store;

import android.provider.BaseColumns;

import com.f_candy_d.olga.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.olga.infra.sqlite.SqliteTableUtils;

import java.util.Calendar;

/**
 * Created by daichi on 9/29/17.
 */

public class DueDateOptionTable implements BaseColumns {

    public static final String TABLE_NAME = "due_date_option";

    /**
     * Columns
     */

    public static final String _DUE_DATE = "due_date";

    /**
     * Table definition
     */

    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK)
                .put(_DUE_DATE, SqliteColumnDataType.INTEGER);
    }

    /**
     * Default values
     */

    public static long defaultId() {
        return DbContract.NULL_ID;
    }

    public static Calendar defaultDueDate() {
        // Return the current datetime
        return Calendar.getInstance();
    }
}
