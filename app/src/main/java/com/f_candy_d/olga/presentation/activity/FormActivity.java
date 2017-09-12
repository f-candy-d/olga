package com.f_candy_d.olga.presentation.activity;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.presentation.fragment.DateFormFragment;
import com.f_candy_d.olga.presentation.view_model.FormViewModel;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

public class FormActivity extends ViewActivity {

    @Override
    protected ActivityViewModel onCreateViewModel() {
        return new FormViewModel(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabLayout tabBar = (TabLayout) findViewById(R.id.tab_bar);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        final String[] pageTitle = new String[] {
                "Date",
                "Repeat",
                "Note"
        };

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new DateFormFragment();
            }

            @Override
            public int getCount() {
                return 7;
            }
        };

        viewPager.setAdapter(pagerAdapter);
        tabBar.setupWithViewPager(viewPager);

        tabBar.getTabAt(0).setIcon(R.drawable.ic_check);
        tabBar.getTabAt(1).setIcon(R.drawable.ic_event);
        tabBar.getTabAt(2).setIcon(R.drawable.ic_flight);
        tabBar.getTabAt(3).setIcon(R.drawable.ic_check);
        tabBar.getTabAt(4).setIcon(R.drawable.ic_event);
        tabBar.getTabAt(5).setIcon(R.drawable.ic_flight);
        tabBar.getTabAt(6).setIcon(R.drawable.ic_flight);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
