package com.f_candy_d.olga.domain;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.data_store.TaskTable;

/**
 * Created by daichi on 9/11/17.
 */

public class EventTask extends Task {

    public EventTask() {
        format();
    }

    public EventTask(Task task) {
        super(task);
    }

    public void format() {
        this.type = TaskTable.TYPE_EVENT;
    }

    public void setStartDate(InstantDate date) {
        this.startDate = date;
    }

    public InstantDate getStartDate() {
        return this.startDate;
    }

    public void setEndDate(InstantDate date) {
        this.endDate = date;
    }

    public InstantDate getEndDate() {
        return this.endDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return this.isDone;
    }
}
