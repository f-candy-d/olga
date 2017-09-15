package com.f_candy_d.olga.presentation.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.view_model.HomeContentViewModel;
import com.f_candy_d.vvm.FragmentViewModel;
import com.f_candy_d.vvm.VIewModelFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeContentFragment extends VIewModelFragment {

    private HomeContentViewModel mViewModel;
    private SimpleTaskAdapter mAdapter;

    @Override
    protected FragmentViewModel onCreateViewModel() {
        mViewModel = new HomeContentViewModel();
        return mViewModel;
    }

    public HomeContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initAdapter(mAdapter);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void initAdapter(SimpleTaskAdapter adapter) {
        mAdapter = new SimpleTaskAdapter(mViewModel.getAllTasks());
        mAdapter.setNoItemMessage(R.string.no_tasks_message);
    }
}
