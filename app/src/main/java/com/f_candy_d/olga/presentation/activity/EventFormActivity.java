package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.f_candy_d.dutils.CalendarUtil;
import com.f_candy_d.dutils.InstantDate;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.presentation.dialog.DatePickerDialogFragment;
import com.f_candy_d.olga.presentation.dialog.TimePickerDialogFragment;
import com.f_candy_d.olga.presentation.view_model.OLDEventFormViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

public class EventFormActivity extends ViewActivity
        implements DatePickerDialogFragment.NoticeDateSetListener,
        TimePickerDialogFragment.NoticeTimeSetListener,
        OLDEventFormViewModel.RequestReplyListener {

    private static final int START_DATE_PICKER = 0;
    private static final int END_DATE_PICKER = 1;
    private static final int START_TIME_PICKER = 2;
    private static final int END_TIME_PICKER = 3;

    public static final String EXTRA_TASK_ID = "extraTaskId";

    // UI
    private EditText mEditTextTitle;
    private EditText mEditTextNote;
    private Button mStartDateButton;
    private Button mStartTimeButton;
    private Button mEndDateButton;
    private Button mEndTimeButton;

    // Misc
    private OLDEventFormViewModel mViewModel;

    @Override
    protected ActivityViewModel onCreateViewModel() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long id = extras.getLong(EXTRA_TASK_ID, DbContract.NULL_ID);
            mViewModel = new OLDEventFormViewModel(this, this, id);
        } else {
            mViewModel = new OLDEventFormViewModel(this, this, DbContract.NULL_ID);
        }

        return mViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        initUI();
    }

    private void initUI() {
        mEditTextTitle = (EditText) findViewById(R.id.edit_title);
        mEditTextTitle.setText(mViewModel.getCurrentTitle());
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mViewModel.onInputTitle(charSequence.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This space intentionally left blank...
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // This one too
            }
        });

        mEditTextNote = (EditText) findViewById(R.id.edit_note);

        mStartDateButton = (Button) findViewById(R.id.select_start_date);
        mStartDateButton.setText(CalendarUtil.formatDateSimply(mViewModel.getCurrentStartDate().asCalendar()));
        mStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(mViewModel.getCurrentStartDate(), START_DATE_PICKER);
            }
        });

        mStartTimeButton = (Button) findViewById(R.id.select_start_time);
        mStartTimeButton.setText(CalendarUtil.formatTimeSimply(mViewModel.getCurrentStartDate().asCalendar(), false));
        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(mViewModel.getCurrentStartDate(), START_TIME_PICKER);
            }
        });

        mEndDateButton = (Button) findViewById(R.id.select_end_date);
        mEndDateButton.setText(CalendarUtil.formatDateSimply(mViewModel.getCurrentEndDate().asCalendar()));
        mEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(mViewModel.getCurrentEndDate(), END_DATE_PICKER);
            }
        });

        mEndTimeButton = (Button) findViewById(R.id.select_end_time);
        mEndTimeButton.setText(CalendarUtil.formatTimeSimply(mViewModel.getCurrentEndDate().asCalendar(), false));
        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(mViewModel.getCurrentEndDate(), END_TIME_PICKER);
            }
        });

        findViewById(R.id.ok_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!mViewModel.onRequestToFinish()) {
                            Toast.makeText(EventFormActivity.this, "Fill out all fields correctly!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDatePicker(InstantDate date, int tag) {
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(tag, date.asCalendar());
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    private void showTimePicker(InstantDate date, int tag) {
        TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.newInstance(tag, date.asCalendar(), false);
        dialogFragment.show(getSupportFragmentManager(), null);
    }

    /**
     * OLDEventFormViewModel.RequestReplyListener implementation
     */

    @Override
    public void onNormalFinish(long newTaskId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_TASK_ID, newTaskId);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAbnormalFinish(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        setResult(RESULT_CANCELED, null);
        finish();
    }

    /**
     * DatePickerDialogFragment.NoticeDateSetListener implementation
     */

    @Override
    public void onDateSet(int year, int month, int dayOfMonth, int tag) {
        switch (tag) {
            case START_DATE_PICKER:
                mViewModel.onInputStartDate(year, month, dayOfMonth);
                mStartDateButton.setText(CalendarUtil.formatDateSimply(mViewModel.getCurrentStartDate().asCalendar()));
                break;

            case END_DATE_PICKER:
                mViewModel.onInputEndDate(year, month, dayOfMonth);
                mEndDateButton.setText(CalendarUtil.formatDateSimply(mViewModel.getCurrentEndDate().asCalendar()));
                break;
        }
    }

    /**
     * TimePickerDialogFragment.NoticeTimeSetListener implementation
     */

    @Override
    public void onTimeSet(int hourOfDay, int minute, int tag) {
        switch (tag) {
            case START_TIME_PICKER:
                mViewModel.onInputStartTime(hourOfDay, minute);
                mStartTimeButton.setText(CalendarUtil.formatTimeSimply(mViewModel.getCurrentStartDate().asCalendar(), false));
                break;

            case END_TIME_PICKER:
                mViewModel.onInputEndTime(hourOfDay, minute);
                mEndTimeButton.setText(CalendarUtil.formatTimeSimply(mViewModel.getCurrentEndDate().asCalendar(), false));
                break;
        }
    }
}
