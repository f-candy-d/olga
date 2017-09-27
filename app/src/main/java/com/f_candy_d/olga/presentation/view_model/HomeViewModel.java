package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;

import com.f_candy_d.olga.domain.filter.DefaultFilterFactory;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.domain.usecase.TaskTableUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

/**
 * Created by daichi on 9/11/17.
 */

public class HomeViewModel extends ActivityViewModel {

    public HomeViewModel(Context context) {
        super(context);
    }

    public UnmodifiableTask[] getTasksInProcess() {
        return TaskTableUseCase.findTasksByFilter(DefaultFilterFactory.createNotAchievedFilter());
    }

    public UnmodifiableTask[] getTasksNeedToBeRescheduled() {
        return new UnmodifiableTask[] {};
    }

}
