package com.f_candy_d.olga.infra;

import com.f_candy_d.olga.MyApp;
import com.f_candy_d.olga.data_store.SqliteDatabaseOpenHelperImpl;
import com.f_candy_d.olga.infra.sqlite.SqliteDatabaseOpenHelper;
import com.f_candy_d.olga.infra.sqlite.SqliteRepository;

/**
 * Created by daichi on 9/10/17.
 */

final public class Repository {

    static SqlRepository getSql() {
        SqliteDatabaseOpenHelper helper = new SqliteDatabaseOpenHelperImpl(MyApp.getAppContext());
        return new SqliteRepository(helper);
    }
}
