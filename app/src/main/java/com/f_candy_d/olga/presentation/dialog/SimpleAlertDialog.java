package com.f_candy_d.olga.presentation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

/**
 * Created by daichi on 9/29/17.
 */

public class SimpleAlertDialog extends DialogFragment {

    public interface ButtonClickListener {
        void onPositiveButtonClick(int tag);
        void onNegativeButtonClick(int tag);
    }

    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_MESSAGE = "arg_message";
    private static final String ARG_NEGATIVE_BUTTON_TITLE = "arg_negative_button_title";
    private static final String ARG_POSITIVE_BUTTON_TITLE = "arg_positive_button_title";
    private static final String ARG_TAG = "arg_tag";

    private ButtonClickListener mListener;
    private String mTitle;
    private String mMessage;
    private String mNegativeButtonTitle;
    private String mPositiveButtonTitle;
    private int mTag;

    public static SimpleAlertDialog newInstance
            (String title, String message, String positiveButtonTitle, String negativeButtonTitle, int tag) {

        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_MESSAGE, message);
        bundle.putString(ARG_NEGATIVE_BUTTON_TITLE, negativeButtonTitle);
        bundle.putString(ARG_POSITIVE_BUTTON_TITLE, positiveButtonTitle);
        bundle.putInt(ARG_TAG, tag);

        SimpleAlertDialog fragment = new SimpleAlertDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE, null);
            mMessage = getArguments().getString(ARG_MESSAGE, null);
            mNegativeButtonTitle = getArguments().getString(ARG_NEGATIVE_BUTTON_TITLE, null);
            mPositiveButtonTitle = getArguments().getString(ARG_POSITIVE_BUTTON_TITLE, null);
            mTag = getArguments().getInt(ARG_TAG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (mTitle != null) {
            builder.setTitle(mTitle);
        }

        if (mMessage != null) {
            builder.setMessage(mMessage);
        }

        if (mPositiveButtonTitle != null) {
            builder.setPositiveButton(mPositiveButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onPositiveButtonClick(mTag);
                }
            });
        }

        if (mNegativeButtonTitle != null) {
            builder.setNegativeButton(mNegativeButtonTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onNegativeButtonClick(mTag);
                }
            });
        }

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement SimpleAlertDialog.ButtonClickListener interface");
        }
    }

    /**
     * Builder
     * --------------------------------------------------------------------------- */

    public static class Builder {

        private String mTitle;
        private String mMessage;
        private String mNegativeButtonTitle;
        private String mPositiveButtonTitle;
        private int mTag;

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder negativeButton(String title) {
            mNegativeButtonTitle = title;
            return this;
        }

        public Builder positiveButton(String title) {
            mPositiveButtonTitle = title;
            return this;
        }

        public Builder tag(int tag) {
            mTag = tag;
            return this;
        }

        public SimpleAlertDialog create() {
            return SimpleAlertDialog.newInstance(
                    mTitle,
                    mMessage,
                    mPositiveButtonTitle,
                    mNegativeButtonTitle,
                    mTag);
        }
    }
}
