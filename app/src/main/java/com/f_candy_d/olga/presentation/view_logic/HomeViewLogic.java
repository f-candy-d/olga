package com.f_candy_d.olga.presentation.view_logic;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.view_model.HomeViewLogicInterface;
import com.f_candy_d.olga.presentation.view_model.HomeViewModelInterface;
import com.f_candy_d.vmvl.ActivityViewLogic;

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

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.onShowSnackBarWithMessage(view);
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
}
