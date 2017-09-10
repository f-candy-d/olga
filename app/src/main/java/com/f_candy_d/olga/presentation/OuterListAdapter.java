package com.f_candy_d.olga.presentation;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f_candy_d.olga.R;

import java.util.ArrayList;

/**
 * Created by daichi on 9/7/17.
 */

public class OuterListAdapter extends RecyclerView.Adapter<OuterListAdapter.OuterListViewHolder> {

    private Context mContext;
    private ArrayList<InnerListAdapter> mAdapters;

    public OuterListAdapter(Context context) {
        mContext = context;
        mAdapters = new ArrayList<>();
    }

    public void addAdapter(InnerListAdapter adapter) {
        mAdapters.add(adapter);
    }

    public void addAdapterAndNotify(InnerListAdapter adapter) {
        addAdapter(adapter);
        notifyItemInserted(mAdapters.size() - 1);
    }

    @Override
    public OuterListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_contains_listview, parent, false);

        return new OuterListViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(OuterListViewHolder holder, int position) {
        InnerListAdapter innerAdapter = mAdapters.get(position);
        holder.recyclerView.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return mAdapters.size();
    }

    static final class OuterListViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;

        OuterListViewHolder(final View view, Context context) {
            super(view);
            recyclerView = view.findViewById(R.id.recyclerview_inside_cardview);
            // Enable touch event of items in the nested recycler view, and disable scrolling of it.
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

//            ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//                @Override
//                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                    return false;
//                }
//
//                @Override
//                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                }
//            };
//
//            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//            touchHelper.attachToRecyclerView(recyclerView);
        }
    }
}
