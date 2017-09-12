package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.EventTask;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.domain.TaskStreamUseCase;
import com.f_candy_d.olga.presentation.fragment.FormFragment;

import java.util.ArrayList;

/**
 * Created by daichi on 9/13/17.
 */

abstract public class TaskFormViewModel extends FormViewModel {

    // Init this value in #onInitWithId()
    private boolean mIsNewData = true;

    public TaskFormViewModel(Context context, RequestReplyListener listener, long contentId) {
        super(context, listener, contentId);
    }

    @Override
    protected void onInitWithId(long contentId) {
        if (contentId == DbContract.NULL_ID) {
            onCreateEmptyBuffer(new Task());
            mIsNewData = true;

        } else {
            Task data = TaskStreamUseCase.findTaskById(contentId);
            if (data != null) {
                onCreateBufferFromExistData(data);
                mIsNewData = false;
            } else {
                data = new Task();
                onCreateEmptyBuffer(data);
                mIsNewData = true;
            }
        }
    }

    @Override
    protected long onSaveContent() {
        Task data = getBuffer();
        if (mIsNewData) {
            return TaskStreamUseCase.insert(data);
        } else {

            boolean result = TaskStreamUseCase.update(data);
            return (result) ? data.id : DbContract.NULL_ID;
        }
    }

    abstract void onCreateBufferFromExistData(Task task);
    abstract void onCreateEmptyBuffer(Task task);
    abstract Task getBuffer();
}
