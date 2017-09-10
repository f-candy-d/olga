package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    public ArrayList<Task> getAllTasks() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.APRIL);
        final long s = c.getTimeInMillis();
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        final long e = c.getTimeInMillis();
        return TaskStreamUseCase.getTasksStartInTerm(s, e);
    }

    public ArrayList<Task> getTasksInProcess() {
        return TaskStreamUseCase.getTasksInProcess();
    }

    public ArrayList<Task> getTasksUpcoming() {
        Calendar limit = Calendar.getInstance();
        limit.add(Calendar.DAY_OF_MONTH, 1);
        return TaskStreamUseCase.getTasksUpcoming(limit);
    }

    public ArrayList<Task> getTasksInFeature() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.DAY_OF_MONTH, 1);
        final long left = date.getTimeInMillis();
        date.add(Calendar.DAY_OF_MONTH, 6);
        final long right = date.getTimeInMillis();
        return TaskStreamUseCase.getTasksStartInTerm(left, right);
    }

    public ArrayList<Task> getTasksNeedToBeRescheduled() {
        return TaskStreamUseCase.getTasksNeedToBeRescheduled();
    }
}
