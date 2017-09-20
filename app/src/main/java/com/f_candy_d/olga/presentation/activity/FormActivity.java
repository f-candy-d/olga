package com.f_candy_d.olga.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.f_candy_d.vvm.ActivityViewModel;
import com.f_candy_d.vvm.ViewActivity;

abstract public class FormActivity extends AppCompatActivity
        implements FormFragment.OnDataInputListener {

    public static final String EXTRA_CONTENT_ID = "contentId";

    private FormFragment[] mFormFragments;
    private int mPrevPagePosition;

    public static Bundle makeExtras(long contentId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_CONTENT_ID, contentId);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        final long id = getIntent().getLongExtra(EXTRA_CONTENT_ID, DbContract.NULL_ID);
        if (id != DbContract.NULL_ID) {
            onInitWithContentId(id);
        } else {
            onInit();
        }

        initUI();
    }

    abstract protected void onInitWithContentId(long contentId);
    abstract protected void onInit();
    abstract protected FormFragment[] getFormFragments();
    abstract protected void onSave();

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {

        // # ToolBar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Set the padding to match the Status Bar height
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setPadding(0, getStatusBarHeight(), 0, 0);

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });

        // # TextSwitcher

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

        final TextSwitcher textSwitcher = (TextSwitcher) findViewById(R.id.title_switcher);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return getLayoutInflater().inflate(R.layout.title_switcher_text_view, textSwitcher, false);
            }
        });

        // # ViewPager

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mFormFragments = getFormFragments();
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFormFragments[position];
            }

            @Override
            public int getCount() {
                return mFormFragments.length;
            }
        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageSelected(int position) {
                //Set TextSwitcher animation based on swipe direction
                if (position >= mPrevPagePosition) {
                    textSwitcher.setInAnimation(IN_SWIPE_FORWARD);
                    textSwitcher.setOutAnimation(OUT_SWIPE_FORWARD);
                } else {
                    textSwitcher.setInAnimation(IN_SWIPE_BACKWARD);
                    textSwitcher.setOutAnimation(OUT_SWIPE_BACKWARD);
                }
                textSwitcher.setText(mFormFragments[position].getTitle());
                mPrevPagePosition = position;
            }
        });

        // # Background Color
        int color = getThemeColor();
        appBarLayout.setBackgroundColor(color);
        viewPager.setBackgroundColor(color);

        // # Initial Status
        if (0 < mFormFragments.length) {
            mPrevPagePosition = 0;
            viewPager.setCurrentItem(mPrevPagePosition);
            textSwitcher.setText(mFormFragments[mPrevPagePosition].getTitle());
        }
    }

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    abstract protected int getThemeColor();
}