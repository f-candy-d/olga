package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.presentation.adapter.FullSpanViewAdapter;
import com.f_candy_d.olga.presentation.adapter.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import java.util.ArrayList;
import java.util.Calendar;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class HomeActivity extends ViewActivity {

    private HomeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerViewMergeAdapter mAdapter;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new RecyclerViewMergeAdapter();

        LayoutInflater inflater = LayoutInflater.from(mRecyclerView.getContext());
        View shortcutView = inflater.inflate(R.layout.shortcut_card, mRecyclerView, false);

        if (mViewModel.getTasksNeedToBeRescheduled().size() != 0) {
            View noticeView = getLayoutInflater().inflate(R.layout.notice_overdue_card, mRecyclerView, false);
            FullSpanViewAdapter fullSpanViewAdapter = new FullSpanViewAdapter(noticeView, shortcutView);
            mAdapter.addAdapter(fullSpanViewAdapter);
        } else {
            FullSpanViewAdapter fullSpanViewAdapter = new FullSpanViewAdapter(shortcutView);
            mAdapter.addAdapter(fullSpanViewAdapter);
        }

        ArrayList<Task> tasks = mViewModel.getTasksInProcess();
        if (tasks.size() != 0) {
            SimpleTaskAdapter adapter = new SimpleTaskAdapter(tasks);
            adapter.setCallback(new SimpleTaskAdapter.Callback() {
                @Override
                public boolean isItemFullSpan(int position) {
                    return false;
                }

                @Override
                public void onBind(SimpleTaskAdapter.OutVisibility outVisibility, Task task,
                                   StringBuffer title, StringBuffer time, StringBuffer location) {
                    // title
                    title.append(task.title);
                    // time
                    Calendar now = Calendar.getInstance();
                    String diff = AppDataDecoration.formatCalendarDiff(now, task.endDate.asCalendar());
                    time.append(diff.concat(" ago"));
                    // location
                    location.append("Shizuoka hamamatsushi 432-8061");
                }
            });
            mAdapter.addAdapter(adapter);

        } else {
            View emptyView = inflater.inflate(R.layout.home_no_task_message, mRecyclerView, false);
            mAdapter.addAdapter(new FullSpanViewAdapter(emptyView));
        }

        mRecyclerView.setAdapter(mAdapter);
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

    private void showAddTaskBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.what_add_dialog, null);

        sheetView.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TodoFormActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }
}
