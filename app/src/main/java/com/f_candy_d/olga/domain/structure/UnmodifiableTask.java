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
    protected int mThemeColor;

    public String getTitle() {
        return mTitle;
    }
    public String getDescription() {
        return mDescription;
    }
    public boolean isAchieved() {
        return mIsAchieved;
    }

    public int getThemeColor() {
        return mThemeColor;
    }

    public UnmodifiableTask() {
        super(TaskTable.TABLE_NAME);
        initWithDefaultValue();
    }

    public UnmodifiableTask(UnmodifiableTask task) {
        super(TaskTable.TABLE_NAME);

        if (task != null) {
            super.mId = task.getId();
            this.mTitle = task.getTitle();
            this.mDescription = task.getDescription();
            this.mIsAchieved = task.isAchieved();
            this.mThemeColor = task.getThemeColor();
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
        entity.put(TaskTable._THEME_COLOR, this.mThemeColor);

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
        this.mThemeColor = entity.getIntOrDefault(TaskTable._THEME_COLOR, this.mThemeColor);
    }

    protected void initWithDefaultValue() {
        super.mId = TaskTable.defaultId();
        this.mTitle = TaskTable.defaultTitle();
        this.mDescription = TaskTable.defaultDescription();
        this.mIsAchieved = TaskTable.defaultIsAchieved();
        this.mThemeColor = TaskTable.defaultThemeColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnmodifiableTask task = (UnmodifiableTask) o;

        if (mIsAchieved != task.mIsAchieved) return false;
        if (mThemeColor != task.mThemeColor) return false;
        if (mTitle != null ? !mTitle.equals(task.mTitle) : task.mTitle != null) return false;
        return mDescription != null ? mDescription.equals(task.mDescription) : task.mDescription == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mIsAchieved ? 1 : 0);
        result = 31 * result + mThemeColor;
        return result;
    }
}
