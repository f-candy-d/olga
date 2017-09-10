package com.f_candy_d.olga.infra.sql_utils;

/**
 * Created by daichi on 17/08/30.
 *
 * The super class of expression classes.
 */

import android.support.annotation.NonNull;

abstract public class SqlWhere {

    protected static final String SPACE = " ";
    private boolean mNegation = false;
    private boolean mIsInBracket = false;

    public SqlWhere() {}

    public boolean isNegation() {
        return mNegation;
    }

    public void setNegation(boolean negation) {
        mNegation = negation;
    }

    public boolean isInBracket() {
        return mIsInBracket;
    }

    public void setInBracket(boolean inBracket) {
        mIsInBracket = inBracket;
    }

    @NonNull
    String formalizeConsideringIsInBrancketAndNegation(@NonNull String baseExpression) {
        if (isNegation()) {
            baseExpression = "NOT " + baseExpression;
        }

        return (isInBracket())
                ? "(" + baseExpression + ")"
                : baseExpression;
    }

    @NonNull abstract public String formalize();

    protected CharSequence encloseBySingleQuote(@NonNull CharSequence string) {
        return "'" + string + "'";
    }
}
