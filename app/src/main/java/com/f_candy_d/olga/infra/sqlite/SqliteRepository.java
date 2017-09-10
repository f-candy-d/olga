package com.f_candy_d.olga.infra.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.SqlRepository;
import com.f_candy_d.olga.infra.sql_utils.SqlCondExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;
import com.f_candy_d.olga.infra.sql_utils.SqlWhere;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by daichi on 17/08/30.
 */

public class SqliteRepository implements SqlRepository {

    private SqliteDatabaseOpenHelper mOpenHelper;

    public SqliteRepository(@NonNull SqliteDatabaseOpenHelper openHelper) {
        mOpenHelper = openHelper;
    }

    @Override
    public long insert(@NonNull SqlEntity entity) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();
        if (entity.getTableName() == null) {
            return -1;
        }

        final long id = sqLiteDatabase.insert(entity.getTableName(), null, entity.getValueMap());
        sqLiteDatabase.close();

        // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
        // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
        return (id != -1) ? id : -1;
    }

    @NonNull
    @Override
    public long[] insert(@NonNull Collection<SqlEntity> entities) {
        final long[] ids = new long[entities.size()];
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();
        int index = 0;

        sqLiteDatabase.beginTransaction();
        try {
            for (SqlEntity entity : entities) {
                final long id = (entity.getTableName() != null)
                        ? sqLiteDatabase.insert(entity.getTableName(), null, entity.getValueMap())
                        : -1;

                // SQLiteDatabase#insert() method returns the row ID of the newly inserted row, or -1 if an error occurred.
                // See document -> https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert(java.lang.String, java.lang.String, android.content.ContentValues)
                ids[index] = (id != -1) ? id : -1;
                ++index;
            }
            sqLiteDatabase.setTransactionSuccessful();

        } finally {
            sqLiteDatabase.endTransaction();
        }
        sqLiteDatabase.close();

        return ids;
    }

    @Override
    public boolean update(@NonNull SqlEntity entity) {
        final long id;
        if (entity.getTableName() == null ||
                !entity.hasColumn(BaseColumns._ID) ||
                (id = entity.getLongOrDefault(BaseColumns._ID, -1)) == -1) {

            return false;
        }

        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();

        // TODO; Check if 'mId' is unique before update a row
        final int affected = sqLiteDatabase.update(entity.getTableName(), entity.getValueMap(), idIs.formalize(), null);
        sqLiteDatabase.close();

        return (affected != 0);
    }

    @Override
    public int updateIf(@NonNull String table, @NonNull SqlEntity entity, @NonNull SqlWhere where) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();
        final int affected = sqLiteDatabase.update(table, entity.getValueMap(), where.formalize(), null);
        sqLiteDatabase.close();

        return affected;
    }

    @Override
    public boolean delete(@NonNull SqlEntity entity) {
        long id;
        if (entity.getTableName() == null ||
                !entity.hasColumn(BaseColumns._ID) ||
                (id = entity.getLongOrDefault(BaseColumns._ID, -1)) == -1) {
            return false;
        }

        return delete(id, entity.getTableName());
    }

    @Override
    public boolean delete(long id, @NonNull String table) {
        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();

        // TODO; Check if 'mId' is unique before delete a row
        final int affected = sqLiteDatabase.delete(table, idIs.formalize(), null);
        sqLiteDatabase.close();

        return (affected != 0);
    }

    @Override
    public int deleteIf(@NonNull String table, @NonNull SqlWhere where) {
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openWritableDatabase();
        final int affected = sqLiteDatabase.delete(table, where.formalize(), null);
        sqLiteDatabase.close();

        return affected;
    }

    @NonNull
    @Override
    public SqlEntity[] select(@NonNull SqlQuery query) {
        ArrayList<SqlEntity> results = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mOpenHelper.openReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                query.distinct(),
                query.tables(),
                query.columns(),
                query.selection(),
                query.selectionArgs(),
                query.groupBy(),
                query.having(),
                query.orderBy(),
                query.limit());

        boolean isEOF = cursor.moveToFirst();
        ContentValues contentValues;
        SqlEntity entity;
        while (isEOF) {
            contentValues = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cursor, contentValues);
            entity = new SqlEntity(contentValues);
            results.add(entity);
            isEOF = cursor.moveToNext();
        }

        cursor.close();
        sqLiteDatabase.close();

        return results.toArray(new SqlEntity[]{});
    }

    @Nullable
    @Override
    public SqlEntity selectRowById(@NonNull String table, long id) {
        SqlCondExpr idIs = new SqlCondExpr(BaseColumns._ID).equalTo(id);
        SqlQuery query = new SqlQuery();
        query.putTables(table);
        query.setSelection(idIs);

        SqlEntity[] results = select(query);
        if (results.length == 1) {
            return results[0];
        }

        return null;
    }
}
