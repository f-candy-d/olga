package com.f_candy_d.olga.presentation.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.f_candy_d.dutils.MergeAdapter;
import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.OuterListAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.SpacerItemDecoration;
import com.f_candy_d.olga.presentation.fragment.HomeContentFragment;
import com.f_candy_d.olga.presentation.view_model.FormViewModelFactory;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeActivity extends ViewActivity {

    private HomeViewModel mViewModel;
    private OuterListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private BottomSheetBehavior mSheetBehavior;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Now");
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskBottomSheetDialog();
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet_layout);
        mSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED && fab.getVisibility() == View.VISIBLE) {
                    fab.animate()
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    fab.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                }
                            })
                            .scaleX(0).scaleY(0).setDuration(150).start();

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED && fab.getVisibility() == View.INVISIBLE) {
                    fab.setVisibility(View.VISIBLE);
                    fab.animate().scaleX(1).scaleY(1).setDuration(300).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            fab.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
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
        header = inflater.inflate(R.layout.item_header_with_done_all_button, recyclerView, false);
        ((TextView) header.findViewById(R.id.header_title)).setText("Needs To Be Rescheduled");
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
                String text = "due by " + AppDataDecoration.formatDatetime(task.dateTermEnd.asCalendar(), false);
                dateLabel.append(text);
            }
        });
        header = inflater.inflate(R.layout.item_header_basic, recyclerView, false);
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
        adapter.setNoItemMessage(R.string.no_tasks_message_upcoming);
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                String text = AppDataDecoration.formatTime(task.dateTermStart.asCalendar(), false);
                dateLabel.append(text);
            }
        });
        header = inflater.inflate(R.layout.item_header_basic, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("Upcoming");
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
                dateLabel.append(AppDataDecoration.formatDatetimeShortly(task.dateTermStart.asCalendar(), false));
            }
        });
        header = inflater.inflate(R.layout.item_header_basic, recyclerView, false);
        ((TextView) header.findViewById(R.id.simple_task_adapter_header_title)).setText("Next 6 Days");
        mergeAdapter = new MergeAdapter();
        mergeAdapter.addView(header);
        mergeAdapter.addAdapter(adapter);
        // Footer
        header = inflater.inflate(R.layout.item_show_more_button, recyclerView, false);
        mergeAdapter.addView(header);
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
//        refresh();
    }

    private void refresh() {
        initAdapter(mRecyclerView);
        mAdapter.notifyDataSetChanged();
    }

    private void showAddTaskBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.what_add_dialog, null);

        sheetView.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle extras = FormActivity.makeExtras(FormViewModelFactory.Model.EVENT_FORM);
                Intent intent = new Intent(HomeActivity.this, FormActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }
}
