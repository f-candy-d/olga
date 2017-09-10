package com.f_candy_d.olga.infra.sql_utils;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 17/08/30.
 * 
 * LIKE expression class.
 */

public class SqlLikeExpr extends SqlWhere {

    @NonNull
    private CharSequence mColumn;
    private CharSequence mRegex;

    public SqlLikeExpr(@NonNull CharSequence column) {
        this(column, null);
    }

    public SqlLikeExpr(@NonNull CharSequence column, CharSequence regex) {
        mColumn = column;
        mRegex = regex;
    }

    public SqlLikeExpr setRegex(@NonNull CharSequence regex) {
        mRegex = regex;
        return this;
    }

    public SqlLikeExpr setColumn(@NonNull CharSequence column) {
        mColumn = column;
        return this;
    }

    @NonNull
    @Override
    public String formalize() {
        if (mRegex != null) {
            String expression = mColumn + " LIKE " + mRegex;
            return formalizeConsideringIsInBrancketAndNegation(expression);
        }
        throw new IllegalStateException("Syntax error");
    }
}

