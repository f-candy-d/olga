package com.f_candy_d.olga.presentation.adapter;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.filter.DefaultFilterFactory;
import com.f_candy_d.olga.domain.filter.TaskFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by daichi on 9/27/17.
 */

public class DefaultTaskFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<TaskFilter> mFilters;

    public DefaultTaskFilterAdapter() {
        TaskFilter[] defaultFilters = new TaskFilter[] {
                DefaultFilterFactory.createAllFilter(),
                DefaultFilterFactory.createAchievedFilter(),
                DefaultFilterFactory.createNotAchievedFilter()
        };

        mFilters = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(defaultFilters)));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_filter_summary, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder vh = (ItemViewHolder) holder;
        vh.filterName.setText(mFilters.get(position).getFilterName());
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    public TaskFilter getAt(int position) {
        return mFilters.get(position);
    }

    /**
     * View Holder
     */

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView filterName;

        ItemViewHolder(View view) {
            super(view);
            filterName = view.findViewById(R.id.filter_name);
        }
    }
}
