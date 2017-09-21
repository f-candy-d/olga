package com.f_candy_d.olga.presentation.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.activity.FormActivity;

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
        View view = inflater.inflate(R.layout.fragment_summary_form, container, false);
        FormActivity.Style style = getStyle();

        // Title
        EditText editTitle = view.findViewById(R.id.edit_text_title);
        editTitle.setTextColor(style.textColorPrimary);
        editTitle.setHintTextColor(style.textColorSecondary);
        editTitle.getBackground().setColorFilter(style.colorSecondary, PorterDuff.Mode.SRC_ATOP);

        // Description
        EditText editDescrip = view.findViewById(R.id.edit_text_description);
        editDescrip.setTextColor(style.textColorPrimary);
        editDescrip.setHintTextColor(style.textColorSecondary);

        return view;
    }

    @Override
    public String getTitle() {
        return "Which exercise?";
    }
}
