package com.f_candy_d.olga.presentation.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.olga.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFormFragment extends FormFragment {

    private static final String TAG = "summaryForm";
    private static final String TITLE = "Summary";

    public SummaryFormFragment() {
        // Required empty public constructor
    }

    public static SummaryFormFragment newInstance() {
        SummaryFormFragment fragment = new SummaryFormFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_form, container, false);
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
        return R.drawable.ic_event;
    }
}
