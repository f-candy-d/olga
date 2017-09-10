package com.f_candy_d.olga.domain;


import android.support.annotation.NonNull;

import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/7/17.
 */

abstract class SqlEntityObject<E extends Enum<E>> {

    @NonNull private final String mTableName;
    public long id;

    SqlEntityObject(@NonNull String tableName) {
        mTableName = tableName;
    }

    @NonNull
    String getTableName() {
        return mTableName;
    }

    /**
     * @return Error codes
     */
    @NonNull
    abstract public E[] checkValidation();

    public boolean isValid() {
        return (checkValidation().length == 0);
    }

    @NonNull
    abstract SqlEntity toSqlEntity(boolean includeRowId);

    abstract void constructFromSqlEntity(SqlEntity entity);
}
