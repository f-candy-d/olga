package com.f_candy_d.olga.infra.sqlite;

/**
 * Created by daichi on 17/08/30.
 */

public enum SqliteColumnDataType {

    INTEGER("INTEGER"),
    INTEGER_PK("INTEGER PRIMARY KEY"),
    TEXT("TEXT"),
    REAL("REAL"),
    BLOB("BLOB");

    private final String mString;

    SqliteColumnDataType(String string) {
        mString = string;
    }

    @Override
    public String toString() {
        return mString;
    }
}
