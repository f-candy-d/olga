package com.f_candy_d.olga.domain.structure;


import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 17/09/03.
 */

public class Note extends UnmodifiableNote {

    public Note() {}

    public Note(UnmodifiableNote task) {
        super(task);
    }

    public Note(SqlEntity entity) {
        super(entity);
    }

    public void setId(long id) {
        super.mId = id;
    }

    public void setTitle(String title) {
        super.mTitle = title;
    }

    public void setDescription(String description) {
        super.mDescription = description;
    }

    public void setIsAchieved(boolean isArchived) {
        super.mIsAchieved = isArchived;
    }

    public void setThemeColor(int themeColor) {
        super.mThemeColor = themeColor;
    }
}
