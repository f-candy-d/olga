package com.f_candy_d.olga.presentation.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.presentation.activity.FormActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFormFragment extends FormFragment {

    private static final String ARGS_TITLE = "args_title";
    private static final String ARGS_DESCRIPTION = "args_description";
    private static final String ARGS_FORM_TITLE = "args_form_title";

    public static final String FIELD_TITLE = "field_title";
    public static final String FIELD_DESCRIPTION = "field_description";

    private Bundle mDataBuffer;

    public static SummaryFormFragment newInstance(String formTitle) {
        return newInstance(null, null, formTitle);
    }

    public static SummaryFormFragment newInstance(String title, String description, String formTitle) {
        SummaryFormFragment fragment = new SummaryFormFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_TITLE, title);
        args.putString(ARGS_DESCRIPTION, description);
        args.putString(ARGS_FORM_TITLE, formTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryFormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBuffer = new Bundle();
        String title = null;
        String description = null;
        if (getArguments() != null) {
            title = getArguments().getString(ARGS_TITLE, null);
            description = getArguments().getString(ARGS_DESCRIPTION, null);
        }

        mDataBuffer.putString(FIELD_TITLE, title);
        mDataBuffer.putString(FIELD_DESCRIPTION, description);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_form, container, false);
        FormActivity.Style style = getStyle();

        // # Title EditText

        EditText editTitle = view.findViewById(R.id.edit_text_title);
        editTitle.setTextColor(style.textColorPrimary);
        editTitle.setHintTextColor(style.textColorSecondary);
        // underline color
        editTitle.getBackground().setColorFilter(style.colorSecondary, PorterDuff.Mode.SRC_ATOP);
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mDataBuffer.putString(FIELD_TITLE, editable.toString());
                onDispatchUserInput(mDataBuffer);
            }
        });

        // # Description EditText

        EditText editDescription = view.findViewById(R.id.edit_text_description);
        editDescription.setTextColor(style.textColorPrimary);
        editDescription.setHintTextColor(style.textColorSecondary);
        editDescription.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mDataBuffer.putString(FIELD_DESCRIPTION, editable.toString());
                onDispatchUserInput(mDataBuffer);
            }
        });

        return view;
    }

    @Override
    public String getTitle() {
        String defaultTitle = Utils.getString(R.string.summary_form_default_form_title);
        if (getArguments() != null) {
            return getArguments().getString(ARGS_FORM_TITLE, defaultTitle);
        }
        return defaultTitle;
    }
}
