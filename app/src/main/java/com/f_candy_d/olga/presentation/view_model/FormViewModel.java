package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.domain.usecase.TaskStreamUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

/**
 * Created by daichi on 9/22/17.
 */

public class FormViewModel extends ActivityViewModel {

    public interface SaveResultListener {
        void onSaveSuccessful(long id);
        void onSaveFailed();
    }

    private Task mTask;
    private SaveResultListener mSaveResultListener;

    public FormViewModel(Context context) {
        super(context);
        init();
        attachListener(context);
    }

    public FormViewModel(Context context, long taskId) {
        super(context);
        initWithTaskId(taskId);
        attachListener(context);
    }

    private void init() {
        mTask = new Task();
    }

    private void initWithTaskId(long taskId) {
        mTask = TaskStreamUseCase.findTaskById(taskId);
        if (mTask == null) {
            init();
        }
    }

    private void attachListener(Context context) {
        try {
            mSaveResultListener = (SaveResultListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getName()
                    + "must implement FormViewModel.SaveResultListener interface");
        }
    }

    public void onSave() {
        if (mTask.getId() != DbContract.NULL_ID) {
            // Attempt to update data
            boolean result = TaskStreamUseCase.update(mTask);
            if (result) {
                mSaveResultListener.onSaveSuccessful(mTask.getId());
                return;
            }
        }

        // Attempt to insert data
        long id = TaskStreamUseCase.insert(mTask);
        if (id != DbContract.NULL_ID) {
            mSaveResultListener.onSaveSuccessful(id);
        } else {
            mSaveResultListener.onSaveFailed();
        }
    }

    /**
     * region; For Task
     */

    public UnmodifiableTask getTaskData() {
        return mTask;
    }

    public void onInputTaskTitle(String title) {
        mTask.setTitle(title);
    }

    public void onInputTaskDescription(String description) {
        mTask.setDescription(description);
    }
}
