package com.f_candy_d.vmvl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by daichi on 9/10/17.
 */

public interface ActivityViewLogicInterface {

    /**
     * This will be only called in ViewModelActivity#onCreate() method
     */
    void setPartnerActivity(ViewModelActivity partnerActivity);

    /**
     * The following methods will be delegated from Activity
     */
    boolean onCreateOptionMenu(Menu menu);
    boolean onOptionsItemSelected(MenuItem item);

    /**
     * The following methods will be called
     * in the Activity-lifecycle methods
     */

    void onCreate(@Nullable Bundle savedInstanceState);
    void onStart();
    void onRestart();
    void onResume();
    void onPause();
    void onStop();
    void onDestroy();
    void onSaveInstanceState(Bundle outState);
}
