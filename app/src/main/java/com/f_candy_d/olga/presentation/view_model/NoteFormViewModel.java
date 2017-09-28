package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.structure.UnmodifiableNote;
import com.f_candy_d.olga.domain.usecase.SqlTableUseCase;
import com.f_candy_d.olga.domain.usecase.NoteTableUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/22/17.
 */

public class NoteFormViewModel extends ActivityViewModel {

    public interface SaveResultListener {
        void onSaveSuccessful(long taskId);
        void onSaveFailed();
    }

    private static final String FIELD_TASK = "task";

    private Map<String, SqlEntityObject> mOldFields;
    private Map<String, SqlEntityObject> mNewFields;
    private SaveResultListener mSaveResultListener;

    public NoteFormViewModel(Context context) {
        super(context);
        init();
        attachListener(context);
    }

    public NoteFormViewModel(Context context, long taskId) {
        super(context);
        initWithTaskId(taskId);
        attachListener(context);
    }

    private void init() {
        mOldFields = new HashMap<>();
        mOldFields.put(FIELD_TASK, null);
        mNewFields = new HashMap<>();
        mNewFields.put(FIELD_TASK, new Note());
    }

    private void initWithTaskId(long taskId) {
        Note note = NoteTableUseCase.findTaskById(taskId);
        if (note != null) {
            mOldFields = new HashMap<>();
            mOldFields.put(FIELD_TASK, note);

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
                    + "must implement NoteFormViewModel.SaveResultListener interface");
        }
    }

    public void onSave() {
        Map<String, Long> idMap = SqlTableUseCase.applyDiff(mOldFields, mNewFields);
        if (idMap != null) {
            mSaveResultListener.onSaveSuccessful(idMap.get(FIELD_TASK));
        } else {
            mSaveResultListener.onSaveFailed();
        }
    }

    /**
     * region; For Note
     */

    @NonNull
    public UnmodifiableNote getTaskData() {
        return (UnmodifiableNote) mNewFields.get(FIELD_TASK);
    }

    public void onInputTaskTitle(String title) {
        ((Note) mNewFields.get(FIELD_TASK)).setTitle(title);
    }

    public void onInputTaskDescription(String description) {
        ((Note) mNewFields.get(FIELD_TASK)).setDescription(description);
    }

    public void onInputTaskThemeColor(int color) {
        ((Note) mNewFields.get(FIELD_TASK)).setThemeColor(color);
    }
}
