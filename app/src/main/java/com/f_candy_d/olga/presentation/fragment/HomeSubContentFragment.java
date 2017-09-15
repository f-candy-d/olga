package com.f_candy_d.olga.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.view_model.HomeSubContentViewModel;
import com.f_candy_d.vvm.FragmentViewModel;
import com.f_candy_d.vvm.VIewModelFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSubContentFragment extends VIewModelFragment {

    private HomeSubContentViewModel mViewModel;

    @Override
    protected FragmentViewModel onCreateViewModel() {
        mViewModel = new HomeSubContentViewModel();
        return mViewModel;
    }

    public HomeSubContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_sub_content, container, false);
    }

}
