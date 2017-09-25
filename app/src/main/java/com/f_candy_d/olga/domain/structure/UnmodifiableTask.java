package com.f_candy_d.olga.domain.structure;

import android.support.annotation.NonNull;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/22/17.
 */

public class UnmodifiableTask extends SqlEntityObject {

    protected String mTitle;
    protected String mDescription;
    protected boolean mIsAchieved;

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }
    public boolean isAchieved() {
        return mIsAchieved;
    }

    public UnmodifiableTask() {
        super(TaskTable.TABLE_NAME);
        initWithDefaultValue();
    }

    public UnmodifiableTask(UnmodifiableTask task) {
        super(TaskTable.TABLE_NAME);

        if (task != null) {
            super.mId = task.mId;
            this.mTitle = task.mTitle;
            this.mDescription = task.mDescription;
            this.mIsAchieved = task.mIsAchieved;
        }
    }

    public UnmodifiableTask(SqlEntity entity) {
        super(TaskTable.TABLE_NAME);
        if (entity != null) {
            constructFromSqlEntity(entity);
        }
    }

    @NonNull
    @Override
    public SqlEntity toSqlEntity(boolean includeRowId) {
        SqlEntity entity = new SqlEntity(TaskTable.TABLE_NAME);

        if (includeRowId) {
            entity.put(TaskTable._ID, super.mId);
        }
        entity.put(TaskTable._TITLE, this.mTitle);
        entity.put(TaskTable._IS_ACHIEVED, this.mIsAchieved);
        entity.put(TaskTable._DESCRIPTION, this.mDescription);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(SqlEntity entity) {
        if (entity == null) {
            return;
        }

        super.mId = entity.getLongOrDefault(TaskTable._ID, super.mId);
        this.mTitle = entity.getStringOrDefault(TaskTable._TITLE, this.mTitle);
        this.mDescription = entity.getStringOrDefault(TaskTable._DESCRIPTION, this.mDescription);
        this.mIsAchieved = entity.getBooleanOrDefault(TaskTable._IS_ACHIEVED, this.mIsAchieved);
    }

    protected void initWithDefaultValue() {
        super.mId = TaskTable.defaultId();
        this.mTitle = TaskTable.defaultTitle();
        this.mDescription = TaskTable.defaultDescription();
        this.mIsAchieved = TaskTable.defaultIsAchieved();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnmodifiableTask that = (UnmodifiableTask) o;

        if (mIsAchieved != that.mIsAchieved) return false;
        if (mTitle != null ? !mTitle.equals(that.mTitle) : that.mTitle != null) return false;
        return mDescription != null ? mDescription.equals(that.mDescription) : that.mDescription == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mIsAchieved ? 1 : 0);
        return result;
    }
}
