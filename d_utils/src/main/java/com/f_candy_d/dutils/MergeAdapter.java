package com.f_candy_d.dutils;


import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daichi on 17/09/01.
 */

public class MergeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static class SubAdapter {
        RecyclerView.Adapter adapter;
        // {@key::globalViewType @value::localViewType}
        SparseIntArray viewTypeMap;

        SubAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            viewTypeMap = new SparseIntArray();
        }

        int getLocalViewType(int globalViewType) {
            return viewTypeMap.get(globalViewType);
        }

        int getGlobalViewType(int localPosition){
            int localVt = adapter.getItemViewType(localPosition);
            int keyIndex = viewTypeMap.indexOfValue(localVt);
            return viewTypeMap.keyAt(keyIndex);
        }
    }

    private ArrayList<SubAdapter> mAdapters;
    private SparseIntArray mViewTypeAdapterPositionMap;

    public MergeAdapter() {
        mAdapters = new ArrayList<>();
        mViewTypeAdapterPositionMap = new SparseIntArray();
    }

    public void addView(View view) {
        addView(mAdapters.size(), view);
    }

    public void addView(int index, View view) {
        SingleViewAdapter adapter = new SingleViewAdapter(view);
        addAdapter(index, adapter);
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
        addAdapter(mAdapters.size(), adapter);
    }

    public void addAdapter(int index, RecyclerView.Adapter adapter) {
        SubAdapter subAdapter = new SubAdapter(adapter);
        mAdapters.add(index, subAdapter);
        mapViewTypes();
    }

    public RecyclerView.Adapter getAdapter(int index) {
        return mAdapters.get(index).adapter;
    }

    public <T extends RecyclerView.Adapter> T getAdapter(int index, Class<T> adapterClass) {
        return adapterClass.cast(getAdapter(index));
    }

    /**
     * Mapping view-types to adapter positions
     */
    private void mapViewTypes() {
        int itemCount, viewType, globalViewType = 0;
        SubAdapter subAdapter;
        mViewTypeAdapterPositionMap.clear();
        for (int i = 0; i < mAdapters.size(); ++i) {
            subAdapter = mAdapters.get(i);
            subAdapter.viewTypeMap.clear();
            itemCount = subAdapter.adapter.getItemCount();
            for (int j = 0; j < itemCount; ++j) {
                // local view type
                viewType = subAdapter.adapter.getItemViewType(j);
                if (subAdapter.viewTypeMap.indexOfValue(viewType) < 0) {
                    subAdapter.viewTypeMap.put(globalViewType, viewType);
                    ++globalViewType;
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Return a global view type
        int adapterPosition = getAdapterPositionOf(position);
        int localPosition = getLocalPositionOf(position, adapterPosition);
        return mAdapters.get(adapterPosition).getGlobalViewType(localPosition);
    }

    /**
     *
     * @param parent
     * @param viewType Global view type returned from #getItemViewType()
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int adapterPosition = mViewTypeAdapterPositionMap.get(viewType);
        SubAdapter subAdapter = mAdapters.get(adapterPosition);
        int localViewType = subAdapter.getLocalViewType(viewType);
        return subAdapter.adapter.onCreateViewHolder(parent, localViewType);
    }

    /**
     *
     * @param holder
     * @param position Global index
     */
    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int adapterPosition = getAdapterPositionOf(position);
        mAdapters.get(adapterPosition).adapter.onBindViewHolder(holder, getLocalPositionOf(position, adapterPosition));
    }

    @SuppressWarnings("unchecked cast")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        int adapterPosition = getAdapterPositionOf(position);
        mAdapters.get(adapterPosition).adapter.onBindViewHolder(holder, getLocalPositionOf(position, adapterPosition), payloads);
    }

    @Override
    public int getItemCount() {
        return getItemCountInRange(0, mAdapters.size());
    }

    /**
     * Count the number of items int each adapters in range of index.
     * @param startAdapterPosition Include this
     * @param endAdapterPosition Exclude this
     * @return Item count
     */
    private int getItemCountInRange(int startAdapterPosition, int endAdapterPosition) {
        int itemCount = 0;
        for (int i = startAdapterPosition; i < endAdapterPosition; ++i) {
            itemCount += mAdapters.get(i).adapter.getItemCount();
        }

        return itemCount;
    }

    private int getLocalPositionOf(int globalPosition, int adapterPosition) {
        return globalPosition - getItemCountInRange(0, adapterPosition);
    }

    private int getAdapterPositionOf(int globalPosition) {
        int itemCount = 0;
        for (int i = 0; i < mAdapters.size(); ++i) {
            itemCount += getItemCountInRange(i, i + 1);
            if (globalPosition < itemCount) {
                return i;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    private int getGlobalPositionOf(int adapterPosition, int localPosition) {
        return getItemCountInRange(0, mAdapters.size()) + localPosition;
    }
}
