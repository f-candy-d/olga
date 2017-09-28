package com.f_candy_d.olga.domain.structure;

import android.support.annotation.NonNull;

import com.f_candy_d.olga.data_store.NoteTable;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/22/17.
 */

public class Note extends SqlEntityObject {

    private String mTitle;
    private String mDescription;
    private boolean mIsAchieved;
    private int mThemeColor;

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

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setIsAchieved(boolean isArchived) {
        mIsAchieved = isArchived;
    }

    public void setThemeColor(int themeColor) {
        mThemeColor = themeColor;
    }

    public Note() {
        super(NoteTable.TABLE_NAME);
        initWithDefaultValue();
    }

    public Note(Note task) {
        super(NoteTable.TABLE_NAME);

        if (task != null) {
            super.mId = task.getId();
            this.mTitle = task.getTitle();
            this.mDescription = task.getDescription();
            this.mIsAchieved = task.isAchieved();
            this.mThemeColor = task.getThemeColor();
        }
    }

    public Note(SqlEntity entity) {
        super(NoteTable.TABLE_NAME);
        if (entity != null) {
            constructFromSqlEntity(entity);
        }
    }

    @NonNull
    @Override
    public SqlEntity toSqlEntity(boolean includeRowId) {
        SqlEntity entity = new SqlEntity(NoteTable.TABLE_NAME);

        if (includeRowId) {
            entity.put(NoteTable._ID, super.mId);
        }
        entity.put(NoteTable._TITLE, this.mTitle);
        entity.put(NoteTable._IS_ACHIEVED, this.mIsAchieved);
        entity.put(NoteTable._DESCRIPTION, this.mDescription);
        entity.put(NoteTable._THEME_COLOR, this.mThemeColor);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(SqlEntity entity) {
        if (entity == null) {
            return;
        }

        super.mId = entity.getLongOrDefault(NoteTable._ID, super.mId);
        this.mTitle = entity.getStringOrDefault(NoteTable._TITLE, this.mTitle);
        this.mDescription = entity.getStringOrDefault(NoteTable._DESCRIPTION, this.mDescription);
        this.mIsAchieved = entity.getBooleanOrDefault(NoteTable._IS_ACHIEVED, this.mIsAchieved);
        this.mThemeColor = entity.getIntOrDefault(NoteTable._THEME_COLOR, this.mThemeColor);
    }

    protected void initWithDefaultValue() {
        super.mId = NoteTable.defaultId();
        this.mTitle = NoteTable.defaultTitle();
        this.mDescription = NoteTable.defaultDescription();
        this.mIsAchieved = NoteTable.defaultIsAchieved();
        this.mThemeColor = NoteTable.defaultThemeColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note task = (Note) o;

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
