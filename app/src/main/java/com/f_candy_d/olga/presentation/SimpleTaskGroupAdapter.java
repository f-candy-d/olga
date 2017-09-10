package com.f_candy_d.olga.presentation;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by daichi on 17/09/03.
 */

public class SimpleTaskGroupAdapter extends InnerListAdapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private ArrayList<Task> mTasks;
    private String mHeaderTitle;

    public SimpleTaskGroupAdapter() {
        mTasks = new ArrayList<>();
    }

    public SimpleTaskGroupAdapter(Collection<Task> tasks) {
        mTasks = new ArrayList<>(tasks);
    }

    public void addTask(Task task) {
        mTasks.add(task);
        notifyItemInserted(mTasks.size() - 1);
    }

    public void addTasks(Collection<Task> tasks) {
        int start = mTasks.size();
        mTasks.addAll(tasks);
        notifyItemRangeInserted(start, mTasks.size() - 1);
    }

    public void setHeaderTitle(String headerTitle) {
        mHeaderTitle = headerTitle;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = inflater.inflate(R.layout.simple_task_group_adapter_header, parent, false);
                return new HeaderViewHolder(view);

            case VIEW_TYPE_ITEM:
                view = inflater.inflate(R.layout.simple_task_group_adapter_item, parent, false);
                return new TaskViewHolder(view);

            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_HEADER: {
                ((HeaderViewHolder) holder).title.setText(mHeaderTitle);
                break;
            }

            case VIEW_TYPE_ITEM: {
                TaskViewHolder h = (TaskViewHolder) holder;
                h.title.setText(mTasks.get(getPositionOffsetFromFirstItem(position)).toSummary());
            }
        }
    }

    @Override
    public int getItemCount() {
        // items + header
        return mTasks.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        final int offsetFromFirstItem = getPositionOffsetFromFirstItem(position);
        if (offsetFromFirstItem == -1) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    private int getPositionOffsetFromFirstItem(int position) {
        return position - getFirstItemPosition();
    }

    public int getFirstItemPosition() {
        return 1;
    }

    /**
     * TaskViewHolder
     */
    private static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.simple_task_adapter_task_item_title);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public HeaderViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.simple_task_adapter_header_title);
        }
    }
}
