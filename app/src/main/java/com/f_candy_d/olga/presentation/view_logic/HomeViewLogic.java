package com.f_candy_d.olga.presentation.view_logic;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.dutils.Group;
import com.f_candy_d.olga.R;
import com.f_candy_d.olga.domain.Task;
import com.f_candy_d.olga.presentation.OuterListAdapter;
import com.f_candy_d.olga.presentation.SimpleTaskGroupAdapter;
import com.f_candy_d.olga.presentation.SpacerItemDecoration;
import com.f_candy_d.olga.presentation.view_model.HomeViewLogicInterface;
import com.f_candy_d.olga.presentation.view_model.HomeViewModelInterface;
import com.f_candy_d.vmvl.ActivityViewLogic;

import java.util.ArrayList;

/**
 * Created by daichi on 9/10/17.
 */

public class HomeViewLogic extends ActivityViewLogic implements HomeViewLogicInterface {

    private HomeViewModelInterface mViewModel;

    public HomeViewLogic(HomeViewModelInterface viewModelInterface) {
        super(viewModelInterface);
        mViewModel = viewModelInterface;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);

//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.triggerAddNewEvent();
            }
        });
    }

    @Override
    public boolean onCreateOptionMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showInitialUpcomingTasks(ArrayList<Group<Task>> taskGroups) {
        OuterListAdapter outerListAdapter = new OuterListAdapter(getContext());

        for (Group<Task> group : taskGroups) {
            SimpleTaskGroupAdapter adapter = new SimpleTaskGroupAdapter(group.getMembers());
            adapter.setHeaderTitle(group.getName());
            outerListAdapter.addAdapter(adapter);
        }

//        mDividerItemDecoration = new f_candy_d.com.boogie.utils.DividerItemDecoration(
//                null, getResources().getDrawable(R.drawable.simple_divider, null));
//        mDividerItemDecoration.setCallback(new f_candy_d.com.boogie.utils.DividerItemDecoration.Callback() {
//            @Override
//            public boolean drawDividerAboveItem(int adapterPosition) {
//                return (mSimpleTaskGroupAdapter.getFirstItemPosition() < adapterPosition &&
//                        adapterPosition < mSimpleTaskGroupAdapter.getItemCount());
//            }
//        });

        final float density = getResources().getDisplayMetrics().density;
        final int itemSideSpace = (int) (16 * density);
        final int itemGroupTopSpace = (int) (8 * density);
        final int itemGroupBottomSpace = (int) (8 * density);

        SpacerItemDecoration spacerItemDecoration =
                new SpacerItemDecoration(getContext(), new SpacerItemDecoration.Callback() {
                    @Override
                    public void getInsertedSpaceAroundItem(int adapterPosition, Rect output) {
                        if (adapterPosition == 0) {
                            output.top = itemGroupTopSpace * 10;
                        } else {
                            output.top = itemGroupTopSpace;
                        }

                        if (adapterPosition == 10 - 1) {
                            output.bottom = itemGroupBottomSpace * 4;
                        } else {
                            output.bottom = itemGroupBottomSpace;
                        }

                        output.left = itemSideSpace;
                        output.right = itemSideSpace;
                    }
                });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(outerListAdapter);
        recyclerView.addItemDecoration(spacerItemDecoration);
//        recyclerView.addItemDecoration(mDividerItemDecoration);
    }
}
