package com.f_candy_d.olga.domain.usecase;

import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.DueDateOptionTable;
import com.f_candy_d.olga.domain.structure.DueDateOption;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/29/17.
 */

public class DueDateOptionTableUseCase extends SqlTableUseCase {

    @Nullable
    public static DueDateOption findDueDateOptionById(long id) {
        SqlEntity entity = findById(id, DueDateOptionTable.TABLE_NAME);
        if (entity != null) {
            return new DueDateOption(entity);
        }
        return null;
    }

    public static boolean delete(long id) {
        return delete(id, DueDateOptionTable.TABLE_NAME);
    }

//    @NonNull
//    public static Note[] findDueDateOptionsByFilter(NoteFilter filter) {
//        SqlEntity[] results = Repository.getSqlite().select(filter.toQuery());
//        Note[] notes = new Note[results.length];
//        for (int i = 0; i < results.length; ++i) {
//            notes[i] = new Note(results[i]);
//        }
//        return notes;
//    }
}
