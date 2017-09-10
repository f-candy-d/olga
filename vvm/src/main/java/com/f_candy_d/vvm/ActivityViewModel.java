package com.f_candy_d.vvm;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

/**
 * Created by daichi on 9/11/17.
 */

abstract public class ActivityViewModel {

    private Context mContext;

    public ActivityViewModel(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }

    protected Resources getResources() {
        return mContext.getResources();
    }

    /**
     * The following methods will be called
     * in the Activity-lifecycle methods
     */

    protected void onCreate(@Nullable Bundle savedInstanceState) {}

    protected void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    }

    protected void onStart() {
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
    }

    protected void onRestart() {
    }

    protected void onResume() {
    }

    protected void onPause() {
    }

    protected void onStop() {
    }

    protected void onDestroy() {
    }

    protected void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    }

    protected void onSaveInstanceState(Bundle outState) {
    }
}
