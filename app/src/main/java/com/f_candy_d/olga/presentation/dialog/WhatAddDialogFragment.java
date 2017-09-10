package com.f_candy_d.olga.presentation.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.f_candy_d.olga.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSelectionChosenListener} interface
 * to handle interaction events.
 */
public class WhatAddDialogFragment extends DialogFragment {

    public enum Selection {
        ADD_EVENT
    }

    private OnSelectionChosenListener mListener;

    public WhatAddDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_what_add_dialog, container, false);

        view.findViewById(R.id.add_event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSelectionChosen(Selection.ADD_EVENT, WhatAddDialogFragment.this);
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectionChosenListener) {
            mListener = (OnSelectionChosenListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSelectionChosenListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSelectionChosenListener {
        void onSelectionChosen(Selection selection, WhatAddDialogFragment dialogFragment);
    }
}
