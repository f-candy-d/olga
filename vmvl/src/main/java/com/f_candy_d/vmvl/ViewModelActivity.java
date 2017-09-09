package com.f_candy_d.vmvl;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by daichi on 9/10/17.
 */

abstract public class ViewModelActivity extends AppCompatActivity implements SupportAVLInterface {

    private ActivityViewLogicInterface mViewLogic;

    abstract protected ActivityViewLogicInterface onCreateViewLogic();

    /**
     * Delegate Activity's methods to ViewLogic class
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewLogic = onCreateViewLogic();
        mViewLogic.setSupportAVLInterface(this);
        mViewLogic.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewLogic.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mViewLogic.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewLogic.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewLogic.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mViewLogic.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewLogic.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mViewLogic.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return mViewLogic.onCreateOptionMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mViewLogic.onOptionsItemSelected(item);
    }

    /**
     * region; SupportAVLInterface implementation
     */

    @Override
    public void onSetContentViewMethodDispatch(View view) {
        setContentView(view);
    }

    @Override
    public void onSetSupportActionBarMethodDispatch(Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }

    @Override
    public ActionBar onGetSupportActionBarMethodDispatch() {
        return getSupportActionBar();
    }

    @Override
    public void onSetContentViewMethodDispatch(@LayoutRes int layoutResId) {
        setContentView(layoutResId);
    }

    @Override
    public View onFindViewByIdMethodDispatch(@IdRes int id) {
        return findViewById(id);
    }
}
