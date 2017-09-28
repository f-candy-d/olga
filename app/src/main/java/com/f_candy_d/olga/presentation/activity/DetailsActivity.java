package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.f_candy_d.dutils.ColorUtils;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.structure.UnmodifiableNote;
import com.f_candy_d.olga.domain.usecase.NoteTableUseCase;
import com.f_candy_d.olga.presentation.adapter.ViewAdapter;
import com.f_candy_d.olga.presentation.dialog.SimpleAlertDialog;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity
        implements SimpleAlertDialog.ButtonClickListener {

    private static final int REQUEST_CODE_EDIT_TASK = 1111;

    private static final int TAG_ERROR_MESSAGE_DIALOG = 0;
    private static final int TAG_CONFIRM_DELETE_DIALOG = 1;

    private static final String EXTRA_TASK_ID = "task_id";

    private static final String RESULT_IS_MODIFIRED = "is_modifired";

    private ViewAdapter mAdapter;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ImageView mToggleHeaderButton;
    private long mTaskId;

    public static Bundle makeExtra(long taskId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_TASK_ID, taskId);
        return bundle;
    }

    public static boolean getResultIsModifired(Intent intent) {
        return (intent.getExtras() != null && intent.getExtras().getBoolean(RESULT_IS_MODIFIRED, false));
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
        UnmodifiableNote task = NoteTableUseCase.findTaskById(mTaskId);
        if (task == null) {
            task = new UnmodifiableNote();
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

        // # Header Toggle Button

        mToggleHeaderButton = (ImageView) findViewById(R.id.toggle_header_button);
        mToggleHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleHeaderSize();
            }
        });

        // # RecyclerView

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    private ViewAdapter createAdapter(RecyclerView recyclerView, UnmodifiableNote task) {
        ArrayList<View> itemViews = new ArrayList<>();
        return new ViewAdapter(itemViews);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void onEditButtonClick() {
        Intent intent = new Intent(this, NoteFormActivity.class);
        intent.putExtras(NoteFormActivity.makeExtra(mTaskId));
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_TASK && resultCode == RESULT_OK) {
            UnmodifiableNote task = NoteTableUseCase.findTaskById(mTaskId);
            if (task == null) {
                task = new UnmodifiableNote();
            }

            invalidate(task, mRecyclerView, mAdapter, mFab);
        }
    }

    /**
     * Only used inside {@link DetailsActivity#invalidate(UnmodifiableNote, RecyclerView, ViewAdapter, FloatingActionButton)}.
     */
    private boolean mIsEmptyListMode = false;

    private void invalidate(@NonNull UnmodifiableNote task,
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

        // # Theme Color

        findViewById(R.id.app_bar).setBackgroundColor(task.getThemeColor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ColorUtils.manipulateBrightness(task.getThemeColor(), 0.7f));
        }
    }

    /**
     * Only used inside {@link DetailsActivity#toggleHeaderSize()}.
     */
    private boolean mIsMinimumHeaderMode = false;
    private static final long HEADER_TOGGLE_ANIM_DURATION = 200;

    /**
     * Use up-arrow icon as a button
     */
    private void toggleHeaderSize() {
        float fromRotation;
        float toRotation;

        if (mIsMinimumHeaderMode) {
            mIsMinimumHeaderMode = false;
            fromRotation = 180f;
            toRotation = 360f;

            // Title
            TextView textView = (TextView) findViewById(R.id.task_title);
            textView.setSingleLine(false);
            // Description
            textView = (TextView) findViewById(R.id.task_description);
            textView.setSingleLine(false);

        } else {
            mIsMinimumHeaderMode = true;
            fromRotation = 0f;
            toRotation = 180f;

            // Title
            TextView textView = (TextView) findViewById(R.id.task_title);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            // Description
            textView = (TextView) findViewById(R.id.task_description);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        // Rotate Arrow icon
        RotateAnimation rotateAnimation = new RotateAnimation(fromRotation, toRotation,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setRepeatCount(0);
        rotateAnimation.setDuration(HEADER_TOGGLE_ANIM_DURATION);
        rotateAnimation.setFillAfter(true);
        mToggleHeaderButton.startAnimation(rotateAnimation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_delete) {
            new SimpleAlertDialog.Builder()
                    .message("Delete this task?")
                    .positiveButton("DELETE")
                    .negativeButton("CANCEL")
                    .tag(TAG_CONFIRM_DELETE_DIALOG)
                    .create()
                    .show(getSupportFragmentManager(), null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteTask() {
        // Delete task from the database
        boolean result = NoteTableUseCase.delete(mTaskId);
        if (!result) {
            // Show error message
            new SimpleAlertDialog.Builder()
                    .title("!!ERROR!!")
                    .message("Sorry, failed to delete this task...")
                    .positiveButton("OK")
                    .tag(TAG_ERROR_MESSAGE_DIALOG)
                    .create()
                    .show(getSupportFragmentManager(), null);
        }

        Intent intent = new Intent();
        intent.putExtra(RESULT_IS_MODIFIRED, result);
        int resultCode = (result) ? RESULT_OK : RESULT_CANCELED;
        setResult(resultCode, intent);
        finish();
    }

    /**
     * SimpleAlertDialog.ButtonClickListener implementation
     * --------------------------------------------------------------------------------------------- */

    @Override
    public void onPositiveButtonClick(int tag) {
        if (tag == TAG_CONFIRM_DELETE_DIALOG) {
            deleteTask();
        }
    }

    @Override
    public void onNegativeButtonClick(int tag) {
        // Nothing to do...
    }
}