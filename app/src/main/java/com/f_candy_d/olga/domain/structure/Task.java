package com.f_candy_d.olga.domain.structure;


import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 17/09/03.
 */

public class Task extends UnmodifiableTask {

    public Task() {}

    public Task(UnmodifiableTask task) {
        super(task);
    }

    public Task(SqlEntity entity) {
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
}
