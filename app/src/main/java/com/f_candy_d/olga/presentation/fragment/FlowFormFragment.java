package com.f_candy_d.olga.presentation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.f_candy_d.olga.presentation.activity.TaskFlowFormActivity;

abstract public class FlowFormFragment extends Fragment {

    private OnDataInputListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataInputListener) {
            mListener = (OnDataInputListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDataInputListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected void onDispatchUserInput(Bundle data) {
        mListener.onDataInput(data, this.getClass().getSimpleName());
    }

    abstract public String getTitle();

    public TaskFlowFormActivity.Style getStyle() {
        return mListener.getStyle();
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
    public interface OnDataInputListener {
        void onDataInput(Bundle data, String simpleFragmentClassName);
        @NonNull TaskFlowFormActivity.Style getStyle();
    }
}
