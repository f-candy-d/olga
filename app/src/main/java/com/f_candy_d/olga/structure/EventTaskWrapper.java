package com.f_candy_d.olga.structure;

import android.support.annotation.NonNull;

import com.f_candy_d.dutils.InstantDate;

/**
 * Created by daichi on 9/6/17.
 */

public class EventTaskWrapper extends TaskWrapper {

    public EventTaskWrapper() {
    }

    public EventTaskWrapper(@NonNull Task task) {
        super(task);
    }

    @Override
    void formatTask(@NonNull Task task) {
        task.doThroughoutTerm = true;
        task.type = TaskType.EVENT;
    }

    public void setStartDate(InstantDate date) {
        getTask().dateTermStart = date;
    }

    public InstantDate getStartDate() {
        return getTask().dateTermStart;
    }

    public void setEndDate(InstantDate date) {
        getTask().dateTermEnd = date;
    }

    public InstantDate getEndDate() {
        return getTask().dateTermEnd;
    }

    public void setTitle(String title) {
        getTask().title = title;
    }

    public String getTitle() {
        return getTask().title;
    }

    public void setIsDone(boolean isDone) {
        getTask().isDone = isDone;
    }

    public boolean isDone() {
        return getTask().isDone;
    }
}
