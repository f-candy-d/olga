package com.f_candy_d.olga.presentation.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.structure.Task;
import com.f_candy_d.olga.domain.structure.UnmodifiableTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by daichi on 9/23/17.
 */

public class TaskAdapter extends FullSpanItemAdapter<RecyclerView.ViewHolder> {

    private SortedList<UnmodifiableTask> mTasks;

    public TaskAdapter() {
        initList();
    }

    public TaskAdapter(Collection<UnmodifiableTask> tasks) {
        initList();
        addAll(tasks);
    }

    public TaskAdapter(UnmodifiableTask[] tasks) {
        initList();
        addAll(tasks);
    }

    private void initList() {
        mTasks = new SortedList<>(UnmodifiableTask.class,
                new SortedList.Callback<UnmodifiableTask>() {
                    @Override
                    public int compare(UnmodifiableTask o1, UnmodifiableTask o2) {
                        return Long.valueOf(o1.getId()).compareTo(o2.getId());
                    }

                    @Override
                    public void onInserted(int position, int count) {
                        notifyItemRangeInserted(position, count);
                    }

                    @Override
                    public void onRemoved(int position, int count) {
                        notifyItemRangeRemoved(position, count);
                    }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        notifyItemMoved(fromPosition, toPosition);
                    }

                    @Override
                    public void onChanged(int position, int count) {
                        notifyItemRangeChanged(position, count);
                    }

                    @Override
                    public boolean areContentsTheSame(UnmodifiableTask oldItem, UnmodifiableTask newItem) {
                        return oldItem.equals(newItem);
                    }

                    @Override
                    public boolean areItemsTheSame(UnmodifiableTask item1, UnmodifiableTask item2) {
                        return item1.getId() == item2.getId();
                    }
                });
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
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TaskViewHolder vh = (TaskViewHolder) holder;
        UnmodifiableTask task = get(position);

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

    public UnmodifiableTask get(int position) {
        return mTasks.get(position);
    }

    public int add(UnmodifiableTask task) {
        return mTasks.add(task);
    }

    public int indexOf(UnmodifiableTask task) {
        return mTasks.indexOf(task);
    }

    public void updateItemAt(int position, UnmodifiableTask task) {
        mTasks.updateItemAt(position, task);
    }

    public void addAll(Collection<UnmodifiableTask> tasks) {
        mTasks.addAll(tasks);
    }

    public void addAll(UnmodifiableTask[] tasks) {
        mTasks.addAll(tasks);
    }

    public boolean remove(UnmodifiableTask task) {
        return mTasks.remove(task);
    }

    public UnmodifiableTask removeTaskAt(int position) {
        return mTasks.removeItemAt(position);
    }

    public void clear() {
        mTasks.clear();
    }

    /**
     * region; ViewHolder
     */

    private static class TaskViewHolder extends RecyclerView.ViewHolder {

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
