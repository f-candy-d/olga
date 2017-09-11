package com.f_candy_d.dutils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 9/11/17.
 *
 * Used in MergeAdapter class.
 */

public class SingleViewAdapter extends RecyclerView.Adapter<SingleViewAdapter.SingleViewHolder> {

    private View mView;

    public SingleViewAdapter() {
        this(null);
    }

    public SingleViewAdapter(View view) {
        mView = view;
    }

    public View getView() {
        return mView;
    }

    public void setView(View view) {
        mView = view;
    }

    @Override
    public SingleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SingleViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(SingleViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * VH class
     */
    static class SingleViewHolder extends RecyclerView.ViewHolder {

        SingleViewHolder(View view) {
            super(view);
        }
    }
}
