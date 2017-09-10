package com.f_candy_d.olga.presentation;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by daichi on 17/09/03.
 *
 * Use with LinearLayoutManager (VERTICAL).
 * LinearLayoutManager (HORIZONTAL) is not supported.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Callback mCallback;
    private Drawable mDivider;

    public DividerItemDecoration() {}

    public DividerItemDecoration(Callback callback, Drawable divider) {
        mCallback = callback;
        mDivider = divider;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setDivider(Drawable divider) {
        mDivider = divider;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public Drawable getDivider() {
        return mDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (mCallback != null &&
                mCallback.drawDividerAboveItem(itemPosition)) {
            outRect.top = mDivider.getIntrinsicHeight();
            Log.d("mylog", "item position ->" + itemPosition);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            return;
        }

        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; ++i) {
            if (mCallback != null && mCallback.drawDividerAboveItem(i)) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

//                int dividerTop = child.getBottom() + params.bottomMargin;
//                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight()
                int dividerTop = child.getTop() - params.topMargin - mDivider.getIntrinsicHeight();
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft + params.leftMargin, dividerTop, dividerRight - params.rightMargin, dividerBottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * Callback interface
     */
    public interface Callback {
        boolean drawDividerAboveItem(int adapterPosition);
    }
}
