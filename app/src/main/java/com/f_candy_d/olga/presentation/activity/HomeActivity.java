package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.f_candy_d.dutils.CalendarUtil;
import com.f_candy_d.dutils.MergeAdapter;
import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.OuterListAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskGroupAdapter;
import com.f_candy_d.olga.presentation.SpacerItemDecoration;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends ViewActivity {

    private HomeViewModel mViewModel;
    private OuterListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected ActivityViewModel onCreateViewModel() {
        mViewModel = new HomeViewModel(this);
        return mViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                WhatAddDialogFragment dialog = new WhatAddDialogFragment();
//                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
//                dialog.show(getSupportFragmentManager(), null);
                showWhatAddBottomSheetDialog();
            }
        });

        final float density = getResources().getDisplayMetrics().density;
        final int itemSideSpace = (int) (12 * density);
        final int itemGroupTopSpace = (int) (8 * density);
        final int itemGroupBottomSpace = (int) (4 * density);

        SpacerItemDecoration spacerItemDecoration =
                new SpacerItemDecoration(this, new SpacerItemDecoration.Callback() {
                    @Override
                    public void getInsertedSpaceAroundItem(int adapterPosition, Rect output) {

                        if (adapterPosition == 4 - 1) {
                            output.bottom = itemGroupBottomSpace * 6;
                        } else {
                            output.bottom = itemGroupBottomSpace;
                        }

                        output.top = itemGroupTopSpace;
                        output.left = itemSideSpace;
                        output.right = itemSideSpace;
                    }
                });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(spacerItemDecoration);

        mAdapter = new OuterListAdapter(this);
        initAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initAdapter(RecyclerView recyclerView) {
        mAdapter.removeAll();
        MergeAdapter mergeAdapter;
        SimpleTaskAdapter adapter;
        ArrayList<Task> tasks;
        LayoutInflater inflater = getLayoutInflater();
        View header;

        /**
         * Tasks needs to be rescheduled
         */
        tasks = mViewModel.getTasksNeedToBeRescheduled();
        adapter = new SimpleTaskAdapter(tasks);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                Calendar now = Calendar.getInstance();
                String diff = AppDataDecoration.formatCalendarDiff(now, task.dateTermEnd.asCalendar());
                dateLabel.append(diff.concat(" ago"));
            }
        });
        header = inflater.inflate(R.layout.simple_task_group_adapter_header, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("Needs To Be Rescheduled");
        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(header);
        mergeAdapter.addAdapter(adapter);
        mAdapter.addAdapter(mergeAdapter);

        /**
         * Tasks in process
         */
        tasks = mViewModel.getTasksInProcess();
        adapter = new SimpleTaskAdapter(tasks);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                String text = "by " + AppDataDecoration.formatDatetime(task.dateTermEnd.asCalendar(), HomeActivity.this);
                dateLabel.append(text);
            }
        });
        header = inflater.inflate(R.layout.simple_task_group_adapter_header, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("Now");
        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(header);
        mergeAdapter.addAdapter(adapter);
        mAdapter.addAdapter(mergeAdapter);

        /**
         * Upcoming tasks
         */
        tasks = mViewModel.getTasksUpcoming();
        adapter = new SimpleTaskAdapter(tasks);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                dateLabel.append(AppDataDecoration.formatDatetime(task.dateTermStart.asCalendar(), HomeActivity.this));
            }
        });
        header = inflater.inflate(R.layout.simple_task_group_adapter_header, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("Upcoming In 24 Hours");
        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(header);
        mergeAdapter.addAdapter(adapter);
        mAdapter.addAdapter(mergeAdapter);

        /**
         * Feature tasks
         */
        tasks = mViewModel.getTasksInFeature();
        adapter = new SimpleTaskAdapter(tasks);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                dateLabel.append(AppDataDecoration.formatDatetime(task.dateTermStart.asCalendar(), HomeActivity.this));
            }
        });
        header = inflater.inflate(R.layout.simple_task_group_adapter_header, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("In Feature");
        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(header);
        mergeAdapter.addAdapter(adapter);
        mAdapter.addAdapter(mergeAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refresh();
    }

    private void refresh() {
        initAdapter(mRecyclerView);
        mAdapter.notifyDataSetChanged();
    }

    private void showWhatAddBottomSheetDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.fragment_what_add_dialog, null);
        dialog.setContentView(sheetView);
        dialog.show();
    }
}
