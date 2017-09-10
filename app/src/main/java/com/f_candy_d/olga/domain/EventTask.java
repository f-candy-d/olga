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
        this.doThroughoutTerm = true;
        this.type = TaskTable.TYPE_EVENT;
    }

    public void setStartDate(InstantDate date) {
        this.dateTermStart = date;
    }

    public InstantDate getStartDate() {
        return this.dateTermStart;
    }

    public void setEndDate(InstantDate date) {
        this.dateTermEnd = date;
    }

    public InstantDate getEndDate() {
        return this.dateTermEnd;
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
