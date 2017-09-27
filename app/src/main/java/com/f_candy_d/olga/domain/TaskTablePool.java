package com.f_candy_d.olga.domain;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.domain.filter.TaskFilter;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlObserver;

/**
 * Created by daichi on 9/26/17.
 */

public class TaskTablePool extends SqliteTablePool<Task> {

    private TaskFilter mFilter;
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

    public TaskTablePool(TaskFilter filter) {
        super(TaskTable.TABLE_NAME, Task.class);
        mFilter = filter;
    }

    public void enableAutoFilter() {
        if (!mIsAutoFilterEnabled) {
            SqlObserver.getInstance().registerChangeListener(mChangeListener, TaskTable.TABLE_NAME);
            mIsAutoFilterEnabled = true;
        }
    }

    public void disableAutoFilter() {
        if (mIsAutoFilterEnabled) {
            SqlObserver.getInstance().unregisterChangeListener(mChangeListener);
        }
    }

    @Override
    Task createEntityObject(SqlEntity entity) {
        return new Task(entity);
    }

    @Override
    boolean areColumnsTheSame(Task oldEntity, Task newEntity) {
        return oldEntity.equals(newEntity);
    }

    public void changeFilter(TaskFilter filter) {
        mFilter = filter;
        applyFilter();
    }

    public TaskFilter getFilter() {
        return mFilter;
    }

    @Override
    boolean isNecessaryToPool(Task entity) {
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
    protected int compareEntites(Task entity1, Task entity2) {
        return -1 * Long.valueOf(entity1.getId()).compareTo(entity2.getId());
    }
}

