package com.f_candy_d.dutils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by daichi on 9/11/17.
 *
 * Used in MergeAdapter class.
 */

public class SingleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(mView) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return 1;
    }
}
