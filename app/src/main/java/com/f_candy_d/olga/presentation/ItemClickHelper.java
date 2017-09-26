package com.f_candy_d.olga.presentation;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daichi on 9/26/17.
 */

public class ItemClickHelper implements RecyclerView.OnChildAttachStateChangeListener {

    public interface Callback {
        void onItemClick(RecyclerView.ViewHolder viewHolder);
        void onItemLongClick(RecyclerView.ViewHolder viewHolder);
    }

    private RecyclerView mRecyclerView;
    @NonNull private Callback mCallback;

    public ItemClickHelper(@NonNull Callback callback) {
        mCallback = callback;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (mRecyclerView == recyclerView) {
            return;
        }
        if (mRecyclerView != null) {
            dettachFromRecyclerView();
        }
        if (recyclerView != null) {
            recyclerView.addOnChildAttachStateChangeListener(this);
        }

        mRecyclerView = recyclerView;
    }

    public void dettachFromRecyclerView() {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnChildAttachStateChangeListener(this);
            mRecyclerView = null;
        }
    }

    /**
     * RecyclerView.OnChildAttachStateChangeListener implementation
     */

    @Override
    public void onChildViewAttachedToWindow(View view) {
        view.setOnClickListener(ON_CLICK_LISTENER);
        view.setOnLongClickListener(ON_LONG_CLICK_LISTENER);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        view.setOnClickListener(null);
        view.setOnLongClickListener(null);
    }

    private final View.OnClickListener ON_CLICK_LISTENER = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mRecyclerView != null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(view);
                if (vh != null) {
                    mCallback.onItemClick(vh);
                }
            }
        }
    };

    private final View.OnLongClickListener ON_LONG_CLICK_LISTENER = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mRecyclerView != null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(view);
                if (vh != null) {
                    mCallback.onItemLongClick(vh);
                }
            }

            return true;
        }
    };
}
