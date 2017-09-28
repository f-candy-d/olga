package com.f_candy_d.olga.data_store;

import android.graphics.Color;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;

import com.f_candy_d.olga.MyApp;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.infra.sqlite.SqliteColumnDataType;
import com.f_candy_d.olga.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 17/09/03.
 */

public class NoteTable implements BaseColumns {

    public static final String TABLE_NAME = "note";

    /**
     * Columns
     */
    public static final String _TITLE = "title";
    public static final String _DESCRIPTION = "description";
    public static final String _IS_ACHIEVED = "is_achieved";
    public static final String _THEME_COLOR = "theme_color";
    public static final String _DUE_DATE_OPTION_ID = "due_date_option_id";

    /**
     * Table definition
     */
    static SqliteTableUtils.TableSource getTableSource() {
        return new SqliteTableUtils.TableSource(TABLE_NAME)
                .put(_ID, SqliteColumnDataType.INTEGER_PK)
                .put(_TITLE, SqliteColumnDataType.TEXT)
                .put(_DESCRIPTION, SqliteColumnDataType.TEXT)
                .put(_IS_ACHIEVED, SqliteColumnDataType.INTEGER)
                .put(_THEME_COLOR, SqliteColumnDataType.INTEGER)
                .put(_DUE_DATE_OPTION_ID, SqliteColumnDataType.INTEGER);
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

    public static int defaultThemeColor() {
        return ContextCompat.getColor(MyApp.getAppContext(), R.color.default_task_theme_color);
    }

    public static long defaultDueDateOptionId() {
        return DbContract.NULL_ID;
    }
}
