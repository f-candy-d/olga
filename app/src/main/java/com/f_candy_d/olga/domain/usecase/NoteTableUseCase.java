package com.f_candy_d.olga.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.NoteTable;
import com.f_candy_d.olga.domain.filter.NoteFilter;
import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/10/17.
 */

final public class NoteTableUseCase extends SqlTableUseCase {

    @Nullable
    public static Note findTaskById(long id) {
        SqlEntity entity = findById(id, NoteTable.TABLE_NAME);
        if (entity != null) {
            return new Note(entity);
        }
        return null;
    }

    public static boolean delete(long id) {
        return delete(id, NoteTable.TABLE_NAME);
    }

    @NonNull
    public static Note[] findTasksByFilter(NoteFilter filter) {
        SqlEntity[] results = Repository.getSqlite().select(filter.toQuery());
        Note[] notes = new Note[results.length];
        for (int i = 0; i < results.length; ++i) {
            notes[i] = new Note(results[i]);
        }
        return notes;
    }
}
