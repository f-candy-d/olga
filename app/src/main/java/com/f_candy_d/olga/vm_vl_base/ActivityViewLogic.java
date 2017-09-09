package com.f_candy_d.olga.vm_vl_base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by daichi on 9/10/17.
 */

abstract public class ActivityViewLogic {

    private SupportAVLInterface mSupportAVLInterface;

    void setSupportAVLInterface(SupportAVLInterface supportAVLInterface) {
        mSupportAVLInterface = supportAVLInterface;
    }

    /**
     * The following methods will be called
     * in the Activity-lifecycle methods
     */

    abstract protected void onCreate(@Nullable Bundle savedInstanceState);

    protected void onStart() {}

    protected void onRestart() {}

    protected void onResume() {}

    protected void onPause() {}

    protected void onStop() {}

    protected void onDestroy() {}

    protected void onSaveInstanceState(Bundle outState) {}

    /**
     * Delegate methods
     */

    protected void setContentView(View view) {
        mSupportAVLInterface.onSetContentViewMethodDispatch(view);
    }

    protected void setSupportActionBar(Toolbar toolbar) {
        mSupportAVLInterface.onSetSupportActionBarMethodDispatch(toolbar);
    }

    protected ActionBar getSupportActionBar() {
        return mSupportAVLInterface.onGetSupportActionBarMethodDispatch();
    }
}
