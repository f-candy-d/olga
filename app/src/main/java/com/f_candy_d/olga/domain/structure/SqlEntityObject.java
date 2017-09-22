package com.f_candy_d.olga.domain.structure;


import android.support.annotation.NonNull;

import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/7/17.
 */

abstract public class SqlEntityObject {

    @NonNull private final String mTableName;
    protected long mId;

    SqlEntityObject(@NonNull String tableName) {
        mTableName = tableName;
    }

    @NonNull
    public String getTableName() {
        return mTableName;
    }

    public long getId() {
        return mId;
    }

    @NonNull
    abstract public SqlEntity toSqlEntity(boolean includeRowId);

    abstract public void constructFromSqlEntity(SqlEntity entity);
}
