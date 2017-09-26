package com.f_candy_d.olga.presentation.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.SqliteTablePool;
import com.f_candy_d.olga.domain.TaskTablePool;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;

import java.util.Collection;

/**
 * Created by daichi on 9/23/17.
 */

public class TaskAdapter extends FullSpanItemAdapter<RecyclerView.ViewHolder> {

    private TaskTablePool mTaskPool;

    public TaskAdapter(TaskTablePool taskPool) {
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
        UnmodifiableTask task = mTaskPool.getAt(position);

        // # Title

        if (task.getTitle() == null) {
            vh.title.setText(R.string.empty_task_title);
        } else {
            vh.title.setText(task.getTitle());
        }

        // # Description

        if (task.getDescription() == null) {
            vh.description.setVisibility(View.GONE);
        } else {
            vh.description.setVisibility(View.VISIBLE);
            vh.description.setText(task.getDescription());
        }

        // # Achieved Masc

        if (task.isAchieved()) {
            vh.achievedMask.setVisibility(View.VISIBLE);
        } else {
            vh.achievedMask.setVisibility(View.GONE);
        }
    }

    /**
     * region; ViewHolder
     */

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        View achievedMask;

        TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.task_card_task_title);
            description = view.findViewById(R.id.task_card_task_description);
            achievedMask = view.findViewById(R.id.achieved_mask);
        }
    }
}
