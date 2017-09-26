package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.SqliteTablePool;
import com.f_candy_d.olga.domain.TaskTablePool;
import com.f_candy_d.olga.domain.filter.DefaultFilterFactory;
import com.f_candy_d.olga.domain.filter.TaskFilter;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.presentation.ItemClickHelper;
import com.f_candy_d.olga.presentation.adapter.FullSpanViewAdapter;
import com.f_candy_d.olga.presentation.adapter.TaskAdapter;
import com.f_candy_d.olga.presentation.dialog.FilterPickerDialogFragment;
import com.f_candy_d.olga.presentation.view_model.HomeViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class FilterdTaskListActivity extends ViewActivity
        implements FilterPickerDialogFragment.OnFilterSelectListener {

    private static final int REQUEST_CODE_MAKE_NEW_TASK = 1111;

    private static final int SINGLE_SPAN_COUNT = 1;
    private static final int MULTIPLE_SPAN_COUNT = 2;
    private static final int DEFAULT_SPAN_COUNT = MULTIPLE_SPAN_COUNT;

    private static final String EXTRA_FILTER = "extra_filter";
    private static final String EXTRA_IS_STACKED = "extra_is_stacked";

    /**
     * Use for 'savedInstanceState'
     */

    private static final String STATE_FILTER = "state_filter";

    private HomeViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerViewMergeAdapter mRootAdapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private TaskAdapter mTaskAdapter;
    private TaskTablePool mTaskPool;
    private int mCurrentSpanCount;

    @Override
    protected ActivityViewModel onCreateViewModel() {
        mViewModel = new HomeViewModel(this);
        return mViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterd_task_list);

        if (savedInstanceState != null) {
            TaskFilter filter = savedInstanceState.getParcelable(STATE_FILTER);
            onCreateTaskPool(filter);
        } else {
            if (getIntent().hasExtra(EXTRA_FILTER)) {
                TaskFilter filter = getIntent().getParcelableExtra(EXTRA_FILTER);
                onCreateTaskPool(filter);
            } else {
                // Default
                onCreateTaskPool(DefaultFilterFactory.createNowFilter());
            }
        }

        onCreateUI();
    }

    private void onCreateTaskPool(TaskFilter filter) {
        mTaskPool = new TaskTablePool(filter);
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
    }

    private void onCreateUI() {

        if (mTaskPool.getFilter() == null) {
            throw new IllegalStateException("Set a filter to the task pool");
        }

        // # Toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mTaskPool.getFilter().getFilterName());

            // If this Activity is launched from the other instance of this Activity,
            // show the navigation back button.
            if (getIsStackedFlagFromIntent()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        // # FAB

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterdTaskListActivity.this, TaskFormActivity.class);
                startActivityForResult(intent, REQUEST_CODE_MAKE_NEW_TASK);
            }
        });

        // # RecyclerView

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mCurrentSpanCount = mLayoutManager.getSpanCount();
        mRecyclerView.setLayoutManager(mLayoutManager);

        // # Adapter

        mRootAdapter = new RecyclerViewMergeAdapter();

        // Create option view items for the certain filters
        if (mTaskPool.getFilter().getFilterName().equals(DefaultFilterFactory.FILTER_NAME_NOW)) {
            final LayoutInflater inflater = LayoutInflater.from(mRecyclerView.getContext());
            View shortcutView = inflater.inflate(R.layout.shortcut_card, mRecyclerView, false);
            FullSpanViewAdapter fullSpanViewAdapter = new FullSpanViewAdapter(shortcutView);
            mRootAdapter.addAdapter(fullSpanViewAdapter);
        }

        mTaskAdapter = new TaskAdapter(mTaskPool);
        mRootAdapter.addAdapter(mTaskAdapter);
        mRecyclerView.setAdapter(mRootAdapter);

        // # ItemClickHelper

        ItemClickHelper itemClickHelper = new ItemClickHelper(new ItemClickHelper.Callback() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                RecyclerViewMergeAdapter.PosSubAdapterInfo info = mRootAdapter.getPosSubAdapterInfoForGlobalPosition(viewHolder.getAdapterPosition());
                int localPosition = info.posInSubAdapter;
                launchTaskDetailsScreen(mTaskPool.getAt(localPosition));
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                // Nothing to do...
            }
        });

        itemClickHelper.attachToRecyclerView(mRecyclerView);

        // # ItemTouchHelper

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof TaskAdapter.TaskViewHolder) {
                    int localPosition = mRootAdapter.getPosSubAdapterInfoForGlobalPosition(viewHolder.getAdapterPosition()).posInSubAdapter;
                    final Task task = mTaskPool.getAt(localPosition);
                    mTaskPool.release(task);
                    task.setIsAchieved(true);
                    mTaskPool.update(task);
                    Snackbar.make(mRecyclerView, "Achieved!", Snackbar.LENGTH_SHORT)
                            .setAction("undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    task.setIsAchieved(false);
                                    mTaskPool.update(task);
                                    mTaskPool.pool(task.getId());
                                }
                            }).show();
                }
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // Desable swipe for position in the RecyclerView.
                // See -> https://stackoverflow.com/questions/30713121/disable-swipe-for-position-in-recyclerview-using-itemtouchhelper-simplecallback
                if (!(viewHolder instanceof TaskAdapter.TaskViewHolder)) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
                return 0.7f;
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If this Activity is launched from the other instance of this Activity,
        // hide the filter menu button.
        if (getIsStackedFlagFromIntent()) {
            menu.findItem(R.id.action_filter).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filterd_task_list, menu);
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
        } else if (id == R.id.action_filter) {
            launchFilterPickerDialog();
        } else if (id == R.id.action_refresh) {
            refreshData();
        } else if (id == R.id.action_change_item_alignment) {
            toggleViewSpanCount();
            // Swap icons
            int icon = (mCurrentSpanCount == SINGLE_SPAN_COUNT) ? R.drawable.ic_view_quilt : R.drawable.ic_view_stream;
            item.setIcon(icon);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddTaskBottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.what_add_dialog, null);

        sheetView.findViewById(R.id.add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FilterdTaskListActivity.this, TaskFormActivity.class);
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

            refreshData();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTaskPool.getFilter() == null) {
            throw new IllegalStateException("TaskPool must has a filter");
        }

        outState.putParcelable(STATE_FILTER, mTaskPool.getFilter());
    }

    private void refreshData() {
        mTaskPool.applyFilter();
    }

    private void launchTaskDetailsScreen(UnmodifiableTask task) {

    }

    private void launchAnotherFilterdTaskListScreen(TaskFilter filter) {
        Intent intent = new Intent(this, FilterdTaskListActivity.class);
        intent.putExtra(EXTRA_FILTER, filter);
        intent.putExtra(EXTRA_IS_STACKED, true);
        startActivity(intent);
    }

    private void launchFilterPickerDialog() {
        FilterPickerDialogFragment picker = FilterPickerDialogFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // Show as a full screen dialog
        transaction.add(android.R.id.content, picker, null).commit();
    }

    private boolean getIsStackedFlagFromIntent() {
        return getIntent().getBooleanExtra(EXTRA_IS_STACKED, false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void toggleViewSpanCount() {
        if (mCurrentSpanCount == SINGLE_SPAN_COUNT) {
            mLayoutManager.setSpanCount(MULTIPLE_SPAN_COUNT);
        } else {
            mLayoutManager.setSpanCount(SINGLE_SPAN_COUNT);
        }

        mCurrentSpanCount = mLayoutManager.getSpanCount();
    }

    /**
     * FilterPickerDialogFragment.OnFilterSelectListener implementation
     */

    @Override
    public void onFilterSelected(TaskFilter filter) {
        launchAnotherFilterdTaskListScreen(filter);
    }
}
