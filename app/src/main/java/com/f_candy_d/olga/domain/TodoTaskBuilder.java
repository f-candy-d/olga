package com.f_candy_d.olga.domain;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.domain.structure.Task;

/**
 * Created by daichi on 9/20/17.
 */

public class TodoTaskBuilder {

    private Task mTask;

    public TodoTaskBuilder() {
        mTask = new Task();
        // initialization
        mTask.type = TaskTable.TYPE_TODO;
        mTask.isDone = false;
    }

    public TodoTaskBuilder(Task initialData) {
        mTask = new Task(initialData);
    }

    public TodoTaskBuilder title(String title) {
        mTask.title = title;
        return this;
    }

    public TodoTaskBuilder startDate(long startDateInMillis) {
        return startDate(new InstantDate(startDateInMillis));
    }

    public TodoTaskBuilder startDate(InstantDate startDate) {
        mTask.startDate = startDate;
        return this;
    }

    public TodoTaskBuilder endDate(long endDateInMillis) {
        return endDate(new InstantDate(endDateInMillis));
    }

    public TodoTaskBuilder endDate(InstantDate endDate) {
        mTask.endDate = endDate;
        return this;
    }

    public Task build() {
        return new Task(mTask);
    }
}
