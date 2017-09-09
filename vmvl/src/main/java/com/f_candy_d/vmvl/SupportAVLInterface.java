package com.f_candy_d.vmvl;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by daichi on 9/10/17.
 */

interface SupportAVLInterface {

    /**
     * The followings will be called to use Activity's methods from ActivityViewLogic class
     */

    void onSetContentViewMethodDispatch(View view);
    void onSetContentViewMethodDispatch(@LayoutRes int layoutResId);
    void onSetSupportActionBarMethodDispatch(Toolbar toolbar);
    ActionBar onGetSupportActionBarMethodDispatch();
    View onFindViewByIdMethodDispatch(@IdRes int id);
    MenuInflater getMenuInflater();
}
