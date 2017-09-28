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
    private long mDueDateOptionId;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public boolean isAchieved() {
        return mIsAchieved;
    }

    public void setAchieved(boolean achieved) {
        mIsAchieved = achieved;
    }

    public int getThemeColor() {
        return mThemeColor;
    }

    public void setThemeColor(int themeColor) {
        mThemeColor = themeColor;
    }

    public long getDueDateOptionId() {
        return mDueDateOptionId;
    }

    public void setDueDateOptionId(long dueDateOptionId) {
        mDueDateOptionId = dueDateOptionId;
    }

    public Note() {
        super(NoteTable.TABLE_NAME);
    }

    public Note(SqlEntity entity) {
        super(NoteTable.TABLE_NAME, entity);
    }

    public Note(Note note) {
        super(NoteTable.TABLE_NAME);

        if (note != null) {
            super.mId = note.getId();
            this.mTitle = note.getTitle();
            this.mDescription = note.getDescription();
            this.mIsAchieved = note.isAchieved();
            this.mThemeColor = note.getThemeColor();
            this.mDueDateOptionId = note.getDueDateOptionId();
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
        entity.put(NoteTable._DUE_DATE_OPTION_ID, this.mDueDateOptionId);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(@NonNull SqlEntity entity) {
        super.mId = entity.getLongOrDefault(NoteTable._ID, super.mId);
        this.mTitle = entity.getStringOrDefault(NoteTable._TITLE, this.mTitle);
        this.mDescription = entity.getStringOrDefault(NoteTable._DESCRIPTION, this.mDescription);
        this.mIsAchieved = entity.getBooleanOrDefault(NoteTable._IS_ACHIEVED, this.mIsAchieved);
        this.mThemeColor = entity.getIntOrDefault(NoteTable._THEME_COLOR, this.mThemeColor);
        this.mDueDateOptionId = entity.getLongOrDefault(NoteTable._DUE_DATE_OPTION_ID, this.mDueDateOptionId);
    }

    @Override
    protected void onInitWithDefaultValues() {
        super.mId = NoteTable.defaultId();
        this.mTitle = NoteTable.defaultTitle();
        this.mDescription = NoteTable.defaultDescription();
        this.mIsAchieved = NoteTable.defaultIsAchieved();
        this.mThemeColor = NoteTable.defaultThemeColor();
        this.mDueDateOptionId = NoteTable.defaultDueDateOptionId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (mIsAchieved != note.mIsAchieved) return false;
        if (mThemeColor != note.mThemeColor) return false;
        if (mDueDateOptionId != note.mDueDateOptionId) return false;
        if (mTitle != null ? !mTitle.equals(note.mTitle) : note.mTitle != null) return false;
        return mDescription != null ? mDescription.equals(note.mDescription) : note.mDescription == null;

    }

    @Override
    public int hashCode() {
        int result = mTitle != null ? mTitle.hashCode() : 0;
        result = 31 * result + (mDescription != null ? mDescription.hashCode() : 0);
        result = 31 * result + (mIsAchieved ? 1 : 0);
        result = 31 * result + mThemeColor;
        result = 31 * result + (int) (mDueDateOptionId ^ (mDueDateOptionId >>> 32));
        return result;
    }
}
