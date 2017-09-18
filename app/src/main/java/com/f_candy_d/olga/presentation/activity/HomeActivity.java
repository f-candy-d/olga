package com.f_candy_d.olga.presentation.activity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.f_candy_d.dutils.BottomSheetStateObserver;
import com.f_candy_d.dutils.MergeAdapter;
import com.f_candy_d.olga.AppDataDecoration;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.FullSpanViewAdapter;
import com.f_candy_d.olga.presentation.OuterListAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskAdapter;
import com.f_candy_d.olga.presentation.SpacerItemDecoration;
import com.f_candy_d.olga.presentation.fragment.HomeContentFragment;
import com.f_candy_d.olga.presentation.fragment.HomeSubContentFragment;
import com.f_candy_d.olga.presentation.view_model.FormViewModelFactory;
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

        SimpleTaskAdapter adapter = new SimpleTaskAdapter(mViewModel.getTasksNeedToBeRescheduled());
        adapter.setOnBindItemCallback(new SimpleTaskAdapter.OnBindItemCallback() {
            @Override
            public void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel) {
                title.append(task.title);
                Calendar now = Calendar.getInstance();
                String diff = AppDataDecoration.formatCalendarDiff(now, task.dateTermEnd.asCalendar());
                dateLabel.append(diff.concat(" ago"));
            }
        });

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

        mAdapter.addAdapter(adapter);
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
