package com.f_candy_d.olga.presentation.view_model;

import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.vvm.FragmentViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by daichi on 9/15/17.
 */

public class HomeSubContentViewModel extends FragmentViewModel {

    /**
     * Find tasks which starts in 24 hours from now.
     */
    public ArrayList<Task> getTasksUpcoming() {
        Calendar date = Calendar.getInstance();
        final long now = date.getTimeInMillis();
        date.add(Calendar.DAY_OF_MONTH, 1);
        final long limit = date.getTimeInMillis();
        ArrayList<Task> tasks = TaskStreamUseCase.getTasksStartInTerm(now, limit);
        sortTasksByDateTermStart(tasks);
        return tasks;
    }

    /**
     * Find tasks which is scheduled for the next 6 days from now.
     */
    public ArrayList<Task> getTasksInFeature() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, 1);
        final long left = date.getTimeInMillis();
        date.add(Calendar.DAY_OF_MONTH, 6);
        final long right = date.getTimeInMillis();
        ArrayList<Task> tasks = TaskStreamUseCase.getTasksStartInTerm(left, right);
        sortTasksByDateTermStart(tasks);
        return tasks;
    }

    /**
     * Find tasks which does expire, but does not finish.
     */
    public ArrayList<Task> getTasksNeedToBeRescheduled() {
        ArrayList<Task> tasks = TaskStreamUseCase.getTasksNeedToBeRescheduled();
        sortTasksByDateTermEnd(tasks);
        return tasks;
    }

    private void sortTasksByDateTermEnd(ArrayList<Task> tasks) {
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return t1.dateTermEnd.compareTo(t2.dateTermEnd);
            }
        };

        Collections.sort(tasks, comparator);
    }

    private void sortTasksByDateTermStart(ArrayList<Task> tasks) {
        Comparator<Task> comparator = new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return t1.dateTermStart.compareTo(t2.dateTermStart);
            }
        };

        Collections.sort(tasks, comparator);
    }
}
