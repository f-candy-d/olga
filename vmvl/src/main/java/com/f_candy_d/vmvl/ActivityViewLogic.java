package com.f_candy_d.vmvl;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by daichi on 9/10/17.
 */

abstract public class ActivityViewLogic implements ActivityViewLogicInterface {

    private SupportAVLInterface mSupportAVLInterface;

    /**
     * Dummy constructor to forc
     */
    public ActivityViewLogic(ViewModelInterface viewModelInterface) {}

    /**
     * Delegate methods
     */

    protected void setContentView(View view) {
        mSupportAVLInterface.onSetContentViewMethodDispatch(view);
    }

    protected void setContentView(@LayoutRes int layoutResId) {
        mSupportAVLInterface.onSetContentViewMethodDispatch(layoutResId);
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        mSupportAVLInterface.onSetSupportActionBarMethodDispatch(toolbar);
    }

    protected ActionBar getSupportActionBar() {
        return mSupportAVLInterface.onGetSupportActionBarMethodDispatch();
    }

    protected View findViewById(@IdRes int id) {
        return mSupportAVLInterface.onFindViewByIdMethodDispatch(id);
    }

    protected MenuInflater getMenuInflater() {
        return mSupportAVLInterface.getMenuInflater();
    }


    /**
     * region; ActivityViewLogicInterface implementation
     */

    @Override
    public void setSupportAVLInterface(SupportAVLInterface supportAVLInterface) {
        mSupportAVLInterface = supportAVLInterface;
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
