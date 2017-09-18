package com.f_candy_d.olga.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by daichi on 9/18/17.
 */

public class FullSpanViewAdapter extends FullSpanItemAdapter<RecyclerView.ViewHolder> {

    private ArrayList<View> mViews;

    public FullSpanViewAdapter() {
        mViews = new ArrayList<>();
    }

    public FullSpanViewAdapter(View... views) {
        mViews = new ArrayList<>(Arrays.asList(views));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mViews.get(viewType));
    }

    @Override
    public int getItemCount() {
        return mViews.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
        }
    }

    @Override
    protected boolean isFullSpan(int position) {
        return true;
    }
}
