package com.f_candy_d.olga.domain.table_pool;

import com.f_candy_d.olga.data_store.NoteTable;
import com.f_candy_d.olga.domain.filter.NoteFilter;
import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlObserver;

/**
 * Created by daichi on 9/26/17.
 */

public class NoteTablePool extends SqliteTablePool<Note> {

    private NoteFilter mFilter;
    private boolean mIsAutoFilterEnabled = false;

    private final SqlObserver.ChangeListener mChangeListener = new SqlObserver.ChangeListener() {
        @Override
        public void onRowInserted(String table, long id) {
            if (mFilter != null && mFilter.isMutch(id)) {
                pool(id);
            }
        }

        @Override
        public void onRowUpdated(String table, long id) {
            if (mFilter != null && mFilter.isMutch(id)) {
                pool(id);
            } else {
                int index = indexOf(id);
                if (index != INVALID_POSITION) {
                    releaseAt(index);
                }
            }
        }

        @Override
        public void onRowDeleted(String table, long id) {
            int index = indexOf(id);
            if (index != INVALID_POSITION) {
                releaseAt(index);
            }
        }
    };

    public NoteTablePool(NoteFilter filter) {
        super(NoteTable.TABLE_NAME, Note.class);
        mFilter = filter;
    }

    public void enableAutoFilter() {
        if (!mIsAutoFilterEnabled) {
            SqlObserver.getInstance().registerChangeListener(mChangeListener, NoteTable.TABLE_NAME);
            mIsAutoFilterEnabled = true;
        }
    }

    public void disableAutoFilter() {
        if (mIsAutoFilterEnabled) {
            SqlObserver.getInstance().unregisterChangeListener(mChangeListener);
        }
    }

    @Override
    Note createEntityObject(SqlEntity entity) {
        return new Note(entity);
    }

    @Override
    boolean areColumnsTheSame(Note oldEntity, Note newEntity) {
        return oldEntity.equals(newEntity);
    }

    public void changeFilter(NoteFilter filter) {
        mFilter = filter;
        applyFilter();
    }

    public NoteFilter getFilter() {
        return mFilter;
    }

    @Override
    boolean isNecessaryToPool(Note entity) {
        return (mFilter != null && mFilter.isMutch(entity.getId()));
    }

    public void applyFilter() {
        if (mFilter != null) {
            swapPoolByQuery(mFilter.toQuery());
        } else {
            throw new IllegalStateException("Set a filter!");
        }
    }

    @Override
    protected int compareEntites(Note entity1, Note entity2) {
        return -1 * Long.valueOf(entity1.getId()).compareTo(entity2.getId());
    }
}

