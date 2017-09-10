package com.f_candy_d.olga.infra;

import com.f_candy_d.olga.MyApp;
import com.f_candy_d.olga.data_store.SqliteDatabaseOpenHelperImpl;
import com.f_candy_d.olga.infra.sqlite.SqliteDatabaseOpenHelper;
import com.f_candy_d.olga.infra.sqlite.SqliteRepository;

/**
 * Created by daichi on 9/10/17.
 */

final public class Repository {

    private static SqlRepository mSqlRepository = null;

    public static SqlRepository getSql() {
        if (mSqlRepository == null) {
            SqliteDatabaseOpenHelper helper = new SqliteDatabaseOpenHelperImpl(MyApp.getAppContext());
            mSqlRepository = new SqliteRepository(helper);
        }

        return mSqlRepository;
    }
}
