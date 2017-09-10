package com.f_candy_d.olga.presentation.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.text.MessagePattern;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by daichi on 9/11/17.
 */

public class DatePickerDialogFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY_OF_MONTH = "dayOfMonth";
    private static final String ARG_DIALOG_TAG = "dialogTag";

    private static final int DEFAULT_DIALOG_TAG = -1;

    private NoticeDateSetListener mListener;
    private int mDialogTag;

    public static DatePickerDialogFragment newInstance() {
        return newInstance(DEFAULT_DIALOG_TAG, Calendar.getInstance());
    }

    public static DatePickerDialogFragment newInstance(Calendar date) {
        return newInstance(
                DEFAULT_DIALOG_TAG,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
    }

    public static DatePickerDialogFragment newInstance(int year, int month, int dayOfMonth) {
        return newInstance(DEFAULT_DIALOG_TAG, year, month, dayOfMonth);
    }

    public static DatePickerDialogFragment newInstance(int tag) {
        return newInstance(tag, Calendar.getInstance());
    }

    public static DatePickerDialogFragment newInstance(int tag, Calendar date) {
        return newInstance(
                tag,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH));
    }

    public static DatePickerDialogFragment newInstance(int tag, int year, int month, int dayOfMonth) {
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY_OF_MONTH, dayOfMonth);
        args.putInt(ARG_DIALOG_TAG, tag);

        DatePickerDialogFragment fragment = new DatePickerDialogFragment();
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
        int y,m,d;

        if (getArguments() != null) {
            y = getArguments().getInt(ARG_YEAR);
            m = getArguments().getInt(ARG_MONTH);
            d = getArguments().getInt(ARG_DAY_OF_MONTH);

        } else {
            Calendar now = Calendar.getInstance();
            y = now.get(Calendar.YEAR);
            m = now.get(Calendar.MONTH);
            d = now.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), this, y, m, d);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        mListener.onDateSet(i, i1, i2, mDialogTag);

    }

    public interface NoticeDateSetListener {
        void onDateSet(int year, int month, int dayOfMonth, int tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDateSetListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
