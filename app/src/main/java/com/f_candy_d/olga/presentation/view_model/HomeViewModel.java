package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.f_candy_d.dutils.Group;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class HomeViewModel extends ActivityViewModel {

    public HomeViewModel(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

    }

    public ArrayList<Group<Task>> getUpcoingTasksAsGroup() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.APRIL);
        final long s = c.getTimeInMillis();
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        final long e = c.getTimeInMillis();
        ArrayList<Task> allTasks = TaskStreamUseCase.getTasksInTerm(s, e);
        ArrayList<Group<Task>> taskGroups = new ArrayList<>();
        taskGroups.add(new Group<>("All Tasks", allTasks));
        return taskGroups;
    }
}
