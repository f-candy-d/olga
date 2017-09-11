package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.OuterListAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskGroupAdapter;
import com.f_candy_d.olga.presentation.SpacerItemDecoration;
import com.f_candy_d.olga.presentation.dialog.WhatAddDialogFragment;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import java.util.ArrayList;

public class HomeActivity extends ViewActivity
        implements WhatAddDialogFragment.OnSelectionChosenListener {

    private HomeViewModel mViewModel;
    private OuterListAdapter mAdapter;

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
                WhatAddDialogFragment dialog = new WhatAddDialogFragment();
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogFragmentTheme);
                dialog.show(getSupportFragmentManager(), null);
            }
        });

        final float density = getResources().getDisplayMetrics().density;
        final int itemSideSpace = (int) (16 * density);
        final int itemGroupTopSpace = (int) (8 * density);
        final int itemGroupBottomSpace = (int) (4 * density);

        SpacerItemDecoration spacerItemDecoration =
                new SpacerItemDecoration(this, new SpacerItemDecoration.Callback() {
                    @Override
                    public void getInsertedSpaceAroundItem(int adapterPosition, Rect output) {

                        if (adapterPosition == 0) {
                            output.top = itemGroupTopSpace * 8;
                        } else {
                            output.top = itemGroupTopSpace;
                        }

                        if (adapterPosition == 5 - 1) {
                            output.bottom = itemGroupBottomSpace * 6;
                        } else {
                            output.bottom = itemGroupBottomSpace;
                        }

                        output.left = itemSideSpace;
                        output.right = itemSideSpace;
                    }
                });

        mAdapter = new OuterListAdapter(this);
        initAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        LayoutInflater inflater = getLayoutInflater();
//        View entryPointCard = inflater.inflate(R.layout.entry_point_card, recyclerView, false);
//
//        MergeAdapter mergeAdapter = new MergeAdapter();
////        mergeAdapter.addView(entryPointCard);
//        mergeAdapter.addAdapter(mAdapter);

        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(spacerItemDecoration);
    }

    private void initAdapter() {
        mAdapter.removeAll();
        SimpleTaskGroupAdapter adapter;
        ArrayList<Task> tasks;

        tasks = mViewModel.getAllTasks();
        adapter = new SimpleTaskGroupAdapter(tasks);
        adapter.setHeaderTitle("All");
        mAdapter.addAdapter(adapter);

        tasks = mViewModel.getTasksNeedToBeRescheduled();
        adapter = new SimpleTaskGroupAdapter(tasks);
        adapter.setHeaderTitle("Needs To Be Rescheduled");
        mAdapter.addAdapter(adapter);

        tasks = mViewModel.getTasksInProcess();
        adapter = new SimpleTaskGroupAdapter(tasks);
        adapter.setHeaderTitle("Now");
        mAdapter.addAdapter(adapter);

        tasks = mViewModel.getTasksUpcoming();
        adapter = new SimpleTaskGroupAdapter(tasks);
        adapter.setHeaderTitle("Upcoming In 24 Hours");
        mAdapter.addAdapter(adapter);

        tasks = mViewModel.getTasksInFeature();
        adapter = new SimpleTaskGroupAdapter(tasks);
        adapter.setHeaderTitle("In Feature");
        mAdapter.addAdapter(adapter);
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
        initAdapter();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * region; WhatAddDialogFragment.OnSelectionChosenListener implementation
     */

    @Override
    public void onSelectionChosen(WhatAddDialogFragment.Selection selection, WhatAddDialogFragment dialogFragment) {
        switch (selection) {
            case ADD_EVENT:
                Intent intent = new Intent(this, EventFormActivity.class);
                startActivityForResult(111, intent, new OnResultListener() {
                    @Override
                    public void onResult(int resultCode, @Nullable Bundle data) {
                        Log.d("mylog", "returned -> ");
                    }
                });
        }
    }

}
