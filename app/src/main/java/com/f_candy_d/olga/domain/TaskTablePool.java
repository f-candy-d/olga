package com.f_candy_d.olga.domain;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.domain.filter.DefaultFilterFactory;
import com.f_candy_d.olga.domain.filter.TaskFilter;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/26/17.
 */

public class TaskTablePool extends SqliteTablePool<Task> {

    private TaskFilter mFilter;

    public TaskTablePool(TaskFilter filter) {
        super(TaskTable.TABLE_NAME, Task.class);
        mFilter = filter;
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

