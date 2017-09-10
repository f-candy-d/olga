package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.f_candy_d.dutils.CalendarUtil;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.dialog.DatePickerDialogFragment;
import com.f_candy_d.olga.presentation.dialog.TimePickerDialogFragment;
import com.f_candy_d.olga.presentation.view_model.EventFormViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import java.util.Calendar;

public class EventFormActivity extends ViewActivity
        implements DatePickerDialogFragment.NoticeDateSetListener,
        TimePickerDialogFragment.NoticeTimeSetListener {

    private static final int START_DATE_PICKER = 0;
    private static final int END_DATE_PICKER = 1;
    private static final int START_TIME_PICKER = 2;
    private static final int END_TIME_PICKER = 3;

    // UI
    EditText mEditTextTitle;
    EditText mEditTextNote;
    Button mStartDateButton;
    Button mStartTimeButton;
    Button mEndDateButton;
    Button mEndTimeButton;

    @Override
    protected ActivityViewModel onCreateViewModel() {
        return new EventFormViewModel(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        initUI();
    }

    private void initUI() {
        mEditTextTitle = (EditText) findViewById(R.id.edit_title);
        mEditTextNote = (EditText) findViewById(R.id.edit_note);

        mStartDateButton = (Button) findViewById(R.id.select_start_date);
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(START_DATE_PICKER);
            }
        });

        mStartTimeButton = (Button) findViewById(R.id.select_start_time);
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(START_TIME_PICKER);
            }
        });

        mEndDateButton = (Button) findViewById(R.id.select_end_date);
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(END_DATE_PICKER);
            }
        });

        mEndTimeButton = (Button) findViewById(R.id.select_end_time);
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(END_TIME_PICKER);
            }
        });

        findViewById(R.id.ok_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
    }

    private void showDatePicker(int tag) {
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(tag);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void showTimePicker(int tag) {
        TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.newInstance(tag, true);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    /**
     * DatePickerDialogFragment.NoticeDateSetListener implementation
     */

    @Override
    public void onDateSet(int year, int month, int dayOfMonth, int tag) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        switch (tag) {
            case START_DATE_PICKER:
                mStartDateButton.setText(CalendarUtil.formatDateSimply(calendar));
                break;

            case END_DATE_PICKER:
                mEndDateButton.setText(CalendarUtil.formatDateSimply(calendar));
                break;
        }
    }

    /**
     * TimePickerDialogFragment.NoticeTimeSetListener implementation
     */

    @Override
    public void onTimeSet(int hourOfDay, int minute, int tag) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        switch (tag) {
            case START_TIME_PICKER:
                mStartTimeButton.setText(CalendarUtil.formatTimeSimply(calendar, false));
                break;

            case END_TIME_PICKER:
                mEndTimeButton.setText(CalendarUtil.formatTimeSimply(calendar, false));
                break;
        }
    }
}
