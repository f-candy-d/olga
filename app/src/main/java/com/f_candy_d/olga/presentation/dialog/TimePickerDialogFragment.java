package com.f_candy_d.olga.presentation.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class TimePickerDialogFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private static final String ARG_HOUR_OF_DAY = "hourOfDay";
    private static final String ARG_MINUTE = "minute";
    private static final String ARG_IS_24_HOUR_FORMAT = "is24HourFormat";
    private static final String ARG_DIALOG_TAG = "dialogTag";

    private static final boolean DEFAULT_IS_24_HOUR_FORMAT = false;
    private static final int DEFAULT_DIALOG_TAG = -1;

    private NoticeTimeSetListener mListener;
    private int mDialogTag;

    public static TimePickerDialogFragment newInstance(boolean is24HourFormat) {
        return newInstance(DEFAULT_DIALOG_TAG, Calendar.getInstance(), is24HourFormat);
    }

    public static TimePickerDialogFragment newInstance(Calendar date, boolean is24HourFormat) {
        return newInstance(
                DEFAULT_DIALOG_TAG,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE),
                is24HourFormat);
    }

    public static TimePickerDialogFragment newInstance(int hourOfDay, int minute, boolean is24HourFormat) {
        return newInstance(DEFAULT_DIALOG_TAG, hourOfDay, minute, is24HourFormat);
    }

    public static TimePickerDialogFragment newInstance(int tag, boolean is24HourFormat) {
        return newInstance(tag, Calendar.getInstance(), is24HourFormat);
    }

    public static TimePickerDialogFragment newInstance(int tag, Calendar date, boolean is24HourFormat) {
        return newInstance(
                tag,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE),
                is24HourFormat);
    }

    public static TimePickerDialogFragment newInstance(int tag, int hourOfDay, int minute, boolean is24HourFormat) {
        Bundle args = new Bundle();
        args.putInt(ARG_HOUR_OF_DAY, hourOfDay);
        args.putInt(ARG_MINUTE, minute);
        args.putInt(ARG_DIALOG_TAG, tag);
        args.putBoolean(ARG_IS_24_HOUR_FORMAT, is24HourFormat);

        TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDialogTag = getArguments().getInt(ARG_DIALOG_TAG);
        } else {
            mDialogTag = DEFAULT_DIALOG_TAG;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int h, m;
        boolean is24h;

        if (getArguments() != null) {
            h = getArguments().getInt(ARG_HOUR_OF_DAY);
            m = getArguments().getInt(ARG_MINUTE);
            is24h = getArguments().getBoolean(ARG_IS_24_HOUR_FORMAT);

        } else {
            Calendar now = Calendar.getInstance();
            h = now.get(Calendar.HOUR_OF_DAY);
            m = now.get(Calendar.MINUTE);
            is24h = DEFAULT_IS_24_HOUR_FORMAT;
        }

        return new TimePickerDialog(getActivity(), this, h, m, is24h);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        mListener.onTimeSet(i, i1, mDialogTag);
    }

    public interface NoticeTimeSetListener {
        void onTimeSet(int hourOfDay, int minute, int tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host fragment implements the callback interface
        Fragment hostFragment = getTargetFragment();
        if (hostFragment != null) {
            try {
                mListener = (NoticeTimeSetListener) hostFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(hostFragment.toString()
                        + " must implement NoticeTimeSetListener");
            }

        } else {
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                mListener = (NoticeTimeSetListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(context.toString()
                        + " must implement NoticeTimeSetListener");
            }
        }
    }
}
