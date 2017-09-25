package com.f_candy_d.olga.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.SqliteRepository;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/10/17.
 */

public class SqlTableUseCase {


    public static long insert(SqlEntityObject entityObject) {
        SqlEntity entity = entityObject.toSqlEntity(false);
        return Repository.getSqlite().insert(entity);
    }

    public static boolean update(SqlEntityObject entityObject) {
        SqlEntity entity = entityObject.toSqlEntity(true);
        return Repository.getSqlite().update(entity);
    }

    public static boolean delete(SqlEntityObject entityObject) {
        return Repository.getSqlite().delete(entityObject.getId(), entityObject.getTableName());
    }

    @Nullable
    public static SqlEntity findById(long id, @NonNull String table) {
        return Repository.getSqlite().selectRowById(table, id);
    }

    @NonNull
    public static SqlEntity[] query(SqlQuery query) {
        return Repository.getSqlite().select(query);
    }

    public static <T> Map<T, Long> applyDiff(final Map<T, SqlEntityObject> before, final Map<T, SqlEntityObject> after) {
        final Map<T, Long> resultIds = new HashMap<>();
        boolean isSuccessful = Repository.getSqlite().doTransaction(new SqliteRepository.Transaction() {
            @Override
            protected boolean onProcess() {
                SqlEntityObject beforeObj, afterObj;
                boolean isSuccessful = true;
                long resultId;

                for (T key : before.keySet()) {
                    beforeObj = (before.containsKey(key)) ? before.get(key) : null;
                    afterObj = (after.containsKey(key)) ? after.get(key) : null;
                    resultId = Repository.SQLITE_NULL_ID;

                    // Insert
                    if (beforeObj == null && afterObj != null) {
                        resultId = insert(afterObj.toSqlEntity(false));

                    // Delete
                    } else if (beforeObj != null && afterObj == null) {
                        resultId = (delete(beforeObj.getId(), beforeObj.getTableName()))
                                ? beforeObj.getId()
                                : Repository.SQLITE_NULL_ID;

                        // Update
                    } else if (beforeObj != null) {
                        resultId = (update(afterObj.toSqlEntity(true)))
                                ? afterObj.getId()
                                : Repository.SQLITE_NULL_ID;
                    }

                    if (resultId == Repository.SQLITE_NULL_ID) {
                        isSuccessful = false;
                        break;
                    } else {
                        resultIds.put(key, resultId);
                    }
                }

                return isSuccessful;
            }
        });

        return (isSuccessful) ? resultIds : null;
    }
}
