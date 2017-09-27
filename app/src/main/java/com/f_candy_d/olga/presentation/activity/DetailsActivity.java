package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.domain.usecase.TaskTableUseCase;
import com.f_candy_d.olga.presentation.adapter.ViewAdapter;

import java.util.ArrayList;
import java.util.Collection;

public class DetailsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_TASK = 1111;

    private static final String EXTRA_TASK_ID = "task_id";

    private ViewAdapter mAdapter;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private long mTaskId;

    public static Bundle makeExtra(long taskId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_TASK_ID, taskId);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        onCreateUI();

        if (!getIntent().getExtras().containsKey(EXTRA_TASK_ID)) {
            throw new IllegalStateException("Pass a task ID");
        }

        mTaskId = getIntent().getExtras().getLong(EXTRA_TASK_ID);
        UnmodifiableTask task = TaskTableUseCase.findTaskById(mTaskId);
        if (task == null) {
            task = new UnmodifiableTask();
        }
        invalidate(task, mRecyclerView, mAdapter, mFab);
    }

    private void onCreateUI() {

        // # Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // # FAB

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditButtonClick();
            }
        });

        // # RecyclerView

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private ViewAdapter createAdapter(RecyclerView recyclerView, UnmodifiableTask task) {
        ArrayList<View> itemViews = new ArrayList<>();
        return new ViewAdapter(itemViews);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void onEditButtonClick() {
        Intent intent = new Intent(this, TaskFormActivity.class);
        intent.putExtras(TaskFormActivity.makeExtra(mTaskId));
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_TASK && resultCode == RESULT_OK) {
            UnmodifiableTask task = TaskTableUseCase.findTaskById(mTaskId);
            if (task == null) {
                task = new UnmodifiableTask();
            }

            invalidate(task, mRecyclerView, mAdapter, mFab);
        }
    }

    /**
     * This is only used in {@link DetailsActivity#invalidate(UnmodifiableTask, RecyclerView, ViewAdapter, FloatingActionButton)}.
     */
    private boolean mIsEmptyListMode = false;

    private void invalidate(@NonNull UnmodifiableTask task,
                            @NonNull RecyclerView recyclerView,
                            @NonNull ViewAdapter viewAdapter,
                            @NonNull FloatingActionButton fab) {

        // # Header

        // title
        TextView textView = (TextView) findViewById(R.id.task_title);
        if (task.getTitle() != null) {
            textView.setText(task.getTitle());
        } else {
            textView.setText(R.string.empty_task_title);
        }
        // description
        textView = (TextView) findViewById(R.id.task_description);
        if (task.getDescription() != null) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(task.getDescription());
        } else {
            textView.setVisibility(View.GONE);
        }
        // achieved or not-achieved label
        textView = (TextView) findViewById(R.id.task_achievement_label);
        if (task.isAchieved()) {
            ((CardView) findViewById(R.id.task_achievement_label_bg))
                    .setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_cream_red));
            textView.setText(R.string.achieved_cap);
        } else {
            ((CardView) findViewById(R.id.task_achievement_label_bg))
                    .setCardBackgroundColor(ContextCompat.getColor(this, R.color.color_teal));
            textView.setText(R.string.not_achieved_cap);
        }

        // # List

        ViewAdapter newDataSet = createAdapter(recyclerView, task);
        if (newDataSet.getItemCount() != 0) {
            viewAdapter.swapDataSet(newDataSet.getAllViews());
            fab.setVisibility(View.VISIBLE);
            mIsEmptyListMode = false;

        } else if (!mIsEmptyListMode) {
            // Set empty view if the task has no options, and hide fab instead
            viewAdapter.clearViews();
            View itemView = ViewAdapter.inflateView(R.layout.no_task_options_empty_view, recyclerView);
            itemView.findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditButtonClick();
                }
            });
            viewAdapter.addView(itemView);

            fab.setVisibility(View.GONE);
            mIsEmptyListMode = true;
        }
    }
}