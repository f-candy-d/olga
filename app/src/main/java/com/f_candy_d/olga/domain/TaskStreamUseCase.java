package com.f_candy_d.olga.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlBetweenExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlCondExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlLogicExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;

import java.util.ArrayList;
import java.util.Calendar;

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
    public static ArrayList<Task> getTasksStartInTerm(long dateTermStart, long dateTermEnd) {
        SqlBetweenExpr between = new SqlBetweenExpr(TaskTable._DATE_TERM_START)
                        .setRange(dateTermStart, dateTermEnd)
                        .setRangeBoundaries(true, false);

        SqlQuery query = new SqlQuery();
        query.setSelection(between);
        query.putTables(TaskTable.TABLE_NAME);

        return selectTasksForQuery(query);
    }

    @NonNull
    public static ArrayList<Task> getTasksInProcess() {
        final long now = Calendar.getInstance().getTimeInMillis();
        SqlCondExpr left = new SqlCondExpr(TaskTable._DATE_TERM_START).lessThanOrEqualTo(now);
        SqlCondExpr right = new SqlCondExpr(TaskTable._DATE_TERM_END).graterThanOrEqualTo(now);
        SqlLogicExpr where = new SqlLogicExpr(left).and(right);

        SqlQuery query = new SqlQuery();
        query.setSelection(where);
        query.putTables(TaskTable.TABLE_NAME);

        return selectTasksForQuery(query);
    }

    @NonNull
    public static ArrayList<Task> getTasksUpcoming(Calendar limitDate) {
        final long now = Calendar.getInstance().getTimeInMillis();
        return getTasksStartInTerm(now, limitDate.getTimeInMillis());
    }

    @NonNull
    public static ArrayList<Task> getTasksNeedToBeRescheduled() {
        final long now = Calendar.getInstance().getTimeInMillis();
        SqlCondExpr where = new SqlCondExpr(TaskTable._DATE_TERM_END).lessThan(now);

        SqlQuery query = new SqlQuery();
        query.putTables(TaskTable.TABLE_NAME);
        query.setSelection(where);

        return selectTasksForQuery(query);
    }

    @NonNull
    private static ArrayList<Task> selectTasksForQuery(SqlQuery query) {
        SqlEntity[] results = Repository.getSql().select(query);
        ArrayList<Task> tasks = new ArrayList<>(results.length);

        for (SqlEntity entity : results) {
            tasks.add(new Task(entity));
        }

        return tasks;
    }
}
