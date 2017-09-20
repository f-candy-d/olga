package com.f_candy_d.olga.domain.structure;


import android.support.annotation.NonNull;

import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/7/17.
 */

abstract public class SqlEntityObject {

    @NonNull private final String mTableName;
    public long id;

    SqlEntityObject(@NonNull String tableName) {
        mTableName = tableName;
    }

    @NonNull
    public String getTableName() {
        return mTableName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    abstract public SqlEntity toSqlEntity(boolean includeRowId);

    abstract public void constructFromSqlEntity(SqlEntity entity);
}
