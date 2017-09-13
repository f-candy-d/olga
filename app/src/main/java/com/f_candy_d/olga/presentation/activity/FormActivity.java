package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.data_store.DbContract;
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
    private int mPrevPagerPosition;
    private FragmentPagerAdapter mPagerAdapter;

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
                if (errors != null && errors.length != 0) {
                    Snackbar.make(view, "There are some errors!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final TextSwitcher titleSwitcher = (TextSwitcher) findViewById(R.id.title_switcher);
        titleSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView title = new TextView(FormActivity.this);
                title.setGravity(Gravity.CENTER_VERTICAL);
                title.setTextAppearance(FormActivity.this, R.style.TitleTextAppearance);
                TextSwitcher.LayoutParams layoutParams = new TextSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                title.setLayoutParams(layoutParams);
                return title;
            }
        });

        final FormFragment[] formFragments = mViewModel.getFormFragments().toArray(new FormFragment[]{});

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return formFragments[position];
            }

            @Override
            public int getCount() {
                return formFragments.length;
            }
        };

        viewPager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        if (0 < formFragments.length) {
            // initial position
            mPrevPagerPosition = 0;
            int currentPosition = mPrevPagerPosition;
            titleSwitcher.setText(formFragments[currentPosition].getTitle());
            viewPager.setCurrentItem(currentPosition);
        }

        for (int i = 0; i < formFragments.length; ++i) {
            tabs.getTabAt(i).setIcon(formFragments[i].getIcon());
        }

        /**
         * See {@link https://github.com/pjambo/ToolbarTitleAnimation/blob/master/app/src/main/java/com/jambo/example/toolbartitleanimation/PagerActivity.java}
         *
         * Set IN an OUT animation for the {@link ToolBar} title
         * ({@link TextSwitcher} in this case) when pager is swiped to the left
         */
        final Animation IN_SWIPE_BACKWARD = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        final Animation OUT_SWIPE_BACKWARD = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);

        /**
         * Set IN an OUT animation for the {@link ToolBar} title
         * ({@link TextSwitcher} in this case) when pager is swiped to the right
         */
        final Animation IN_SWIPE_FORWARD = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        final Animation OUT_SWIPE_FORWARD = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);

        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                appBarLayout.setExpanded(true, true);

                if (position >= mPrevPagerPosition) {
                    titleSwitcher.setInAnimation(IN_SWIPE_FORWARD);
                    titleSwitcher.setOutAnimation(OUT_SWIPE_FORWARD);
                } else {
                    titleSwitcher.setInAnimation(IN_SWIPE_BACKWARD);
                    titleSwitcher.setOutAnimation(OUT_SWIPE_BACKWARD);
                }

                titleSwitcher.setText(formFragments[position].getTitle());
                mPrevPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Nullable
    private FormFragment findFormFragmentByTag(String tag) {
        for (int i = 0; i < mPagerAdapter.getCount(); ++i) {
            if (tag.equals(((FormFragment) mPagerAdapter.getItem(i)).getFragmentTag())) {
                return (FormFragment) mPagerAdapter.getItem(i);
            }
        }

        return null;
    }

    /**
     * region; implements FormViewModel.RequestReplyListener implementation
     */

    @Override
    public void onNormalFinish(long contentId) {

    }

    @Override
    public void onAbnormalFinish(String message) {

    }

    /**
     * region; FormFragment.OnDataInputListener implementation
     */

    @Override
    public void onUserInputData(Bundle data, String fragmentTag) {
        Pair<Integer, String>[] errors = mViewModel.onDataInput(data, fragmentTag);
        // TODO; Show error messages.
        FormFragment fragment = findFormFragmentByTag(fragmentTag);
        Log.d("mylog", "onUserInptData fragment is null =" + String.valueOf(fragment == null));
        if (fragment != null && errors != null) {
            for (Pair<Integer, String> error : errors) {
                fragment.onShowError(error.first);
            }
        }
    }
}