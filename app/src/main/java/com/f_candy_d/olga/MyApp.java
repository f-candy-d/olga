package com.f_candy_d.olga;

import android.app.Application;
import android.content.Context;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.data_store.SqliteDatabaseOpenHelperImpl;
import com.f_candy_d.olga.infra.sqlite.SqliteTableUtils;

/**
 * Created by daichi on 9/10/17.
 *
 * You can getDataType the ApplicationContext using this class anytime.
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

        // TODO : REMOVE THIS CODE
        SqliteDatabaseOpenHelperImpl openHelper = new SqliteDatabaseOpenHelperImpl(this);
        SqliteTableUtils.resetTable(openHelper.getWritableDatabase(), DbContract.getTableSources());
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}
