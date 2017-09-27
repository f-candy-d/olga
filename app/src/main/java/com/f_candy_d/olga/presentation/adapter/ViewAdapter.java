package com.f_candy_d.olga.presentation.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by daichi on 9/27/17.
 */

public class ViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int INVAILED_POSITION = -1;

    private ArrayList<View> mViews;

    public ViewAdapter() {
        mViews = new ArrayList<>();
    }

    public ViewAdapter(View... views) {
        this(Arrays.asList(views));
    }

    public ViewAdapter(Collection<View> views) {
        mViews = new ArrayList<>(views);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Each items should be different views, so viewType is item's position.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(mViews.get(viewType)) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return mViews.size();
    }

    /**
     * ADD VIEW
     * ------------------------------------------------------------ */

    public void addView(View view) {
        addView(mViews.size(), view);
    }

    public void addView(int position, View view) {
        mViews.add(position, view);
        notifyItemInserted(position);
    }

    public void addViews(View... views) {
        addViews(mViews.size(), Arrays.asList(views));
    }

    public void addViews(int position, View... views) {
        addViews(position, Arrays.asList(views));
    }

    public void addViews(Collection<View> views) {
        addViews(mViews.size(), views);
    }

    public void addViews(int position, Collection<View> views) {
        mViews.addAll(position, views);
        notifyItemRangeInserted(position, views.size());
    }

    /**
     * REMOVE VIEW
     * --------------------------------------------------------------- */

    public void removeView(View view) {
        int position = mViews.indexOf(view);
        removeView(position);
    }

    public View removeView(int position) {
        View view = mViews.remove(position);
        notifyItemRemoved(position);
        return view;
    }

    public void removeViewsInRange(int start, int count) {
        Iterator<View> iterator = mViews.iterator();
        for (int i = 0; i < start + count; ++i, iterator.next()) {
            if (start <= i) {
                iterator.remove();
            }
        }

        notifyItemRangeRemoved(start, count);
    }

    public void clearViews() {
        int size = mViews.size();
        mViews.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * CHANGE VIEW
     * ---------------------------------------------------------------- */

    public void changeView(int position, View view) {
        mViews.set(position, view);
        notifyItemChanged(position);
    }

    public void changeViews(int start, View... views) {
        changeViews(start, Arrays.asList(views));
    }

    public void changeViews(int start, Collection<View> views) {
        int position = start;
        for (View view : views) {
            mViews.set(position, view);
            ++position;
        }

        notifyItemRangeChanged(start, views.size());
    }

    public void swapDataSet(View... views) {
        swapDataSet(Arrays.asList(views));
    }

    public void swapDataSet(Collection<View> views) {
        mViews.clear();
        mViews.addAll(views);
        notifyDataSetChanged();
    }

    /**
     * GET VIEW
     * ---------------------------------------------------------------------- */

    public View getViewAt(int position) {
        return mViews.get(position);
    }

    public Collection<View> getAllViews() {
        return mViews;
    }

    /**
     * UTILS
     * --------------------------------------------------------------------- */

    public int positionOf(View view) {
        int result = mViews.indexOf(view);
        return (result != -1) ? result : INVAILED_POSITION;
    }

    public static View inflateView(@LayoutRes int layout, RecyclerView recyclerView) {
        return LayoutInflater.from(recyclerView.getContext())
                .inflate(layout, recyclerView, false);
    }
}
