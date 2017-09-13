package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.Pair;
import android.util.Log;

import com.f_candy_d.olga.domain.EventTask;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.dialog.DatePickerDialogFragment;
import com.f_candy_d.olga.presentation.fragment.DateFormFragment;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.olga.presentation.fragment.SummaryFormFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by daichi on 9/13/17.
 */

public class EventFormViewModel extends TaskFormViewModel {

    private EventTask mBuffer;
    private String mDateFormTag;
    private String mSummaryFormTag;

    public EventFormViewModel(Context context, RequestReplyListener listener, long contentId) {
        super(context, listener, contentId);
    }

    @NonNull
    @Override
    public ArrayList<FormFragment> getFormFragments() {
        ArrayList<FormFragment> fragments = new ArrayList<>();
        FormFragment fragment;

        fragment = SummaryFormFragment.newInstance(mBuffer.getTitle(), null);
        mSummaryFormTag = fragment.getFragmentTag();
        fragments.add(fragment);

        fragment = DateFormFragment.newInstance();
        mDateFormTag = fragment.getFragmentTag();
        fragments.add(fragment);

        return fragments;
    }

    @Nullable
    @Override
    public Pair<Integer, String>[] onDataInput(Bundle data, String fragmentTag) {
        ArrayList<Pair<Integer, String>> errors = new ArrayList<>();

        if (fragmentTag.equals(mDateFormTag)) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            DateFormFragment.getArgsFromBundle(start, end, data);
            if (start.after(end)) {
                Pair<Integer, String> error = new Pair<>(
                        DateFormFragment.ERROR_CODE_START_DATE_IS_AFTER_END_DATE,
                        "Change the start date to be before the end date.");

                errors.add(error);
            }
        }

        Log.d("mylog", "error counot > " + String.valueOf(errors.size()) + " tag=" + fragmentTag + "  == " + mDateFormTag);

        return errors.toArray(new Pair[]{});
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
