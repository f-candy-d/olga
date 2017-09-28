package com.f_candy_d.olga.domain;

import android.content.Context;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.domain.usecase.SqlTableUseCase;
import com.f_candy_d.olga.domain.usecase.NoteTableUseCase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/22/17.
 */

public class NoteFormManager {

    public interface SaveResultListener {
        void onSaveSuccessful(long taskId);
        void onSaveFailed();
    }

    private static final String FIELD_TASK = "task";

    private Map<String, SqlEntityObject> mOldFields;
    private Map<String, SqlEntityObject> mNewFields;
    private SaveResultListener mSaveResultListener;

    public NoteFormManager(Context context) {
        init();
        attachListener(context);
    }

    public NoteFormManager(Context context, long taskId) {
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
        Note note = NoteTableUseCase.findNoteById(taskId);
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
                    + "must implement NoteFormManager.SaveResultListener interface");
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
    public Note getTaskData() {
        return (Note) mNewFields.get(FIELD_TASK);
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
