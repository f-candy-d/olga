package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.domain.TodoTaskBuilder;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.usecase.TaskStreamUseCase;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.olga.presentation.fragment.SummaryFormFragment;

/**
 * Created by daichi on 9/20/17.
 */

public class TodoFormActivity extends FormActivity {

    TodoTaskBuilder mBuilder;

    @Override
    protected void onInitWithContentId(long contentId) {
        Task task = TaskStreamUseCase.findTaskById(contentId);
        if (task != null) {
            mBuilder = new TodoTaskBuilder(task);
        } else {
            onInit();
        }
    }

    @Override
    protected void onInit() {
        mBuilder = new TodoTaskBuilder();
    }

    @Override
    protected FormFragment[] getFormFragments() {
        Task task = mBuilder.build();
        return new FormFragment[] {
                SummaryFormFragment.newInstance(task.getTitle(), task.getDescription(), null)
        };
    }

    @Override
    public void onDataInput(Bundle data, String simpleFragmentClassName) {
        if (simpleFragmentClassName.equals(SummaryFormFragment.class.getSimpleName())) {
            mBuilder.title(data.getString(SummaryFormFragment.FIELD_TITLE));
            mBuilder.description(data.getString(SummaryFormFragment.FIELD_DESCRIPTION));
        }
    }

    @Override
    protected void onSave() {
    }

    @NonNull
    @Override
    public Style getStyle() {
        Style style = super.getStyle();
        style.colorPrimary = Utils.getColor(R.color.color_cream_blue);
        style.colorSecondary = Utils.getColor(R.color.color_cream_red_light);
        return style;
    }
}
