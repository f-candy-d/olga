package com.f_candy_d.olga.vm_vl_base;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by daichi on 9/10/17.
 */

interface SupportAVLInterface {

    void onSetContentViewMethodDispatch(View view);
    void onSetSupportActionBarMethodDispatch(Toolbar toolbar);
    ActionBar onGetSupportActionBarMethodDispatch();
}
