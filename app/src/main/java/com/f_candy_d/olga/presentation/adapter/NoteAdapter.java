package com.f_candy_d.olga.presentation.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.table_pool.NoteTablePool;
import com.f_candy_d.olga.domain.structure.Note;


/**
 * Created by daichi on 9/23/17.
 */

public class NoteAdapter extends FullSpanItemAdapter<RecyclerView.ViewHolder> {

    private NoteTablePool mTaskPool;

    public NoteAdapter(NoteTablePool taskPool) {
        mTaskPool = taskPool;
    }

    @Override
    protected boolean isFullSpan(int position) {
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mTaskPool.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TaskViewHolder vh = (TaskViewHolder) holder;
        Note note = mTaskPool.getAt(position);

        // # Title

        if (note.getTitle() == null) {
            vh.title.setText(R.string.empty_task_title);
        } else {
            vh.title.setText(note.getTitle());
        }

        // # Description

        if (note.getDescription() == null) {
            vh.description.setVisibility(View.GONE);
        } else {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(note.getDescription());
        }

        // # Achieved Masc

        if (note.isAchieved()) {
            vh.achievedMask.setVisibility(View.VISIBLE);
        } else {
            vh.achievedMask.setVisibility(View.GONE);
        }

        // # Theme Color

        vh.background.setCardBackgroundColor(note.getThemeColor());
    }

    /**
     * region; ViewHolder
     */

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        View achievedMask;
        CardView background;

        TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.task_card_task_title);
            description = view.findViewById(R.id.task_card_task_description);
            achievedMask = view.findViewById(R.id.achieved_mask);
            background = view.findViewById(R.id.task_card_bg);
        }
    }
}
