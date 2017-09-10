package com.f_candy_d.olga;

import android.app.Application;
import android.content.Context;

/**
 * Created by daichi on 9/10/17.
 *
 * You can get the ApplicationContext using this class anytime.
 * Do not forget to insert the below code into the AndroidManifests.xml.
 *
 * android:name=".MyApp"
 *
 * DO NOT CONSTRUCT THIS CLASS DIRECTLY!
 */

public class MyApp extends Application {

    private static MyApp mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}
