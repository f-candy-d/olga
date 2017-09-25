package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;
import com.f_candy_d.olga.presentation.view_model.TaskFormViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class TaskFormActivity extends ViewActivity implements TaskFormViewModel.SaveResultListener {

    private static final String EXTRA_TASK_ID = "task_id";

    private TaskFormViewModel mViewModel;
    private RecyclerViewMergeAdapter mFormCardAdapter;

    public static Bundle makeExtra(long taskId) {
        Bundle extras = new Bundle();
        extras.putLong(EXTRA_TASK_ID, taskId);
        return extras;
    }

    @Override
    protected ActivityViewModel onCreateViewModel() {
        long id = getIntent().getLongExtra(EXTRA_TASK_ID, DbContract.NULL_ID);
        if (id == DbContract.NULL_ID) {
            mViewModel = new TaskFormViewModel(this);
        } else {
            mViewModel = new TaskFormViewModel(this, id);
        }

        return mViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        // # FAB

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.onSave();
            }
        });

        // # RecyclerView

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set top padding to match the status bar height
        recyclerView.setPadding(0, Utils.getStatusBarHeight(), 0, 0);

        // # Adapter

        mFormCardAdapter = new RecyclerViewMergeAdapter();
        LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());

        // Header
        View itemView = inflater.inflate(R.layout.item_form_header_with_navigation_back_button, recyclerView, false);
        setupHeaderItemView(itemView);
        mFormCardAdapter.addView(itemView);
        // Core fields
        itemView = inflater.inflate(R.layout.item_task_form, recyclerView, false);
        setupTaskFormItemView(itemView);
        mFormCardAdapter.addView(itemView);
        // Footer
        itemView = inflater.inflate(R.layout.item_footer_with_add_option_button, recyclerView, false);
        setupFooterItemView(itemView);
        mFormCardAdapter.addView(itemView);

        recyclerView.setAdapter(mFormCardAdapter);
    }

    private void onDiscardButtonClick() {
        onBackPressed();
    }

    private void onAddOptionButtonClick() {
        Toast.makeText(this, "Add new option", Toast.LENGTH_SHORT).show();
    }

    /**
     * region; Setup form item view
     */

    private void setupHeaderItemView(View itemView) {
        // Navigation back button
        itemView.findViewById(R.id.nav_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDiscardButtonClick();
            }
        });
    }

    private void setupTaskFormItemView(View itemView) {
        UnmodifiableTask taskData = mViewModel.getTaskData();
        // EditText for the Task's title
        EditText editText = itemView.findViewById(R.id.edit_text_title);
        editText.setText(taskData.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.onInputTaskTitle(editable.toString());
            }
        });

        // EditText for the Task's description
        editText = itemView.findViewById(R.id.edit_text_description);
        editText.setText(taskData.getDescription());
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.onInputTaskDescription(editable.toString());
            }
        });
    }

    private void setupFooterItemView(View itemView) {
        // Add option button
        itemView.findViewById(R.id.add_option_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddOptionButtonClick();
            }
        });
    }

    /**
     * region; TaskFormViewModel.SaveResultListener interface
     */

    @Override
    public void onSaveSuccessful(long taskId) {
        Toast.makeText(this, "Save successful! @id=" + taskId, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onSaveFailed() {
        Toast.makeText(this, "Save failed...", Toast.LENGTH_SHORT).show();
        finish();
    }
}
