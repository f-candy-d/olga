package com.f_candy_d.olga.presentation;

import android.support.annotation.StringRes;
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
 * Created by daichi on 9/12/17.
 */

public class OldSimpleTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TASK = 0;
    private static final int VIEW_TYPE_EMPTY_VIEW = 1;

    private ArrayList<Task> mTasks;
    private OnBindItemCallback mOnBindItemCallback = null;
    @StringRes  private int mNoItemMessage = R.string.no_tasks_message;

    public OldSimpleTaskAdapter() {
        mTasks = new ArrayList<>();
    }

    public OldSimpleTaskAdapter(Collection<Task> tasks) {
        mTasks = new ArrayList<>(tasks);
    }

    public void setOnBindItemCallback(OnBindItemCallback onBindItemCallback) {
        mOnBindItemCallback = onBindItemCallback;
    }

    public void setNoItemMessage(@StringRes int noItemMessage) {
        mNoItemMessage = noItemMessage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_TASK) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_simple_task_adapter, parent, false);
            return new SimpleTaskViewHolder(view);

        } else if (viewType == VIEW_TYPE_EMPTY_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_empty_view, parent, false);
            return new EmptyViewHolder(view);

        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPE_TASK:

                SimpleTaskViewHolder vh = (SimpleTaskViewHolder) holder;
                Task task = mTasks.get(position);

                if (mOnBindItemCallback != null) {
                    StringBuffer title = new StringBuffer();
                    StringBuffer dateLabel = new StringBuffer();
                    mOnBindItemCallback.onDecorateItemData(task, title, dateLabel);

                    vh.title.setText(title);
                    vh.date_label.setText(dateLabel);

                } else {
                    vh.title.setText(task.title);
                    vh.date_label.setText(task.dateTermStart.toString());
                }

                break;


            case VIEW_TYPE_EMPTY_VIEW:
                ((EmptyViewHolder) holder).message.setText(mNoItemMessage);
                break;
        }
    }

    @Override
    public int getItemCount() {
        // If there are no items, show an empty view
        return (mTasks.size() != 0)
                ? mTasks.size()
                : 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (mTasks.size() != 0)
                ? VIEW_TYPE_TASK
                : VIEW_TYPE_EMPTY_VIEW;
    }

    /**
     * VH class
     */

    private static class SimpleTaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date_label;

        SimpleTaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.simple_task_adapter_task_item_title);
            date_label = view.findViewById(R.id.date_label);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        TextView message;

        EmptyViewHolder(View view) {
            super(view);
            message = view.findViewById(R.id.empty_item_message);
        }
    }

    /**
     * Callback interface
     */

    public interface OnBindItemCallback {
        void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel);
    }
}
