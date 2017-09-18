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
 * Created by daichi on 9/18/17.
 */

public class SimpleTaskAdapter extends FullSpanItemAdapter<RecyclerView.ViewHolder> {

    private ArrayList<Task> mTasks;
    private Callback mCallback;

    public SimpleTaskAdapter() {
        mTasks = new ArrayList<>();
    }

    public SimpleTaskAdapter(Collection<Task> tasks) {
        mTasks = new ArrayList<>(tasks);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_simle_task_card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mCallback == null) return;

        StringBuffer title = new StringBuffer();
        StringBuffer time = new StringBuffer();
        StringBuffer location = new StringBuffer();
        OutVisibility outVisibility = new OutVisibility();
        mCallback.onBind(outVisibility, mTasks.get(position), title, time, location);

        TaskViewHolder vh = (TaskViewHolder) holder;

        // title
        vh.title.setText(title.toString());

        // time
        if (outVisibility.isTimeVisible) {
            vh.timeLayout.setVisibility(View.VISIBLE);
            vh.time.setText(time.toString());
        } else {
            vh.timeLayout.setVisibility(View.GONE);
        }

        // location
        if (outVisibility.isLocationVisible) {
            vh.locationLayout.setVisibility(View.VISIBLE);
            vh.location.setText(location.toString());
        } else {
            vh.locationLayout.setVisibility(View.GONE);
        }

        // done-mask
        if (mTasks.get(position).isDone) {
            vh.doneMask.setVisibility(View.VISIBLE);
        } else {
            vh.doneMask.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isFullSpan(int position) {
        if (mCallback != null) {
            return mCallback.isItemFullSpan(position);
        } else {
            return true;
        }
    }

    /**
     * ViewHolder class
     */

    private static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        TextView location;
        View timeLayout;
        View locationLayout;
        View doneMask;

        TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title_label);
            time = view.findViewById(R.id.time_label);
            location = view.findViewById(R.id.location_label);
            timeLayout = view.findViewById(R.id.layout_time_label);
            locationLayout = view.findViewById(R.id.layout_location_label);
            doneMask = view.findViewById(R.id.done_mask);
        }
    }

    /**
     * Callback interface
     */

    public interface Callback {
        boolean isItemFullSpan(int position);
        void onBind(OutVisibility outVisibility, Task task, StringBuffer title, StringBuffer time, StringBuffer location);
    }

    public class OutVisibility {
        boolean isTimeVisible = true;
        boolean isLocationVisible = true;
    }
}
