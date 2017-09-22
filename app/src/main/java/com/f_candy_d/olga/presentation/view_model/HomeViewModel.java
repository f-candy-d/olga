package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;

import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.domain.usecase.TaskStreamUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by daichi on 9/11/17.
 */

public class HomeViewModel extends ActivityViewModel {

    public HomeViewModel(Context context) {
        super(context);
    }

    public Task[] getAllTasks() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.APRIL);
        final long s = c.getTimeInMillis();
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        final long e = c.getTimeInMillis();
        return TaskStreamUseCase.getTasksStartInTerm(s, e);
    }

    /**
     * Find tasks which already starts and does not finish yet.
     */
    public Task[] getTasksInProcess() {
        final Task[] tasks = TaskStreamUseCase.getTasksInProcess();
        sortTasksByDateTermEnd(tasks);
        return tasks;
    }

    /**
     * Find tasks which starts in 24 hours from now.
     */
    public Task[] getTasksUpcoming() {
        Calendar date = Calendar.getInstance();
        final long now = date.getTimeInMillis();
        date.add(Calendar.DAY_OF_MONTH, 1);
        final long limit = date.getTimeInMillis();
        Task[] tasks = TaskStreamUseCase.getTasksStartInTerm(now, limit);
        sortTasksByDateTermStart(tasks);
        return tasks;
    }

    /**
     * Find tasks which is scheduled for the next 6 days from now.
     */
    public Task[] getTasksInFeature() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, 1);
        final long left = date.getTimeInMillis();
        date.add(Calendar.DAY_OF_MONTH, 6);
        final long right = date.getTimeInMillis();
        Task[] tasks = TaskStreamUseCase.getTasksStartInTerm(left, right);
        sortTasksByDateTermStart(tasks);
        return tasks;
    }

    /**
     * Find tasks which does expire, but does not finish.
     */
    public Task[] getTasksNeedToBeRescheduled() {
        Task[] tasks = TaskStreamUseCase.getTasksNeedToBeRescheduled();
        sortTasksByDateTermEnd(tasks);
        return tasks;
    }

    private void sortTasksByDateTermEnd(UnmodifiableTask[] tasks) {
//        Comparator<Task> comparator = new Comparator<Task>() {
//            @Override
//            public int compare(Task t1, Task t2) {
//                return t1.endDate.compareTo(t2.endDate);
//            }
//        };
//
//        Collections.sort(tasks, comparator);
    }

    private void sortTasksByDateTermStart(UnmodifiableTask[] tasks) {
//        Comparator<Task> comparator = new Comparator<Task>() {
//            @Override
//            public int compare(Task t1, Task t2) {
//                return t1.startDate.compareTo(t2.startDate);
//            }
//        };
//
//        Collections.sort(tasks, comparator);
    }
}
