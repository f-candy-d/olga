package com.f_candy_d.olga.domain.structure;

import android.support.annotation.NonNull;

import com.f_candy_d.olga.data_store.DueDateOptionTable;
import com.f_candy_d.olga.infra.SqlEntity;

import java.util.Calendar;

/**
 * Created by daichi on 9/29/17.
 */

public class DueDateOption extends SqlEntityObject {

    @NonNull private Calendar mDueDate;

    @NonNull
    public Calendar getDueDate() {
        return mDueDate;
    }

    public void setDueDate(@NonNull Calendar dueDate) {
        if (dueDate == null) {
            throw new NullPointerException("Cannot pass a null Calendar object");
        }
        mDueDate = dueDate;
    }

    public DueDateOption() {
        super(DueDateOptionTable.TABLE_NAME);
    }

    public DueDateOption(SqlEntity entity) {
        super(DueDateOptionTable.TABLE_NAME, entity);
    }

    @NonNull
    @Override
    public SqlEntity toSqlEntity(boolean includeRowId) {
        SqlEntity entity = new SqlEntity(DueDateOptionTable.TABLE_NAME);

        if (includeRowId) {
            entity.put(DueDateOptionTable._ID, super.mId);
        }

        entity.put(DueDateOptionTable._DUE_DATE, this.mDueDate);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(@NonNull SqlEntity entity) {
        super.mId = entity.getLongOrDefault(DueDateOptionTable._ID, super.mId);
        this.mDueDate = entity.getCalendarOrDefault(DueDateOptionTable._DUE_DATE, this.mDueDate);
    }

    @Override
    protected void onInitWithDefaultValues() {
        super.mId = DueDateOptionTable.defaultId();
        this.mDueDate = DueDateOptionTable.defaultDueDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DueDateOption that = (DueDateOption) o;

        return mDueDate.equals(that.mDueDate);

    }

    @Override
    public int hashCode() {
        return mDueDate.hashCode();
    }
}
