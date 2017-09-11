package com.f_candy_d.dutils;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 17/09/01.
 */

public class MergeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Contains View or RecyclerView.Adapter
    // Indexes of this array means ContentIndex
    private ArrayList<Object> mContents;
    // Same order as mContents
    private ArrayList<Boolean> mIsFullSpanFlags;

    public MergeAdapter() {
        mContents = new ArrayList<>();
        mIsFullSpanFlags = new ArrayList<>();
    }

    public void addView(View view) {
        addView(mContents.size(), view, false);
    }

    public void addView(int index, View view) {
        addView(index, view, false);
    }

    public void addView(View view, boolean isFullSpan) {
        addView(mContents.size(), view, isFullSpan);
    }

    public void addView(int index, View view, boolean isFullSpan) {
        mContents.add(index, view);
        mIsFullSpanFlags.add(isFullSpan);
    }

    public void addAdapter(RecyclerView.Adapter adapter, boolean isFullSpan) {
        addAdapter(mContents.size(), adapter, isFullSpan);
    }

    public void addAdapter(int index, RecyclerView.Adapter adapter, boolean isFullSpan) {
        mContents.add(index, adapter);
        mIsFullSpanFlags.add(isFullSpan);
    }

    public void addAdapter(RecyclerView.Adapter adapter) {
        addAdapter(mContents.size(), adapter, false);
    }

    public void addAdapter(int index, RecyclerView.Adapter adapter) {
        addAdapter(index, adapter, false);
    }

    @Override
    public int getItemViewType(int position) {
        // Return a global index
        return position;
    }

    /**
     *
     * @param parent
     * @param viewType Global index of an item returned from #getItemViewType()
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Object content = mContents.get(getContentIndexOf(viewType));
        if (content instanceof View) {
            return new SingleViewHolder((View) content);

        } else if (content instanceof RecyclerView.Adapter) {
            int contentIndex = getContentIndexOf(viewType);
            RecyclerView.Adapter adapter = (RecyclerView.Adapter) mContents.get(contentIndex);
            int localViewType = adapter.getItemViewType(getLocalIndexOf(viewType, contentIndex));
            return adapter.onCreateViewHolder(parent, localViewType);

        } else {
            // 'content' must be a object of View or RecyclerView.Adapter
            throw new IllegalStateException();
        }
    }

    /**
     *
     * @param holder
     * @param position Global index
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int contentIndex = getContentIndexOf(position);
        Object content = mContents.get(contentIndex);
        if (content instanceof RecyclerView.Adapter) {
            ((RecyclerView.Adapter) content).onBindViewHolder(holder, getLocalIndexOf(position, contentIndex));
        }
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        int contentIndex = getContentIndexOf(position);
        Object content = mContents.get(contentIndex);
        if (content instanceof RecyclerView.Adapter) {
            ((RecyclerView.Adapter) content).onBindViewHolder(holder, getLocalIndexOf(position, contentIndex), payloads);
        }
    }

    @Override
    public int getItemCount() {
        return getItemCountInRange(0, mContents.size());
    }

    /**
     * Count the number of items int each contents in range of index.
     * @param startContentIndex Include this
     * @param endContentIndex Exclude this
     * @return Item count
     */
    private int getItemCountInRange(int startContentIndex, int endContentIndex) {
        int itemCount = 0;
        Object content;
        for (int i = startContentIndex; i < endContentIndex; ++i) {
            content = mContents.get(i);

            if (content instanceof View) {
                ++itemCount;
            } else if (content instanceof RecyclerView.Adapter){
                itemCount += ((RecyclerView.Adapter) content).getItemCount();
            }
        }

        return itemCount;
    }

    private int getLocalIndexOf(int globalIndex, int contentIndex) {
        return globalIndex - getItemCountInRange(0, contentIndex);
    }

    private int getContentIndexOf(int globalIndex) {
        int itemCount = 0;
        for (int i = 0; i < mContents.size(); ++i) {
            itemCount += getItemCountInRange(i, i + 1);
            if (globalIndex < itemCount) {
                return i;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    private int getGlobalIndexOf(int contentIndex, int localIndex) {
        return getItemCountInRange(0, mContents.size()) + localIndex;
    }

    /**
     * TaskViewHolder for a single view
     */
    private static class SingleViewHolder extends RecyclerView.ViewHolder {

        SingleViewHolder(View view) {
            super(view);
        }
    }
}
