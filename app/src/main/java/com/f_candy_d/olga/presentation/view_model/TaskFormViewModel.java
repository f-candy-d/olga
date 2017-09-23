package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.domain.usecase.SqlDbUseCase;
import com.f_candy_d.olga.domain.usecase.TaskDbUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/22/17.
 */

public class TaskFormViewModel extends ActivityViewModel {

    public interface SaveResultListener {
        void onSaveSuccessful(long taskId);
        void onSaveFailed();
    }

    private static final String FIELD_TASK = "task";

    private Map<String, SqlEntityObject> mOldFields;
    private Map<String, SqlEntityObject> mNewFields;
    private SaveResultListener mSaveResultListener;

    public TaskFormViewModel(Context context) {
        super(context);
        init();
        attachListener(context);
    }

    public TaskFormViewModel(Context context, long taskId) {
        super(context);
        initWithTaskId(taskId);
        attachListener(context);
    }

    private void init() {
        mOldFields = new HashMap<>();
        mOldFields.put(FIELD_TASK, null);

        mNewFields = new HashMap<>();
        mNewFields.put(FIELD_TASK, new Task());
    }

    private void initWithTaskId(long taskId) {
        Task task = TaskDbUseCase.findTaskById(taskId);
        if (task != null) {
            mOldFields.put(FIELD_TASK, task);

            mNewFields = new HashMap<>(mOldFields);

        } else {
            init();
        }
    }

    private void attachListener(Context context) {
        try {
            mSaveResultListener = (SaveResultListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getName()
                    + "must implement TaskFormViewModel.SaveResultListener interface");
        }
    }

    public void onSave() {
        Map<String, Long> idMap = SqlDbUseCase.applyDiff(mOldFields, mNewFields);
        if (idMap != null) {
            mSaveResultListener.onSaveSuccessful(idMap.get(FIELD_TASK));
        } else {
            mSaveResultListener.onSaveFailed();
        }
    }

    /**
     * region; For Task
     */

    @NonNull
    public UnmodifiableTask getTaskData() {
        return (UnmodifiableTask) mNewFields.get(FIELD_TASK);
    }

    public void onInputTaskTitle(String title) {
        ((Task) mNewFields.get(FIELD_TASK)).setTitle(title);
    }

    public void onInputTaskDescription(String description) {
        ((Task) mNewFields.get(FIELD_TASK)).setDescription(description);
    }
}
