package com.f_candy_d.olga.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlBetweenExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlLogicExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;

import java.util.ArrayList;

/**
 * Created by daichi on 9/10/17.
 */

final public class TaskStreamUseCase extends SqlStreamUseCase {

    @Nullable
    public static Task findTaskById(long id) {
        SqlEntity entity = findById(id, TaskTable.TABLE_NAME);
        if (entity != null) {
            return new Task(entity);
        }
        return null;
    }

    @NonNull
    public static ArrayList<Task> getTasksInTerm(long dateTermStart, long dateTermEnd) {
        SqlBetweenExpr between1 =
                new SqlBetweenExpr(TaskTable._DATE_TERM_START)
                        .setRange(dateTermStart, dateTermEnd)
                        .setRangeBoundaries(false, true);

        SqlBetweenExpr between2 =
                new SqlBetweenExpr(TaskTable._DATE_TERM_END)
                        .setRange(dateTermStart, dateTermEnd)
                        .setRangeBoundaries(false, true);

        SqlLogicExpr where = new SqlLogicExpr(between1).or(between2);

        SqlQuery query = new SqlQuery();
        query.setSelection(where);
        query.putTables(TaskTable.TABLE_NAME);

        SqlEntity[] results = Repository.getSql().select(query);
        ArrayList<Task> tasks = new ArrayList<>(results.length);

        for (SqlEntity entity : results) {
            tasks.add(new Task(entity));
        }

        return tasks;
    }
}
