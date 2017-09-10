package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;

import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.EventTask;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.vvm.ActivityViewModel;

/**
 * Created by daichi on 9/11/17.
 */

public class EventFormViewModel extends ActivityViewModel {

    public interface RequestReplyListener {
        void onNormalFinish(long newTaskId);
        void onAbnormalFinish(String message);
    }

    private EventTask mBuffer;
    private RequestReplyListener mRequestReplyListener;

    public EventFormViewModel(Context context, RequestReplyListener listener, long id) {
        super(context);
        mRequestReplyListener = listener;
        initBuffer(id);
    }

    private void initBuffer(long id) {
        if (id == DbContract.NULL_ID) {
            mBuffer = new EventTask();

        } else {
            Task task = TaskStreamUseCase.findTaskById(id);
            if (task != null) {
                mBuffer = new EventTask(task);
            } else {
                mBuffer = new EventTask();
            }
        }

        mBuffer.setTitle(null);
    }

    public void onInputStartDate(int year, int month, int dayOfMonth) {
        mBuffer.getStartDate().setYear(year);
        mBuffer.getStartDate().setMonth(month);
        mBuffer.getStartDate().setDayOfMonth(dayOfMonth);
    }

    public void onInputStartTime(int hourOfDay, int minute) {
        mBuffer.getStartDate().setHourOfDay(hourOfDay);
        mBuffer.getStartDate().setMinute(minute);
    }

    public void onInputEndDate(int year, int month, int dayOfMonth) {
        mBuffer.getEndDate().setYear(year);
        mBuffer.getEndDate().setMonth(month);
        mBuffer.getEndDate().setDayOfMonth(dayOfMonth);
    }

    public void onInputEndTime(int hourOfDay, int minute) {
        mBuffer.getEndDate().setHourOfDay(hourOfDay);
        mBuffer.getEndDate().setMinute(minute);
    }

    public void onInputTitle(String title) {
        mBuffer.setTitle(title);
    }

    public boolean onRequestToFinish() {
        boolean isOk = mBuffer.isValid();
        if (isOk) {
            long id = TaskStreamUseCase.insert(mBuffer);
            if (id != DbContract.NULL_ID) {
                mRequestReplyListener.onNormalFinish(id);
            } else {
                mRequestReplyListener.onAbnormalFinish("ERROR! #Sorry, failed to save Event data...");
            }
        }

        return isOk;
    }

    public InstantDate getCurrentStartDate() {
        return mBuffer.getStartDate();
    }

    public InstantDate getCurrentEndDate() {
        return mBuffer.getEndDate();
    }

    public String getCurrentTitle() {
        return mBuffer.getTitle();
    }
}
