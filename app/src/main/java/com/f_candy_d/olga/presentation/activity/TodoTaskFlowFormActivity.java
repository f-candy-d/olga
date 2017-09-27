package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.presentation.fragment.FlowFormFragment;
import com.f_candy_d.olga.presentation.fragment.SummaryFlowFormFragment;
import com.f_candy_d.olga.presentation.view_model.TaskFormViewModel;
import com.f_candy_d.vvm.ActivityViewModel;

/**
 * Created by daichi on 9/20/17.
 */

public class TodoTaskFlowFormActivity extends TaskFlowFormActivity implements TaskFormViewModel.SaveResultListener {

    TaskFormViewModel mViewModel;

    @Override
    protected ActivityViewModel onCreateViewModelWithContentId(long contentId) {
        mViewModel = new TaskFormViewModel(this, contentId);
        return mViewModel;
    }

    @Override
    protected ActivityViewModel onCreateViewModelWithNoId() {
        mViewModel = new TaskFormViewModel(this);
        return mViewModel;
    }

    @Override
    protected FlowFormFragment[] getFlowFormFragments() {
        UnmodifiableTask task = mViewModel.getTaskData();
        return new FlowFormFragment[] {
                SummaryFlowFormFragment.newInstance(task.getTitle(), task.getDescription(), null)
        };
    }

    @Override
    public void onDataInput(Bundle data, String simpleFragmentClassName) {
        if (simpleFragmentClassName.equals(SummaryFlowFormFragment.class.getSimpleName())) {
            mViewModel.onInputTaskTitle(data.getString(SummaryFlowFormFragment.FIELD_TITLE));
            mViewModel.onInputTaskDescription(data.getString(SummaryFlowFormFragment.FIELD_DESCRIPTION));
        }
    }

    @Override
    protected void onSave() {
//        mViewModel.onSave();
    }

    @NonNull
    @Override
    public Style getStyle() {
        Style style = super.getStyle();
        style.colorPrimary = Utils.getColor(R.color.color_cream_blue);
        style.colorSecondary = Utils.getColor(R.color.color_cream_red_light);
        return style;
    }

    @Override
    public void onSaveSuccessful(long taskId) {

    }

    @Override
    public void onSaveFailed() {

    }
}