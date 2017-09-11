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
 * Created by daichi on 9/12/17.
 */

public class SimpleTaskAdapter extends RecyclerView.Adapter<SimpleTaskAdapter.SimpleTaskViewHolder> {

    private ArrayList<Task> mTasks;
    private OnBindItemCallback mOnBindItemCallback = null;

    public SimpleTaskAdapter() {
        mTasks = new ArrayList<>();
    }

    public SimpleTaskAdapter(Collection<Task> tasks) {
        mTasks = new ArrayList<>(tasks);
    }

    public void setOnBindItemCallback(OnBindItemCallback onBindItemCallback) {
        mOnBindItemCallback = onBindItemCallback;
    }

    @Override
    public SimpleTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_task_adapter_item, parent, false);

        return new SimpleTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleTaskViewHolder holder, int position) {
        Task task = mTasks.get(position);

        if (mOnBindItemCallback != null) {
            StringBuffer title = new StringBuffer();
            StringBuffer dateLabel = new StringBuffer();
            mOnBindItemCallback.onDecorateItemData(task, title, dateLabel);

            holder.title.setText(title);
            holder.date_label.setText(dateLabel);

        } else {
            holder.title.setText(task.title);
            holder.date_label.setText(task.dateTermStart.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    /**
     * VH class
     */

    static class SimpleTaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date_label;

        SimpleTaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.simple_task_adapter_task_item_title);
            date_label = view.findViewById(R.id.date_label);
        }
    }

    /**
     * Callback interface
     */

    public interface OnBindItemCallback {
        void onDecorateItemData(Task task, StringBuffer title, StringBuffer dateLabel);
    }
}
