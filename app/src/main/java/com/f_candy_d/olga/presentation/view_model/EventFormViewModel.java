package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.domain.EventTask;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.fragment.DateFormFragment;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.olga.presentation.fragment.SummaryFormFragment;

import java.util.ArrayList;

/**
 * Created by daichi on 9/13/17.
 */

public class EventFormViewModel extends TaskFormViewModel {

    private EventTask mBuffer;

    public EventFormViewModel(Context context, RequestReplyListener listener, long contentId) {
        super(context, listener, contentId);
    }

    @NonNull
    @Override
    public ArrayList<FormFragment> getFormFragments() {
        ArrayList<FormFragment> fragments = new ArrayList<>();
        fragments.add(SummaryFormFragment.newInstance(mBuffer.getTitle(), null));
        fragments.add(DateFormFragment.newInstance());
        return fragments;
    }

    @Nullable
    @Override
    public String[] onDataInput(Bundle data, String fragmentTag) {
        return null;
    }

    @Override
    void onCreateBufferFromExistData(Task task) {
        mBuffer = new EventTask(task);
    }

    @Override
    void onCreateEmptyBuffer(Task task) {
        mBuffer = new EventTask(task);
    }

    @Override
    Task getBuffer() {
        return mBuffer;
    }

    @Nullable
    @Override
    protected String[] isContentValid() {
        return new String[0];
    }
}
