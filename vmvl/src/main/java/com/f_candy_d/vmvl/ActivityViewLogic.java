package com.f_candy_d.vmvl;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

/**
 * Created by daichi on 9/10/17.
 */

abstract public class ActivityViewLogic implements ActivityViewLogicInterface {

    private ViewModelActivity mPartnerActivity;

    /**
     * Dummy constructor to forc
     */
    public ActivityViewLogic(ViewModelInterface viewModelInterface) {}

    protected Context getContext() {
        return mPartnerActivity;
    }

    protected Context getApplicationContext() {
        return mPartnerActivity.getApplicationContext();
    }

    /**
     * Delegate methods
     */

    protected void setContentView(View view) {
        mPartnerActivity.setContentView(view);
    }

    protected void setContentView(@LayoutRes int layoutResId) {
        mPartnerActivity.setContentView(layoutResId);
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        mPartnerActivity.setSupportActionBar(toolbar);
    }

    protected ActionBar getSupportActionBar() {
        return mPartnerActivity.getSupportActionBar();
    }

    protected View findViewById(@IdRes int id) {
        return mPartnerActivity.findViewById(id);
    }

    protected MenuInflater getMenuInflater() {
        return mPartnerActivity.getMenuInflater();
    }

    protected Window getWindow() {
        return mPartnerActivity.getWindow();
    }

    /**
     * region; ActivityViewLogicInterface implementation
     */

    public void setPartnerActivity(ViewModelActivity partnerActivity) {
        mPartnerActivity = partnerActivity;
    }

    /**
     * The following methods will be called
     * in the Activity-lifecycle methods
     */

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
