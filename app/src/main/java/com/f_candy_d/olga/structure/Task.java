package com.f_candy_d.olga.structure;


import android.support.annotation.NonNull;
import android.util.Log;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.data_store.TaskTableRule;
import com.f_candy_d.olga.infra.SqlEntity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by daichi on 17/09/03.
 */

public class Task extends SqlEntityObject<TaskTableRule.ValidationErrorCode> {

    public String title;
    public InstantDate dateTermStart;
    public InstantDate dateTermEnd;
    public boolean isDone;
    public boolean doThroughoutTerm;
    public TaskType type;

    public Task() {
        super(TaskTableRule.TABLE_NAME);
        init();
    }

    public Task(Task task) {
        super(TaskTableRule.TABLE_NAME);

        if (task != null) {
            super.id = task.id;
            this.title = task.title;
            this.isDone = task.isDone;
            this.doThroughoutTerm = task.doThroughoutTerm;
            this.type = task.type;

            this.dateTermStart = (task.dateTermStart != null)
                    ? new InstantDate(task.dateTermStart)
                    : null;

            this.dateTermEnd = (task.dateTermEnd != null)
                    ? new InstantDate(task.dateTermEnd)
                    : null;

        } else {
            init();
        }
    }

    public Task(SqlEntity entity) {
        super(TaskTableRule.TABLE_NAME);
        if (entity != null) {
            constructFromSqlEntity(entity);
        } else {
            init();
        }
    }

    @NonNull
    @Override
    public TaskTableRule.ValidationErrorCode[] checkValidation() {
        ArrayList<TaskTableRule.ValidationErrorCode> errorCodes = new ArrayList<>();
        TaskTableRule.ValidationErrorCode errorCode;

        if ((errorCode = TaskTableRule.isTitleValid(this.title)) != null) {
            errorCodes.add(errorCode);
        }

        if ((errorCode = TaskTableRule.isDateValid(
                this.dateTermStart.getTimeInMillis(), this.dateTermEnd.getTimeInMillis())) != null) {
            errorCodes.add(errorCode);
        }

        if ((errorCode = TaskTableRule.isTypeValid(this.type)) != null) {
            errorCodes.add(errorCode);
        }

        for (TaskTableRule.ValidationErrorCode e : errorCodes) {
            Log.d("mylog", "error code -> " + e.toString());
        }

        return errorCodes.toArray(new TaskTableRule.ValidationErrorCode[]{});
    }

    @NonNull
    @Override
    public SqlEntity toSqlEntity(boolean includeRowId) {
        SqlEntity entity = new SqlEntity(TaskTableRule.TABLE_NAME);

        if (includeRowId) {
            entity.put(TaskTableRule._ID, super.id);
        }
        entity.put(TaskTableRule._DATE_TERM_END, this.dateTermEnd.asCalendar());
        entity.put(TaskTableRule._DATE_TERM_START, this.dateTermStart.asCalendar());
        entity.put(TaskTableRule._TITLE, this.title);
        entity.put(TaskTableRule._IS_DONE, this.isDone);
        entity.put(TaskTableRule._DO_THROUGHOUT_TERM, this.doThroughoutTerm);
        entity.put(TaskTableRule._TYPE, this.type);

        return entity;
    }

    @Override
    public void constructFromSqlEntity(SqlEntity entity) {
        if (entity == null) {
            return;
        }

        super.id = entity.getLongOrDefault(TaskTableRule._ID, super.id);
        this.title = entity.getStringOrDefault(TaskTableRule._TITLE, this.title);
        this.isDone = entity.getBooleanOrDefault(TaskTableRule._IS_DONE, this.isDone);
        this.doThroughoutTerm = entity.getBooleanOrDefault(TaskTableRule._DO_THROUGHOUT_TERM, this.doThroughoutTerm);
        this.type = entity.getQuantizableEnumOrDefault(TaskTableRule._TYPE, TaskType.class, this.type);

        Calendar date = entity.getCalendarOrDefault(TaskTableRule._DATE_TERM_END, null);
        this.dateTermEnd = (date != null) ? new InstantDate(date) : this.dateTermEnd;

        date = entity.getCalendarOrDefault(TaskTableRule._DATE_TERM_START, null);
        this.dateTermStart= (date != null) ? new InstantDate(date) : this.dateTermStart;
    }

    public void init() {
        super.id = TaskTableRule.defaultId();
        this.title = TaskTableRule.defaultTitle();
        this.dateTermStart = new InstantDate(TaskTableRule.defaultDateTermStart());
        this.dateTermEnd = new InstantDate(TaskTableRule.defaultDateTermEnd());
        this.isDone = TaskTableRule.defaultIsDone();
        this.doThroughoutTerm = TaskTableRule.defaultDoThroughoutTerm();
        this.type = TaskTableRule.defaultType();
    }

    public String toSummary() {
        return this.title + " @" + this.type.toString();
    }
}
