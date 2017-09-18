package com.f_candy_d.olga.presentation.fragment;


import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.f_candy_d.dutils.MergeAdapter;
import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.view_model.HomeSubContentViewModel;
import com.f_candy_d.vvm.FragmentViewModel;
import com.f_candy_d.vvm.VIewModelFragment;

import java.util.ArrayList;
import java.util.Calendar;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeSubContentFragment extends VIewModelFragment {

    public interface InteractionListener {
        void onHeaderClick();
    }

    // Use as a second parameter of #View.canScrollVertically(int)
    private static final int CHECK_SCROLLING_UP_FLAG = -1;

    private static final int TAB_POSITION_EXPIRED = 0;
    private static final int TAB_POSITION_UPCOMING = 1;
    private static final int TAB_POSITION_FEATURE = 2;

    // header
    private View mHeader;

    // tabs
    private ImageView mTabIconExpired;
    private ImageView mTabIconUpcoming;
    private ImageView mTabIconFeature;
    private TextView mTabTitleExpired;
    private TextView mTabTitleUpcoming;
    private TextView mTabTitleFeature;
    private View mTabs;

    // adapters
    private RecyclerViewMergeAdapter mExpiredDataAdapter;
    private RecyclerViewMergeAdapter mUpcomingDataAdapter;
    private RecyclerViewMergeAdapter mFeatureDataAdapter;

    // misc
    private int mSelectedTabColor;
    private int mUnselectedTabColor;
    private int mTabsElevation;
    private int mPrevTabPosition;
    private HomeSubContentViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private InteractionListener mInteractionListener;

    @Override
    protected FragmentViewModel onCreateViewModel() {
        mViewModel = new HomeSubContentViewModel();
        return mViewModel;
    }

    public HomeSubContentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mInteractionListener = (InteractionListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement HomeSubContentFragment.InteractionListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_sub_content, container, false);

        // Header
        mHeader = view.findViewById(R.id.header);
        mHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInteractionListener.onHeaderClick();
            }
        });

        // Tabs
        mTabs = view.findViewById(R.id.tabs_layout);

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

        // Resources
        mSelectedTabColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        mUnselectedTabColor = ContextCompat.getColor(getActivity(), R.color.google_material_icon_gray);
        mTabsElevation = getResources().getDimensionPixelSize(R.dimen.toolbar_elevation);

        // Recycler view
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Set elevation to tabs & header view only when the user starts scrolling
                super.onScrolled(recyclerView, dx, dy);
                boolean canScrollVertically = recyclerView.canScrollVertically(CHECK_SCROLLING_UP_FLAG);
                boolean hasTabsElevationZero = (ViewCompat.getElevation(mTabs) == 0f);

                if (hasTabsElevationZero && canScrollVertically) {
                    ViewCompat.setElevation(mTabs, mTabsElevation);
                    ViewCompat.setElevation(mHeader, mTabsElevation);
                } else if (!hasTabsElevationZero && !canScrollVertically){
                    ViewCompat.setElevation(mTabs, 0);
                    ViewCompat.setElevation(mHeader, 0);
                }
            }
        });

        // adapters
        initAdapter(mRecyclerView);

        // Set up initial state
        mPrevTabPosition = -1;
        onTabClick(TAB_POSITION_EXPIRED);
        mPrevTabPosition = TAB_POSITION_EXPIRED;
        ViewCompat.setElevation(mTabs, 0);
        ViewCompat.setElevation(mHeader, 0);

        return view;
    }

    private void initAdapter(RecyclerView recyclerView) {
        SimpleTaskAdapter adapter;

        // Tasks needs to be rescheduled
        adapter = new SimpleTaskAdapter(mViewModel.getTasksNeedToBeRescheduled());
        adapter.setNoItemMessage(R.string.no_tasks_message);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                Calendar now = Calendar.getInstance();
                String diff = AppDataDecoration.formatCalendarDiff(now, task.dateTermEnd.asCalendar());
                dateLabel.append(diff.concat(" ago"));
            }
        });
        mExpiredDataAdapter = new RecyclerViewMergeAdapter();
        mExpiredDataAdapter.addAdapter(adapter);

        // Upcoming tasks
        adapter = new SimpleTaskAdapter(mViewModel.getTasksUpcoming());
        adapter.setNoItemMessage(R.string.no_tasks_message_upcoming);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                String text = AppDataDecoration.formatTime(task.dateTermStart.asCalendar(), false);
                dateLabel.append(text);
            }
        });
        mUpcomingDataAdapter = new RecyclerViewMergeAdapter();
        mUpcomingDataAdapter.addAdapter(adapter);

        // Feature tasks
        adapter = new SimpleTaskAdapter(mViewModel.getTasksInFeature());
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                dateLabel.append(AppDataDecoration.formatDatetimeShortly(task.dateTermStart.asCalendar(), false));
            }
        });
        mFeatureDataAdapter = new RecyclerViewMergeAdapter();
        mFeatureDataAdapter.addAdapter(adapter);
        // Footer
        View footer = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.item_show_more_button, recyclerView, false);
        mFeatureDataAdapter.addView(footer);
    }

    private void onTabClick(int position) {
        if (mPrevTabPosition == position) {
            return;
        }

        switch (position) {
            case TAB_POSITION_EXPIRED:
                switchAdapter(mExpiredDataAdapter);
                applyTabColor(mTabIconExpired, mTabTitleExpired, mSelectedTabColor);
                applyTabColor(mTabIconUpcoming, mTabTitleUpcoming, mUnselectedTabColor);
                applyTabColor(mTabIconFeature, mTabTitleFeature, mUnselectedTabColor);
                break;

            case TAB_POSITION_UPCOMING:
                switchAdapter(mUpcomingDataAdapter);
                applyTabColor(mTabIconExpired, mTabTitleExpired, mUnselectedTabColor);
                applyTabColor(mTabIconUpcoming, mTabTitleUpcoming, mSelectedTabColor);
                applyTabColor(mTabIconFeature, mTabTitleFeature, mUnselectedTabColor);
                break;

            case TAB_POSITION_FEATURE:
                switchAdapter(mFeatureDataAdapter);
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
//        mRecyclerView.swapAdapter(adapter, true);
        mRecyclerView.setAdapter(adapter);
//        mRecyclerView.animate().alpha(0.0f).setDuration(150)
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animator) {
//                        mRecyclerView.setAdapter(adapter);
//                        mRecyclerView.animate().alpha(1.0f).setDuration(150).start();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animator) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animator) {
//
//                    }
//                }).start();
    }

    public void setHeaderAlpha(float alpha) {
        mHeader.setAlpha(alpha);
        if (alpha == 0.0f && mHeader.getVisibility() == View.VISIBLE) {
            mHeader.setVisibility(View.GONE);
        } else if (mHeader.getVisibility() == View.GONE) {
            mHeader.setVisibility(View.VISIBLE);
        }
    }
}
