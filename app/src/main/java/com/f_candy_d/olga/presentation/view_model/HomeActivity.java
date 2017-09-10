package com.f_candy_d.olga.presentation.view_model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.f_candy_d.dutils.Group;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.olga.presentation.dialog.WhatAddDialogFragment;
import com.f_candy_d.olga.presentation.view_logic.HomeViewLogic;
import com.f_candy_d.vmvl.ActivityViewLogicInterface;
import com.f_candy_d.vmvl.ViewModelActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends ViewModelActivity implements HomeViewModelInterface,
        WhatAddDialogFragment.OnSelectionChosenListener {

    private HomeViewLogicInterface mViewLogic;

    @Override
    protected ActivityViewLogicInterface onCreateViewLogic() {
        mViewLogic = new HomeViewLogic(this);
        return mViewLogic;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, Calendar.APRIL);
        final long s = c.getTimeInMillis();
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        final long e = c.getTimeInMillis();
        ArrayList<Task> allTasks = TaskStreamUseCase.getTasksInTerm(s, e);
        ArrayList<Group<Task>> taskGroups = new ArrayList<>();
        taskGroups.add(new Group<>("All Tasks", allTasks));
        mViewLogic.showInitialUpcomingTasks(taskGroups);
    }

    /**
     * region; WhatAddDialogFragment.OnSelectionChosenListener implementation
     */

    @Override
    public void onSelectionChosen(WhatAddDialogFragment.Selection selection, WhatAddDialogFragment dialogFragment) {
        Log.d("mylog", "selected -> " + selection.toString());
//        dialogFragment.dismiss();
    }

    /**
     * region; HomeViewModelInterface implementation
     */

    @Override
    public void triggerAddNewEvent() {
        WhatAddDialogFragment dialog = new WhatAddDialogFragment();
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
        dialog.show(getSupportFragmentManager(), null);
    }
}
