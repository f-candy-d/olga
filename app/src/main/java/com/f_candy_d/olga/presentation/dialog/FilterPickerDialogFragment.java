package com.f_candy_d.olga.presentation.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f_candy_d.dutils.MergeAdapter;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.filter.TaskFilter;
import com.f_candy_d.olga.presentation.ItemClickHelper;
import com.f_candy_d.olga.presentation.adapter.DefaultTaskFilterAdapter;

import me.mvdw.recyclerviewmergeadapter.adapter.RecyclerViewMergeAdapter;

public class FilterPickerDialogFragment extends DialogFragment {

    private OnFilterSelectListener mListener;

    public FilterPickerDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FilterPickerDialogFragment.
     */
    public static FilterPickerDialogFragment newInstance() {
        FilterPickerDialogFragment fragment = new FilterPickerDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_filter_picker_dialog, container, false);

        // # Toolbar

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Select A Filter");
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSelf();
            }
        });

        // # RecyclerView

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // # Adapter

        final RecyclerViewMergeAdapter adapter = new RecyclerViewMergeAdapter();
        inflater = LayoutInflater.from(recyclerView.getContext());

        // Header for default filters
        View header = inflater.inflate(R.layout.item_header_basic, recyclerView, false);
        ((TextView) header.findViewById(R.id.header_title)).setText("Defaults");
        adapter.addView(header);

        // Default filters
        final DefaultTaskFilterAdapter defaultTaskFilterAdapter = new DefaultTaskFilterAdapter();
        adapter.addAdapter(defaultTaskFilterAdapter);

        recyclerView.setAdapter(adapter);

        // # ItemClickHelper

        ItemClickHelper itemClickHelper = new ItemClickHelper(new ItemClickHelper.Callback() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof DefaultTaskFilterAdapter.ItemViewHolder) {
                    int localPosition = adapter.getPosSubAdapterInfoForGlobalPosition(viewHolder.getAdapterPosition()).posInSubAdapter;
                    mListener.onFilterSelected(defaultTaskFilterAdapter.getAt(localPosition));
                    closeSelf();
                }
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder viewHolder) {
                // Nothing to do
            }
        });

        itemClickHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFilterSelectListener) {
            mListener = (OnFilterSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFilterSelectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void closeSelf() {
        dismiss();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFilterSelectListener {
        void onFilterSelected(TaskFilter filter);
    }
}
