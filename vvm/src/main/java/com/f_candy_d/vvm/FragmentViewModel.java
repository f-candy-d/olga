package com.f_candy_d.vvm;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by daichi on 9/15/17.
 *
 * This class depends on Fragment's life-cycle.
 */

public class FragmentViewModel {

    private Context mContext;

    protected Context getContext() {
        return mContext;
    }

    void setContext(Context context) {
        mContext = context;
    }

    protected Resources getResources() {
        return mContext.getResources();
    }

    /**
     * Life cycle
     */

    protected void onCreate(@Nullable Bundle savedInstanceState) {
    }

    protected void onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
    }

    protected void onActivityCreated(@Nullable Bundle savedInstanceState) {
    }

    protected void onStart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDestroyView() {
    }

    protected void onDestroy() {
    }

    protected void onDetach() {
    }
}
