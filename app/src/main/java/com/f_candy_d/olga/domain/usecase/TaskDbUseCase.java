package com.f_candy_d.olga.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.domain.filter.TaskFilter;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;

/**
 * Created by daichi on 9/10/17.
 */

final public class TaskDbUseCase extends SqlTableUseCase {

    @Nullable
    public static Task findTaskById(long id) {
        SqlEntity entity = findById(id, TaskTable.TABLE_NAME);
        if (entity != null) {
            return new Task(entity);
        }
        return null;
    }

    @NonNull
    public static Task[] findTasksByFilter(TaskFilter filter) {
        SqlEntity[] results = Repository.getSqlite().select(filter.toQuery());
        Task[] tasks = new Task[results.length];
        for (int i = 0; i < results.length; ++i) {
            tasks[i] = new Task(results[i]);
        }
        return tasks;
    }
}
