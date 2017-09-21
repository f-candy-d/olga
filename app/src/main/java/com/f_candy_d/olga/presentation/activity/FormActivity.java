package com.f_candy_d.olga.presentation.activity;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.f_candy_d.olga.R;
import com.f_candy_d.olga.Utils;
import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.presentation.fragment.FormFragment;

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
        // color set
        final Style style = getStyle();

        // # ToolBar & AppBar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Set the padding to match the Status Bar height
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setPadding(0, Utils.getStatusBarHeight(), 0, 0);

        // # FAB

        FloatingActionButton saveButton = (FloatingActionButton) findViewById(R.id.fab);
        saveButton.setBackgroundTintList(ColorStateList.valueOf(style.colorSecondary));
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
                TextView view = (TextView) getLayoutInflater().inflate(R.layout.title_switcher_text_view, textSwitcher, false);
                view.setTextColor(style.textColorPrimary);
                return view;
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

        // # Navigation Back Button Color
        final Drawable upArrow = Utils.getDrawable(R.drawable.ic_arrow_back);
        upArrow.setColorFilter(style.textColorPrimary, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        // # Background Color
        appBarLayout.setBackgroundColor(style.colorPrimary);
        viewPager.setBackgroundColor(style.colorPrimary);

        // # Initial Status
        if (0 < mFormFragments.length) {
            mPrevPagePosition = 0;
            viewPager.setCurrentItem(mPrevPagePosition);
            textSwitcher.setText(mFormFragments[mPrevPagePosition].getTitle());
        }
    }

    @NonNull
    @Override
    public Style getStyle() {
        // Default theme colors
        Style style = new Style();
        style.colorPrimary = ContextCompat.getColor(this, R.color.color_blue_gray);
        style.colorSecondary = ContextCompat.getColor(this, R.color.color_cream_red);
        style.textColorPrimary = ContextCompat.getColor(this, R.color.primary_text_light);
        style.textColorSecondary = ContextCompat.getColor(this, R.color.secondary_text_light);
        return style;
    }

    /**
     * Style data
     */

    public static class Style {
        public int colorPrimary;
        public int colorSecondary;
        public int textColorPrimary;
        public int textColorSecondary;
    }
}