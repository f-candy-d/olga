package com.f_candy_d.olga.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.olga.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFormFragment extends FormFragment {


    public SummaryFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary_form, container, false);
    }

    @Override
    protected String getTitle() {
        return null;
    }
}
