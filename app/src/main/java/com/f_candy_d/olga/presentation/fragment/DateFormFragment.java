package com.f_candy_d.olga.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.dialog.DatePickerDialogFragment;
import com.f_candy_d.olga.presentation.dialog.TimePickerDialogFragment;

import java.util.Calendar;

public class DateFormFragment extends FormFragment
        implements
        DatePickerDialogFragment.NoticeDateSetListener,
        TimePickerDialogFragment.NoticeTimeSetListener {

    private static final String TAG = "dateForm";
    private static final String TITLE = "Date";

    private static final String ARG_START_YEAR = "startYear";
    private static final String ARG_START_MONTH = "startMonth";
    private static final String ARG_START_DAY_OF_MONTH = "startDayOfMonth";
    private static final String ARG_START_HOUR_OF_DAY = "startHourOfDay";
    private static final String ARG_START_MINUTE = "startMinute";
    private static final String ARG_END_YEAR = "endYear";
    private static final String ARG_END_MONTH = "endMonth";
    private static final String ARG_END_DAY_OF_MONTH = "endDayOfMonth";
    private static final String ARG_END_HOUR_OF_DAY = "endHourOfDay";
    private static final String ARG_END_MINUTE = "endMinute";

    // Dialog tags
    private static final int TAG_START_DATE_PICKER = 0;
    private static final int TAG_END_DATE_PICKER = 1;
    private static final int TAG_START_TIME_PICKER = 2;
    private static final int TAG_END_TIME_PICKER = 3;

    private Calendar mStartDate;
    private Calendar mEndDate;

    // UI
    private TextView mLabelStartY;
    private TextView mLabelStartMD;
    private TextView mLabelStartHm;
    private TextView mLabelEndY;
    private TextView mLabelEndMD;
    private TextView mLabelEndHm;

    // colors
    @ColorRes private static final int TEXT_COLOR_NORMAL = android.R.color.white;
    @ColorRes private static final int TEXT_COLOR_ERROR = R.color.color_cream_orange_light;

    // Error codes
    public static final int ERROR_CODE_START_DATE_IS_AFTER_END_DATE = 1111;

    public DateFormFragment() {
        // Required empty public constructor
    }

    public static DateFormFragment newInstance() {
        return newInstance(Calendar.getInstance(), Calendar.getInstance());
    }

    public static DateFormFragment newInstance(Calendar start, Calendar end) {
        DateFormFragment fragment = new DateFormFragment();
        Bundle args = new Bundle();
        putArgsIntoBundle(start, end, args);
        fragment.setArguments(args);
        return fragment;
    }
    
    private static void putArgsIntoBundle(Calendar start, Calendar end, Bundle bundle) {
        bundle.putInt(ARG_START_YEAR, start.get(Calendar.YEAR));
        bundle.putInt(ARG_START_MONTH, start.get(Calendar.MONTH));
        bundle.putInt(ARG_START_DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH));
        bundle.putInt(ARG_START_HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
        bundle.putInt(ARG_START_MINUTE, start.get(Calendar.MINUTE));

        bundle.putInt(ARG_END_YEAR, end.get(Calendar.YEAR));
        bundle.putInt(ARG_END_MONTH, end.get(Calendar.MONTH));
        bundle.putInt(ARG_END_DAY_OF_MONTH, end.get(Calendar.DAY_OF_MONTH));
        bundle.putInt(ARG_END_HOUR_OF_DAY, end.get(Calendar.HOUR_OF_DAY));
        bundle.putInt(ARG_END_MINUTE, end.get(Calendar.MINUTE));
    }
    
    public static void getArgsFromBundle(Calendar outStart, Calendar outEnd, Bundle bundle) {
        outStart.set(Calendar.YEAR, bundle.getInt(ARG_START_YEAR, outStart.get(Calendar.YEAR)));
        outStart.set(Calendar.MONTH, bundle.getInt(ARG_START_MONTH, outStart.get(Calendar.MONTH)));
        outStart.set(Calendar.DAY_OF_MONTH, bundle.getInt(ARG_START_DAY_OF_MONTH, outStart.get(Calendar.DAY_OF_MONTH)));
        outStart.set(Calendar.HOUR_OF_DAY, bundle.getInt(ARG_START_HOUR_OF_DAY, outStart.get(Calendar.HOUR_OF_DAY)));
        outStart.set(Calendar.MINUTE, bundle.getInt(ARG_START_MINUTE, outStart.get(Calendar.MINUTE)));

        outEnd.set(Calendar.YEAR, bundle.getInt(ARG_END_YEAR, outEnd.get(Calendar.YEAR)));
        outEnd.set(Calendar.MONTH, bundle.getInt(ARG_END_MONTH, outEnd.get(Calendar.MONTH)));
        outEnd.set(Calendar.DAY_OF_MONTH, bundle.getInt(ARG_END_DAY_OF_MONTH, outEnd.get(Calendar.DAY_OF_MONTH)));
        outEnd.set(Calendar.HOUR_OF_DAY, bundle.getInt(ARG_END_HOUR_OF_DAY, outEnd.get(Calendar.HOUR_OF_DAY)));
        outEnd.set(Calendar.MINUTE, bundle.getInt(ARG_END_MINUTE, outEnd.get(Calendar.MINUTE)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartDate = Calendar.getInstance();
        mEndDate = Calendar.getInstance();
        if (getArguments() != null) {
            getArgsFromBundle(mStartDate, mEndDate, getArguments());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_form, container, false);

        // Pick up start date button
        view.findViewById(R.id.start_date_label_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(mStartDate, TAG_START_DATE_PICKER);
            }
        });

        // Pick up start time button
        view.findViewById(R.id.date_label_start_h_m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(mStartDate, TAG_START_TIME_PICKER);
            }
        });

        // Pick up end date button
        view.findViewById(R.id.end_date_label_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(mEndDate, TAG_END_DATE_PICKER);
            }
        });

        // Pick up end time button
        view.findViewById(R.id.date_label_end_h_m).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(mEndDate, TAG_END_TIME_PICKER);
            }
        });

        // Labels
        mLabelStartY = view.findViewById(R.id.date_label_start_y);
        mLabelStartHm = view.findViewById(R.id.date_label_start_h_m);
        mLabelStartMD = view.findViewById(R.id.date_label_start_m_d);
        mLabelEndY = view.findViewById(R.id.date_label_end_y);
        mLabelEndHm = view.findViewById(R.id.date_label_end_h_m);
        mLabelEndMD = view.findViewById(R.id.date_label_end_m_d);

        invalidateUI();

        return view;
    }

    @NonNull
    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_access_time;
    }

    @Override
    public void onShowError(int errorCode) {
        if (errorCode == ERROR_CODE_START_DATE_IS_AFTER_END_DATE) {
            int color = ContextCompat.getColor(getActivity(), TEXT_COLOR_ERROR);
            mLabelStartY.setTextColor(color);
            mLabelStartMD.setTextColor(color);
            mLabelStartHm.setTextColor(color);
        }
    }

    private void releaseError(int errorCode) {
        if (errorCode == ERROR_CODE_START_DATE_IS_AFTER_END_DATE) {
            int color = ContextCompat.getColor(getActivity(), TEXT_COLOR_NORMAL);
            mLabelStartY.setTextColor(color);
            mLabelStartMD.setTextColor(color);
            mLabelStartHm.setTextColor(color);
        }
    }

    private void showDatePicker(Calendar date, int tag) {
        DatePickerDialogFragment dialogFragment = DatePickerDialogFragment.newInstance(tag, date);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    private void showTimePicker(Calendar date, int tag) {
        TimePickerDialogFragment dialogFragment = TimePickerDialogFragment.newInstance(tag, date, false);
        dialogFragment.setTargetFragment(this, 0);
        dialogFragment.show(getActivity().getSupportFragmentManager(), null);
    }

    private void invalidateUI() {
        mLabelStartY.setText(String.valueOf(mStartDate.get(Calendar.YEAR)));
        mLabelStartMD.setText(AppDataDecoration.formatDateShortly(mStartDate));
        mLabelStartHm.setText(AppDataDecoration.formatTime(mStartDate, false));
        mLabelEndY.setText(String.valueOf(mEndDate.get(Calendar.YEAR)));
        mLabelEndMD.setText(AppDataDecoration.formatDateShortly(mEndDate));
        mLabelEndHm.setText(AppDataDecoration.formatTime(mEndDate, false));
    }

    /**
     * DatePickerDialogFragment.NoticeDateSetListener implementation
     */

    @Override
    public void onDateSet(int year, int month, int dayOfMonth, int tag) {
        switch (tag) {
            case TAG_START_DATE_PICKER:
                mStartDate.set(Calendar.YEAR, year);
                mStartDate.set(Calendar.MONTH, month);
                mStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                break;

            case TAG_END_DATE_PICKER:
                mEndDate.set(Calendar.YEAR, year);
                mEndDate.set(Calendar.MONTH, month);
                mEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                break;
        }

        invalidateUI();
        releaseError(ERROR_CODE_START_DATE_IS_AFTER_END_DATE);
        Bundle data = new Bundle();
        putArgsIntoBundle(mStartDate, mEndDate, data);
        onDispatchUserInput(data);
    }

    /**
     * TimePickerDialogFragment.NoticeTimeSetListener implementation
     */

    @Override
    public void onTimeSet(int hourOfDay, int minute, int tag) {
        switch (tag) {
            case TAG_START_TIME_PICKER:
                mStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mStartDate.set(Calendar.MINUTE, minute);
                break;

            case TAG_END_TIME_PICKER:
                mEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mEndDate.set(Calendar.MINUTE, minute);
                break;
        }

        invalidateUI();
        releaseError(ERROR_CODE_START_DATE_IS_AFTER_END_DATE);
        Bundle data = new Bundle();
        putArgsIntoBundle(mStartDate, mEndDate, data);
        onDispatchUserInput(data);
    }
}
