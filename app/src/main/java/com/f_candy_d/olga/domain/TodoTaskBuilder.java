package com.f_candy_d.olga.domain;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.domain.structure.Task;

import java.util.Calendar;

/**
 * Created by daichi on 9/20/17.
 */

public class TodoTaskBuilder {

    private Task mTask;

    public TodoTaskBuilder() {
        mTask = new Task();
        // initialization
//        mTask.type = TaskTable.TYPE_TODO;
//        mTask.startDate.set(Calendar.getInstance());
    }

    public TodoTaskBuilder(Task initialData) {
        mTask = new Task(initialData);
    }

    public TodoTaskBuilder title(String title) {
        mTask.setTitle(title);
        return this;
    }

    public TodoTaskBuilder description(String description) {
        mTask.setDescription(description);
        return this;
    }

    public TodoTaskBuilder dueDate(long dueDateInMillis) {
        return dueDate(new InstantDate(dueDateInMillis));
    }

    public TodoTaskBuilder dueDate(Calendar dueDate) {
        return dueDate(new InstantDate(dueDate));
    }

    public TodoTaskBuilder dueDate(InstantDate dueDate) {
//        mTask.endDate = dueDate;
        return this;
    }

    public Task build() {
        return new Task(mTask);
    }
}
