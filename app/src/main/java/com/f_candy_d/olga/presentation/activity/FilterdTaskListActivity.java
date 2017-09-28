package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class FilterdTaskListActivity extends ViewActivity
        implements FilterPickerDialogFragment.OnFilterSelectListener {

    private static final int REQUEST_CODE_MAKE_NEW_TASK = 1111;
    private static final int REQUEST_CODE_SHOW_DETAILS = 2222;

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
        mTaskPool.enableAutoFilter();
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
//            final LayoutInflater inflater = LayoutInflater.from(mRecyclerView.getContext());
//            View shortcutView = inflater.inflate(R.layout.item_shourtcuts_grid_card, mRecyclerView, false);
            View shortcutView = createShourtcutView(mRecyclerView);
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
                if (isItemClickable(viewHolder)) {
                    RecyclerViewMergeAdapter.PosSubAdapterInfo info = mRootAdapter.getPosSubAdapterInfoForGlobalPosition(viewHolder.getAdapterPosition());
                    int localPosition = info.posInSubAdapter;
                    launchTaskDetailsScreen(mTaskPool.getAt(localPosition));
                }
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
                    onTaskItemSwiped((TaskAdapter.TaskViewHolder) viewHolder, direction);
                }
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (!canItemSwipe(recyclerView, viewHolder)) return 0;
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

    private View createShourtcutView(RecyclerView parent) {
        // Calculate vertical span count
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int gridViewWidth =
                metrics.widthPixels - (getResources().getDimensionPixelSize(R.dimen.shortcut_card_layout_margin) * 2
                        + getResources().getDimensionPixelSize(R.dimen.shortcut_card_margin) * 2);

        final int shourtcutItemSize = getResources().getDimensionPixelSize(R.dimen.shortcut_item_size)
                + getResources().getDimensionPixelSize(R.dimen.shortcut_item_margin) * 2;

        final int spanCount = gridViewWidth / shourtcutItemSize;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shourtcuts_grid_card, parent, false);
        RecyclerView grid = view.findViewById(R.id.shourtcuts_grid_view);
        grid.setLayoutManager(new GridLayoutManager(this, spanCount));
        grid.setHasFixedSize(true);

        // Shourtcut items
        final ArrayList<View> items = new ArrayList<>();
        LayoutInflater inflater = LayoutInflater.from(grid.getContext());
        View item;

        item = inflater.inflate(R.layout.shourtcut_item, grid, false);
        items.add(item);
        ((ImageView) item.findViewById(R.id.shortcut_item_icon)).setImageResource(R.drawable.ic_inbox);
        ((TextView) item.findViewById(R.id.shourtcut_item_title)).setText("All");
        item.findViewById(R.id.shourtcut_item_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAnotherFilterdTaskListScreen(DefaultFilterFactory.createAllFilter());
            }
        });

        item = inflater.inflate(R.layout.shourtcut_item, grid, false);
        items.add(item);
        ((ImageView) item.findViewById(R.id.shortcut_item_icon)).setImageResource(R.drawable.ic_bookmark);
        ((TextView) item.findViewById(R.id.shourtcut_item_title)).setText("Achieved");
        item.findViewById(R.id.shourtcut_item_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAnotherFilterdTaskListScreen(DefaultFilterFactory.createAchievedFilter());
            }
        });

        item = inflater.inflate(R.layout.shourtcut_item, grid, false);
        items.add(item);
        ((ImageView) item.findViewById(R.id.shortcut_item_icon)).setImageResource(R.drawable.ic_bookmark_border);
        ((TextView) item.findViewById(R.id.shourtcut_item_title)).setText("Unachieved");
        item.findViewById(R.id.shourtcut_item_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchAnotherFilterdTaskListScreen(DefaultFilterFactory.createNotAchievedFilter());
            }
        });

        // adapter
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(items.get(viewType)) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

            @Override
            public int getItemCount() {
                return items.size();
            }
        };

        grid.setAdapter(adapter);

        return view;
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
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtras(DetailsActivity.makeExtra(task.getId()));
        startActivityForResult(intent, REQUEST_CODE_SHOW_DETAILS);
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

    private boolean canItemSwipe(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Desable swipe for position in the RecyclerView.
        // See -> https://stackoverflow.com/questions/30713121/disable-swipe-for-position-in-recyclerview-using-itemtouchhelper-simplecallback
        return  (viewHolder instanceof TaskAdapter.TaskViewHolder);
    }

    private void onTaskItemSwiped(TaskAdapter.TaskViewHolder viewHolder, int direction) {
        int localPosition = mRootAdapter.getPosSubAdapterInfoForGlobalPosition(viewHolder.getAdapterPosition()).posInSubAdapter;
        final Task task = mTaskPool.getAt(localPosition);
        mTaskPool.release(task);
        final int pickupFlag = mTaskPool.getFilter().getPickUpAchievementFlag();

        if (pickupFlag == TaskFilter.FLAG_PICKUP_ONLY_NOT_ACHIEVED) {
            // 'task.isAchieved' should be false
            task.setIsAchieved(true);
            mTaskPool.update(task);
            Snackbar.make(mRecyclerView, R.string.item_achieved_message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setIsAchieved(false);
                            mTaskPool.update(task);
                        }
                    }).show();

        } else if (pickupFlag == TaskFilter.FLAG_PICKUP_ONLY_ACHIEVED) {
            // 'task.isAchieved' should be true
            task.setIsAchieved(false);
            mTaskPool.update(task);
            Snackbar.make(mRecyclerView, R.string.item_unachieved_message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_undo, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            task.setIsAchieved(true);
                            mTaskPool.update(task);
                        }
                    }).show();

        } else {
            // pickupFlag should be 'TaskFilter.FLAG_PICKUP_BOTH'
            task.setIsAchieved(!task.isAchieved());
            mTaskPool.update(task);
            int title = (task.isAchieved()) ? R.string.item_achieved_message : R.string.item_unachieved_message;
            Snackbar.make(mRecyclerView, title, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTaskPool.disableAutoFilter();
    }

    private boolean isItemClickable(RecyclerView.ViewHolder viewHolder) {
        return (viewHolder instanceof TaskAdapter.TaskViewHolder);
    }

    /**
     * FilterPickerDialogFragment.OnFilterSelectListener implementation
     */

    @Override
    public void onFilterSelected(TaskFilter filter) {
        launchAnotherFilterdTaskListScreen(filter);
    }
}
