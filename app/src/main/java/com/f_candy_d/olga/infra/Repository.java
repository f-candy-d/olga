package com.f_candy_d.olga.infra;

import com.f_candy_d.olga.MyApp;
import com.f_candy_d.olga.data_store.SqliteDatabaseOpenHelperImpl;
import com.f_candy_d.olga.infra.sqlite.SqliteDatabaseOpenHelper;

/**
 * Created by daichi on 9/10/17.
 */

final public class Repository {

    public static final long SQLITE_NULL_ID = SqliteRepository.NULL_ID;
    private static SqliteRepository mSqliteRepository = null;

    public static SqliteRepository getSqlite() {
        if (mSqliteRepository == null) {
            SqliteDatabaseOpenHelper helper = new SqliteDatabaseOpenHelperImpl(MyApp.getAppContext());
             mSqliteRepository = new SqliteRepository(helper);
        }

        return mSqliteRepository;
    }
}
