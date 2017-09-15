package com.f_candy_d.olga.presentation.fragment;


import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.view_model.HomeSubContentViewModel;
import com.f_candy_d.vvm.FragmentViewModel;
import com.f_candy_d.vvm.VIewModelFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSubContentFragment extends VIewModelFragment {

    private static final int TAB_POSITION_EXPIRED = 0;
    private static final int TAB_POSITION_UPCOMING = 1;
    private static final int TAB_POSITION_FEATURE = 2;

    // tabs
    private ImageView mTabIconExpired;
    private ImageView mTabIconUpcoming;
    private ImageView mTabIconFeature;
    private TextView mTabTitleExpired;
    private TextView mTabTitleUpcoming;
    private TextView mTabTitleFeature;

    // adapters
    private SimpleTaskAdapter mAdapter;

    private int mSelectedTabColor;
    private int mUnselectedTabColor;
    private int mPrevTabPosition;
    private HomeSubContentViewModel mViewModel;
    private RecyclerView mRecyclerView;

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
        View view = inflater.inflate(R.layout.fragment_home_sub_content, container, false);

        // Tabs
        view.findViewById(R.id.tab_expired).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabClick(TAB_POSITION_EXPIRED);
            }
        });

        view.findViewById(R.id.tab_upcoming).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabClick(TAB_POSITION_UPCOMING);
            }
        });

        view.findViewById(R.id.tab_feature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTabClick(TAB_POSITION_FEATURE);
            }
        });

        // Tab icons
        mTabIconExpired = view.findViewById(R.id.tab_icon_expired);
        mTabIconUpcoming = view.findViewById(R.id.tab_icon_upcoming);
        mTabIconFeature = view.findViewById(R.id.tab_icon_feature);

        // Tab titles
        mTabTitleExpired = view.findViewById(R.id.tab_title_expired);
        mTabTitleUpcoming = view.findViewById(R.id.tab_title_upcoming);
        mTabTitleFeature = view.findViewById(R.id.tab_title_feature);

        // Color
        mSelectedTabColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        mUnselectedTabColor = ContextCompat.getColor(getActivity(), R.color.google_material_icon_gray);

        // Recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set up initial state
        mPrevTabPosition = TAB_POSITION_EXPIRED;
        applyTabColor(mTabIconExpired, mTabTitleExpired, mSelectedTabColor);
        mAdapter = new SimpleTaskAdapter(mViewModel.getAllTasks());
        mAdapter.setNoItemMessage(R.string.no_tasks_message);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void onTabClick(int position) {
        if (mPrevTabPosition == position) {
            return;
        }

        switch (position) {
            case TAB_POSITION_EXPIRED:
                switchAdapter(mAdapter);
                applyTabColor(mTabIconExpired, mTabTitleExpired, mSelectedTabColor);
                applyTabColor(mTabIconUpcoming, mTabTitleUpcoming, mUnselectedTabColor);
                applyTabColor(mTabIconFeature, mTabTitleFeature, mUnselectedTabColor);
                break;

            case TAB_POSITION_UPCOMING:
                switchAdapter(mAdapter);
                applyTabColor(mTabIconExpired, mTabTitleExpired, mUnselectedTabColor);
                applyTabColor(mTabIconUpcoming, mTabTitleUpcoming, mSelectedTabColor);
                applyTabColor(mTabIconFeature, mTabTitleFeature, mUnselectedTabColor);
                break;

            case TAB_POSITION_FEATURE:
                switchAdapter(mAdapter);
                applyTabColor(mTabIconExpired, mTabTitleExpired, mUnselectedTabColor);
                applyTabColor(mTabIconUpcoming, mTabTitleUpcoming, mUnselectedTabColor);
                applyTabColor(mTabIconFeature, mTabTitleFeature, mSelectedTabColor);
                break;
        }

        mPrevTabPosition = position;
    }

    private void applyTabColor(ImageView icon, TextView title, int color) {
        icon.setColorFilter(color);
        title.setTextColor(color);
    }

    private void switchAdapter(final RecyclerView.Adapter adapter) {
        mRecyclerView.animate().alpha(0f).setDuration(150)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.animate().alpha(1f).setDuration(150).start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
    }
}
