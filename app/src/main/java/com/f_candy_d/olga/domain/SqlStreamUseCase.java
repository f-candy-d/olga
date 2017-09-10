package com.f_candy_d.olga.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/10/17.
 */

public class SqlStreamUseCase {


    public static long insert(SqlEntityObject entityObject) {
        if (!entityObject.isValid()) {
            return DbContract.NULL_ID;
        }

        SqlEntity entity = entityObject.toSqlEntity(false);
        final long id = Repository.getSql().insert(entity);

        return (id != -1) ? id : DbContract.NULL_ID;
    }

    @Nullable
    public static SqlEntity findById(long id, @NonNull String table) {
        return Repository.getSql().selectRowById(table, id);
    }

    public static boolean update(SqlEntityObject entityObject) {
        if (!entityObject.isValid()) {
            return false;
        }

        SqlEntity entity = entityObject.toSqlEntity(true);
        return Repository.getSql().update(entity);
    }

    public static boolean delete(SqlEntityObject entityObject) {
        return Repository.getSql().delete(entityObject.id, entityObject.getTableName());
    }
}
