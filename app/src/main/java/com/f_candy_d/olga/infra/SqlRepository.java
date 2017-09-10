package com.f_candy_d.olga.infra;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.infra.sql_utils.SqlQuery;
import com.f_candy_d.olga.infra.sql_utils.SqlWhere;

import java.util.Collection;

/**
 * Created by daichi on 9/10/17.
 */

public interface SqlRepository {

    long NULL_ID = -1;

    /**
     * Insert a row into the database.
     * @param entity Initial column values for the row
     * @return SQLiteDatabase.insert()
     */
    long insert(@NonNull SqlEntity entity);

    /**
     * Insert rows into the database.
     * @param entities Entities witch have initial column values for rows
     * @return An array that contains SQLiteDatabase.insert() or -1
     */
    @NonNull
    long[] insert(@NonNull Collection<SqlEntity> entities);

    /**
     * Update the row of a specific entity in the database.
     * @param entity New column values.
     *               This object must has a pair of the following column & value:
     *               COLUMN   : android.provider.BaseColumns._ID
     *               VALUE    : Row's ID of a specific entity
     *
     * @return True if updating is successful, or false if an error occur
     */
    boolean update(@NonNull SqlEntity entity);

    /**
     * Update rows which meet a passed sql WHERE clause in the database.
     * @param entity New column values
     * @param where Sql WHERE clause
     * @return SQLiteDatabase.update()
     */
    int updateIf(@NonNull String table, @NonNull SqlEntity entity, @NonNull SqlWhere where);

    /**
     * Delete the row of a specific entity in the database.
     * @param entity An entity which will be deleted.
     *               This object must has a pair of the following column & value:
     *               COLUMN  : android.provider.BaseColumns._ID
     *               VALUE   : Row's ID of a specific entity
     *
     * @return True if deleting is successful, false otherwise
     */
    boolean delete(@NonNull SqlEntity entity);

    boolean delete(long id, @NonNull String table);

    /**
     * Delete rows which meet a passed sql WHERE clause in the database.
     * @param where Sql WHERE clause
     * @return SQLiteDatabase.delete()
     */
    int deleteIf(@NonNull String table, @NonNull SqlWhere where);

    /**
     * Find rows for a passed query.
     * @param query Conditions for finding rows
     * @return An array which contains results of finding, or an empty array
     */
    @NonNull
    SqlEntity[] select(@NonNull SqlQuery query);

    /**
     * Find the row for a passed ID.
     * @param table Target table name
     * @param id ID of a specific row
     * @return Null if a specific row is not found
     */
    @Nullable
    SqlEntity selectRowById(@NonNull String table, long id);
}
