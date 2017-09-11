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
    private ArrayList<RecyclerView.Adapter> mContents;

    public MergeAdapter() {
        mContents = new ArrayList<>();
    }

    public void addView(View view) {
        addView(mContents.size(), view);
    }

    public void addView(int index, View view) {
        SingleViewAdapter adapter = new SingleViewAdapter(view);
        mContents.add(index, adapter);
    }

    public View getView(int index) {
        RecyclerView.Adapter adapter = getAdapter(index);
        if (adapter instanceof SingleViewAdapter) {
            return ((SingleViewAdapter) adapter).getView();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addAdapter(RecyclerView.Adapter adapter) {
        addAdapter(mContents.size(), adapter);
    }

    public void addAdapter(int index, RecyclerView.Adapter adapter) {
        mContents.add(index, adapter);
    }

    public RecyclerView.Adapter getAdapter(int index) {
        return mContents.get(index);
    }

    public <T extends RecyclerView.Adapter> T getAdapter(int index, Class<T> adapterClass) {
        return adapterClass.cast(getAdapter(index));
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
        int contentIndex = getContentIndexOf(viewType);
        RecyclerView.Adapter adapter = mContents.get(contentIndex);
        int localViewType = adapter.getItemViewType(getLocalIndexOf(viewType, contentIndex));
        return adapter.onCreateViewHolder(parent, localViewType);
    }

    /**
     *
     * @param holder
     * @param position Global index
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int adpPos = holder.getAdapterPosition();
        int contentIndex = getContentIndexOf(adpPos);
        RecyclerView.Adapter content = mContents.get(contentIndex);
        content.onBindViewHolder(holder, getLocalIndexOf(adpPos, contentIndex));
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        int adpPos = holder.getAdapterPosition();
        int contentIndex = getContentIndexOf(adpPos);
        RecyclerView.Adapter content = mContents.get(contentIndex);
        content.onBindViewHolder(holder, getLocalIndexOf(adpPos, contentIndex));
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
        for (int i = startContentIndex; i < endContentIndex; ++i) {
            itemCount += mContents.get(i).getItemCount();
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
}
