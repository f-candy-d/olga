package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.SqliteTablePool;
import com.f_candy_d.olga.domain.TaskTablePool;
import com.f_candy_d.olga.domain.filter.DefaultFilterFactory;
import com.f_candy_d.olga.presentation.adapter.FullSpanViewAdapter;
import com.f_candy_d.olga.presentation.adapter.TaskAdapter;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class HomeActivity extends ViewActivity {

    private static final int REQUEST_CODE_MAKE_NEW_TASK = 1111;

    private HomeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerViewMergeAdapter mRootAdapter;
    private TaskAdapter mTaskAdapter;
    private TaskTablePool mTaskPool;

    @Override
    protected ActivityViewModel onCreateViewModel() {
        mViewModel = new HomeViewModel(this);
        return mViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mTaskPool = new TaskTablePool(DefaultFilterFactory.createAllFilter());
        mTaskPool.setCallback(new SqliteTablePool.Callback() {
            @Override
            public void onPooled(int index, int count) {
                if (mTaskAdapter != null) {
                    mTaskAdapter.notifyItemRangeInserted(index, count);
                }
            }

            @Override
            public void onReleased(int index, int count) {
                if (mTaskAdapter != null) {
                    mTaskAdapter.notifyItemRangeRemoved(index, count);
                }
            }

            @Override
            public void onChanged(int index, int count) {
                if (mTaskAdapter != null) {
                    mTaskAdapter.notifyItemRangeChanged(index, count);
                }
            }

            @Override
            public void onMoved(int fromIndex, int toIndex) {
                if (mTaskAdapter != null) {
                    mTaskAdapter.notifyItemMoved(fromIndex, toIndex);
                }
            }
        });
        mTaskPool.applyFilter();
        initUI();
    }

    private void initUI() {

        // # Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Now");
        }

        // # FAB

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TaskFormActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MAKE_NEW_TASK);
            }
        });

        // # RecyclerView

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // # Adapter

        mRootAdapter = new RecyclerViewMergeAdapter();
        LayoutInflater inflater = LayoutInflater.from(mRecyclerView.getContext());
        View shortcutView = inflater.inflate(R.layout.shortcut_card, mRecyclerView, false);

        // TODO;
        if (mViewModel.getTasksNeedToBeRescheduled().length != 0) {
            View noticeView = getLayoutInflater().inflate(R.layout.notice_overdue_card, mRecyclerView, false);
            FullSpanViewAdapter fullSpanViewAdapter = new FullSpanViewAdapter(noticeView, shortcutView);
            mRootAdapter.addAdapter(fullSpanViewAdapter);
        } else {
            FullSpanViewAdapter fullSpanViewAdapter = new FullSpanViewAdapter(shortcutView);
            mRootAdapter.addAdapter(fullSpanViewAdapter);
        }

        // TODO;
        if (mTaskPool.size() != 0) {
            mTaskAdapter = new TaskAdapter(mTaskPool);
            mRootAdapter.addAdapter(mTaskAdapter);

        } else {
            View emptyView = inflater.inflate(R.layout.home_no_task_message, mRecyclerView, false);
            mRootAdapter.addAdapter(new FullSpanViewAdapter(emptyView));
        }

        mRecyclerView.setAdapter(mRootAdapter);
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
                Intent intent = new Intent(HomeActivity.this, TaskFormActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setContentView(sheetView);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MAKE_NEW_TASK &&
                resultCode == RESULT_OK && data.getExtras() != null) {

            mTaskPool.applyFilter();
        }
    }
}
