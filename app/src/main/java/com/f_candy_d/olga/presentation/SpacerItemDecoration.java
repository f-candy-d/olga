package com.f_candy_d.olga.presentation;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by daichi on 17/09/03.
 *
 * Use with LinearLayoutManager (VERTICAL).
 * LinearLayoutManager (HORIZONTAL) and the other LayoutManagers are not supported.
 */

public class SpacerItemDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;
    private Callback mCallback;

    public SpacerItemDecoration(Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mCallback != null) {
            mCallback.getInsertedSpaceAroundItem(parent.getChildAdapterPosition(view), outRect);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    /**
     * Callback interface
     */
    public interface Callback {
        /**
         * Determine left, top, right, bottom side's spaces of an item.
         * Default space sizes are (0, 0, 0, 0).
         * @param adapterPosition Item's adapter position
         * @param output Rect to receive the output (in pixel size)
         */
        void getInsertedSpaceAroundItem(int adapterPosition, Rect output);
    }
}
