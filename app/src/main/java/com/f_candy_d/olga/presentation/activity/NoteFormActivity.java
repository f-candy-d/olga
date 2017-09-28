package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.f_candy_d.dutils.view.ToggleColorBackground;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.structure.Note;
import com.f_candy_d.olga.presentation.dialog.SimpleAlertDialog;
import com.f_candy_d.olga.domain.NoteFormManager;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class NoteFormActivity extends AppCompatActivity
        implements NoteFormManager.SaveResultListener,
        SimpleAlertDialog.ButtonClickListener {

    private static final String EXTRA_TASK_ID = "task_id";
    private static final String RESULT_SAVED_TASK_ID = EXTRA_TASK_ID;

    private NoteFormManager mFormManager;
    private RecyclerViewMergeAdapter mFormCardAdapter;
    private ToggleColorBackground mToggleColorBg;

    public static Bundle makeExtra(long taskId) {
        Bundle extras = new Bundle();
        extras.putLong(EXTRA_TASK_ID, taskId);
        return extras;
    }

    public static long getResultSavedTaskId(Intent intent) {
        if (intent.getExtras() != null) {
            return intent.getExtras().getLong(RESULT_SAVED_TASK_ID, DbContract.NULL_ID);
        } else {
            return DbContract.NULL_ID;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        long id = getIntent().getLongExtra(EXTRA_TASK_ID, DbContract.NULL_ID);
        if (id == DbContract.NULL_ID) {
            mFormManager = new NoteFormManager(this);
        } else {
            mFormManager = new NoteFormManager(this, id);
        }

        onCreateUI();
    }

    private void onCreateUI() {
        // # FAB

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFormManager.onSave();
            }
        });

        // # ToggleColorBg

        mToggleColorBg = (ToggleColorBackground) findViewById(R.id.toggle_color_bg);
        mToggleColorBg.setCurrentColor(mFormManager.getTaskData().getThemeColor());

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
        itemView = inflater.inflate(R.layout.item_task_form_panel, recyclerView, false);
        setupTaskFormItemView(itemView);
        mFormCardAdapter.addView(itemView);
        // Footer
        itemView = inflater.inflate(R.layout.item_add_option_button, recyclerView, false);
        setupFooterItemView(itemView);
        mFormCardAdapter.addView(itemView);

        recyclerView.setAdapter(mFormCardAdapter);
    }

    private void onDiscardButtonClick() {
        new SimpleAlertDialog.Builder()
                .message("Are you sure want to discard this note?")
                .positiveButton("KEEP EDITING")
                .negativeButton("DISCARD")
                .create()
                .show(getSupportFragmentManager(), null);
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
        final Note taskData = mFormManager.getTaskData();
        // EditText for the Note's title
        EditText editText = itemView.findViewById(R.id.edit_text_title);
        editText.setText(taskData.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mFormManager.onInputTaskTitle(editable.toString());
            }
        });

        // EditText for the Note's description
        editText = itemView.findViewById(R.id.edit_text_description);
        editText.setText(taskData.getDescription());
        editText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mFormManager.onInputTaskDescription(editable.toString());
            }
        });

        // # Select Theme Color Button
        TypedArray a = getResources().obtainTypedArray(R.array.task_theme_colors);
        final int[] colors = new int[a.length()];
        for (int i = 0; i < a.length(); ++i) {
            colors[i] = a.getColor(i, 0);
        }
        a.recycle();

        itemView.findViewById(R.id.select_theme_color_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int paletteColumnCount = 4;
                ColorPickerDialog picker = new ColorPickerDialog();
                picker.initialize(R.string.color_picker_title, colors, taskData.getThemeColor(), paletteColumnCount, colors.length);
                picker.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mFormManager.onInputTaskThemeColor(color);
                        mToggleColorBg.toggleColor(color);
                    }
                });
                picker.show(getFragmentManager(), null);
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
     * region; NoteFormManager.SaveResultListener interface
     */

    @Override
    public void onSaveSuccessful(long taskId) {
        Toast.makeText(this, "Save successful! @id=" + taskId, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra(RESULT_SAVED_TASK_ID, taskId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSaveFailed() {
        Toast.makeText(this, "Save failed...", Toast.LENGTH_SHORT).show();
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * SimpleAlertDialog.ButtonClickListener implementation
     * ------------------------------------------------------------------------ */

    @Override
    public void onPositiveButtonClick(int tag) {
        // Nothing to do...
    }

    @Override
    public void onNegativeButtonClick(int tag) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
