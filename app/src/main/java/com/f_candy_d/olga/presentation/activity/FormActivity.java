package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
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
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.presentation.fragment.DateFormFragment;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.olga.presentation.view_model.FormViewModel;
import com.f_candy_d.olga.presentation.view_model.FormViewModelFactory;
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

public class FormActivity extends ViewActivity
        implements
        FormViewModel.RequestReplyListener,
        FormFragment.OnDataInputListener {

    public static final String EXTRA_CONTENT_ID = "contentId";
    public static final String EXTRA_MODEL = "model";

    private FormViewModel mViewModel;

    public static Bundle makeExtras(FormViewModelFactory.Model model) {
        return makeExtras(DbContract.NULL_ID, model);
    }

    public static Bundle makeExtras(long contentId, FormViewModelFactory.Model model) {
        if (model == null) {
            throw new NullPointerException("Specify a ViewModel type!");
        }

        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_CONTENT_ID, contentId);
        bundle.putParcelable(EXTRA_MODEL, model);

        return bundle;
    }

    @Override
    protected ActivityViewModel onCreateViewModel() {
        Bundle bundle = getIntent().getExtras();
        long contentId = bundle.getLong(EXTRA_CONTENT_ID, DbContract.NULL_ID);
        FormViewModelFactory.Model model = bundle.getParcelable(EXTRA_MODEL);
        mViewModel = FormViewModelFactory.create(model, this, this, contentId);

        if (mViewModel == null) {
            throw new NullPointerException("Specify a ViewModel type!");
        }

        return mViewModel;
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

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] errors = mViewModel.onRequestFinish();
                if (errors.length != 0) {
                    Snackbar.make(view, "There are some errors!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);

        final FormFragment[] formFragments = mViewModel.getFormFragments().toArray(new FormFragment[]{});

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return formFragments[position];
            }

            @Override
            public int getCount() {
                return formFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return formFragments[position].getTitle();
            }
        };

        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);

        for (int i = 0; i < formFragments.length; ++i) {
            tabs.getTabAt(i).setIcon(formFragments[i].getIcon());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * region; implements FormViewModel.RequestReplyListener implementation
     */

    @Override
    public void onNormalFinish(long contentId) {

    }

    @Override
    public void onAbnormalFinish() {

    }

    /**
     * region; FormFragment.OnDataInputListener implementation
     */

    @Override
    public void onUserInputData(Bundle data, String fragmentTag) {
        mViewModel.onDataInput(data, fragmentTag);
    }
}
