package com.f_candy_d.olga.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.structure.DueDateOption;
import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.domain.usecase.DueDateOptionTableUseCase;
import com.f_candy_d.olga.domain.usecase.SqlTableUseCase;
import com.f_candy_d.olga.domain.usecase.NoteTableUseCase;
import com.f_candy_d.olga.infra.SqlEntity;

import java.util.Calendar;
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
    private static final String FIELD_DUE_DATE_OPTION = "due_date_option";

    private Map<String, SqlEntityObject> mOldFields;
    private Map<String, SqlEntityObject> mNewFields;
    private SaveResultListener mSaveResultListener;

    public NoteFormManager(SaveResultListener listener) {
        init();
        mSaveResultListener = listener;
    }

    public NoteFormManager(SaveResultListener listener, long taskId) {
        initWithTaskId(taskId);
        mSaveResultListener = listener;
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

            SqlEntityObject option;

            // DueDateOption
            option = DueDateOptionTableUseCase.findDueDateOptionById(note.getDueDateOptionId());
            if (option != null) {
                mOldFields.put(FIELD_DUE_DATE_OPTION, option);
            }

            mNewFields = new HashMap<>(mOldFields);

        } else {
            init();
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

    /**
     * region; For DueDateOption
     */

    public void attachDueDateOption() {
        if (!mNewFields.containsKey(FIELD_DUE_DATE_OPTION)) {
            mNewFields.put(FIELD_DUE_DATE_OPTION, new DueDateOption());
        }
    }

    public void dettachDueDateOption() {
        if (mNewFields.containsKey(FIELD_DUE_DATE_OPTION)) {
            mNewFields.remove(FIELD_DUE_DATE_OPTION);
        }
    }

    public boolean hasDueDateOption() {
        return mNewFields.containsKey(FIELD_DUE_DATE_OPTION);
    }

    @NonNull
    public DueDateOption getDueDateOptionData() {
        if (!mNewFields.containsKey(FIELD_DUE_DATE_OPTION)) {
            throw new IllegalStateException("attachDueDateOption() has not been called");
        }
        return (DueDateOption) mNewFields.get(FIELD_DUE_DATE_OPTION);
    }

    public void onInputDueDateOptionDueDate(@NonNull Calendar calendar) {
        if (!mNewFields.containsKey(FIELD_DUE_DATE_OPTION)) {
            throw new IllegalStateException("attachDueDateOption() has not been called");
        }
        ((DueDateOption) mNewFields.get(FIELD_DUE_DATE_OPTION)).setDueDate(calendar);
    }
}
