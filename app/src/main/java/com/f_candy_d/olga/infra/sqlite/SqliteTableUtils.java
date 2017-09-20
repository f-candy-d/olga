package com.f_candy_d.olga.infra.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by daichi on 17/08/30.
 */

public class SqliteTableUtils {

    public static class TableSource {
        // key -> columnName | value -> dataType
        @NonNull private Map<String, SqliteColumnDataType> mPairMap;
        private String mTableName;

        public TableSource(String tableName) {
            this(tableName, -1);
        }

        public TableSource(String tableName, int capacity) {
            if (capacity < 0) {
                mPairMap = new HashMap<>();
            } else {
                mPairMap = new HashMap<>(capacity);
            }
            mTableName = tableName;
        }

        public TableSource put(@NonNull String column, @NonNull SqliteColumnDataType dataType) {
            mPairMap.put(column, dataType);
            return this;
        }

        public SqliteColumnDataType getDataType(@NonNull String column) {
            return mPairMap.get(column);
        }

        @NonNull
        public String getTableName() {
            return mTableName;
        }

        public void setTableName(String tableName) {
            mTableName = tableName;
        }

        public Set<String> getAllColumns() {
            return mPairMap.keySet();
        }
    }

    private SqliteTableUtils() {}

    public static boolean createTable(@NonNull SQLiteDatabase database, @NonNull TableSource source) {
        if (!database.isOpen() || database.isReadOnly()) {
            return false;
        }

        final String comma_sep = ",";
        String sqlCreate = "CREATE TABLE " + source.getTableName() + " (";
        Set<String> columns = source.getAllColumns();
        String[] tokens = new String[columns.size()];
        int i = 0;

        for (String column : columns) {
            tokens[i] = column + " " + source.getDataType(column).toString();
            ++i;
        }

        sqlCreate = sqlCreate.concat(TextUtils.join(comma_sep, tokens));
        sqlCreate = sqlCreate.concat(");");

        database.execSQL(sqlCreate);
        return true;
    }

    public static boolean deleteTable(@NonNull SQLiteDatabase sqLiteDatabase, @NonNull String tableName) {
        if (!sqLiteDatabase.isOpen() || sqLiteDatabase.isReadOnly() || tableName == null) {
            return false;
        }

        final String sqlDropTable = "DROP TABLE IF EXISTS ";
        sqLiteDatabase.execSQL(sqlDropTable + tableName);
        return true;
    }

    public static boolean resetTable(@NonNull SQLiteDatabase sqLiteDatabase,
                                     @NonNull TableSource... sources) {
        if (!sqLiteDatabase.isOpen() || sqLiteDatabase.isReadOnly()) {
            return false;
        }

        for (TableSource source : sources) {
            deleteTable(sqLiteDatabase, source.getTableName());
            createTable(sqLiteDatabase, source);
        }

        return true;
    }
}