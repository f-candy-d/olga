package com.f_candy_d.olga.infra.sql_utils;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 17/08/30.
 * 
 * IS NULL expression class.
 */

public class SqlIsNullExpr extends SqlWhere {

    @NonNull
    String mColumn;

    public SqlIsNullExpr(@NonNull String column) {
        mColumn = column;
    }

    public SqlIsNullExpr setColumn(@NonNull String column) {
        mColumn = column;
        return this;
    }

    @NonNull
    @Override
    public String formalize() {
        String expression;
        if (isNegation()) {
            expression = mColumn + " IS NOT NULL";
        } else {
            expression = mColumn + " IS NULL";
        }

        return (isInBracket())
                ? "(" + expression +")"
                : expression;
    }
}
