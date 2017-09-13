package com.f_candy_d.olga.presentation.fragment;


import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.f_candy_d.olga.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SummaryFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFormFragment extends FormFragment {

    private static final String ARG_TITLE = "argTitle";
    private static final String ARG_DESCRIPTION = "argDescription";

    private static final String TAG = "summaryForm";
    private static final String TITLE = "Summary";

    private String mTitle = null;
    private String mDescription = null;

    public SummaryFormFragment() {
        // Required empty public constructor
    }

    public static SummaryFormFragment newInstance() {
        return newInstance(null, null);
    }

    public static SummaryFormFragment newInstance(String title, String description) {
        SummaryFormFragment fragment = new SummaryFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE, null);
            mDescription = getArguments().getString(ARG_DESCRIPTION, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_form, container, false);

        TextInputLayout inputLayout = view.findViewById(R.id.text_input_layout_title);
        if (mTitle == null || mTitle.length() == 0) {
            inputLayout.setErrorEnabled(true);
            inputLayout.setError("You need to enter a title");
        }

        TextInputEditText titleEdit = view.findViewById(R.id.input_edit_text_title);
//        titleEdit.getBackground().setColorFilter(ContextCompat.getColor(getActivity(),
//                R.color.color_cream_orange_light), PorterDuff.Mode.SRC_ATOP);
        titleEdit.setText(mTitle);

        EditText descripEdit = view.findViewById(R.id.edit_text_description);
        descripEdit.setText(mDescription);

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
        return R.drawable.ic_title;
    }
}
