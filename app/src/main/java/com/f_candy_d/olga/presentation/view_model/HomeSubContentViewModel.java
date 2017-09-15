package com.f_candy_d.olga.presentation.view_model;

import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.vvm.FragmentViewModel;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by daichi on 9/15/17.
 */

public class HomeSubContentViewModel extends FragmentViewModel {

    public ArrayList<Task> getAllTasks() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.APRIL);
        final long s = c.getTimeInMillis();
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        final long e = c.getTimeInMillis();
        return TaskStreamUseCase.getTasksStartInTerm(s, e);
    }

}
