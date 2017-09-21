package com.f_candy_d.olga.domain.structure;


import android.support.annotation.NonNull;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.infra.SqlEntity;

import java.util.Calendar;

/**
 * Created by daichi on 17/09/03.
 */

public class Task extends SqlEntityObject {

    public String title;
    public String description;
    public InstantDate startDate;
    public InstantDate endDate;
    public boolean isDone;
    public int type;

    public Task() {
        super(TaskTable.TABLE_NAME);
        init();
    }

    public Task(Task task) {
        super(TaskTable.TABLE_NAME);

        if (task != null) {
            super.id = task.id;
            this.title = task.title;
            this.description = task.description;
            this.isDone = task.isDone;
            this.type = task.type;

            this.startDate = (task.startDate != null)
                    ? new InstantDate(task.startDate)
                    : null;

            this.endDate = (task.endDate != null)
                    ? new InstantDate(task.endDate)
                    : null;

        }
    }

    public Task(SqlEntity entity) {
        super(TaskTable.TABLE_NAME);
        if (entity != null) {
            constructFromSqlEntity(entity);
        }
    }

    @NonNull
    @Override
    public SqlEntity toSqlEntity(boolean includeRowId) {
        SqlEntity entity = new SqlEntity(TaskTable.TABLE_NAME);

        if (includeRowId) {
            entity.put(TaskTable._ID, super.id);
        }
        entity.put(TaskTable._END_DATE, this.endDate.asCalendar());
        entity.put(TaskTable._START_DATE, this.startDate.asCalendar());
        entity.put(TaskTable._TITLE, this.title);
        entity.put(TaskTable._IS_DONE, this.isDone);
        entity.put(TaskTable._TYPE, this.type);
        entity.put(TaskTable._DESCRIPTION, this.description);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(SqlEntity entity) {
        if (entity == null) {
            return;
        }

        super.id = entity.getLongOrDefault(TaskTable._ID, super.id);
        this.title = entity.getStringOrDefault(TaskTable._TITLE, this.title);
        this.description = entity.getStringOrDefault(TaskTable._DESCRIPTION, this.description);
        this.isDone = entity.getBooleanOrDefault(TaskTable._IS_DONE, this.isDone);
        this.type = entity.getIntOrDefault(TaskTable._TYPE, this.type);

        Calendar date = entity.getCalendarOrDefault(TaskTable._END_DATE, null);
        this.endDate = (date != null) ? new InstantDate(date) : this.endDate;

        date = entity.getCalendarOrDefault(TaskTable._START_DATE, null);
        this.startDate = (date != null) ? new InstantDate(date) : this.startDate;
    }

    public void init() {
        super.id = TaskTable.defaultId();
        this.title = TaskTable.defaultTitle();
        this.description = TaskTable.defaultDescription();
        this.isDone = TaskTable.defaultIsDone();
        this.type = TaskTable.defaultType();
        this.startDate = new InstantDate(TaskTable.defaultStartDate());
        this.endDate = new InstantDate(TaskTable.defaultEndDate());
    }
}
