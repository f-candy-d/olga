package com.f_candy_d.olga.infra.sql_utils;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 17/08/30.
 * 
 * BETWEEN expression class.
 */

public class SqlBetweenExpr extends SqlWhere {

    @NonNull
    private CharSequence mColumn;
    private CharSequence mMin;
    private CharSequence mMax;
    private boolean mExcludeBoundaryOfMin = false;
    private boolean mExcludeBoundaryOfMax = false;

    public SqlBetweenExpr(@NonNull CharSequence column) {
        mColumn = column;
        mMax = null;
        mMin = null;
    }

    public SqlBetweenExpr setRange(@NonNull Object min, @NonNull Object max) {
        mMin = (min instanceof CharSequence)
                ? encloseBySingleQuote(min.toString())
                : min.toString();

        mMax = (max instanceof CharSequence)
                ? encloseBySingleQuote(max.toString())
                : max.toString();

        return this;
    }

    public SqlBetweenExpr setColumn(@NonNull CharSequence column) {
        mColumn = column;
        return this;
    }

    public SqlBetweenExpr setRangeBoundaries(boolean excludeMin, boolean excludeMax) {
        mExcludeBoundaryOfMin = excludeMin;
        mExcludeBoundaryOfMax = excludeMax;
        return this;
    }

    @NonNull
    @Override
    public String formalize() {
        if (mMin != null && mMax != null) {
            String expression;
            if (mExcludeBoundaryOfMin || mExcludeBoundaryOfMax) {
                SqlCondExpr condMin = (mExcludeBoundaryOfMin)
                        ? new SqlCondExpr(mColumn).graterThan(mMin)
                        : new SqlCondExpr(mColumn).graterThanOrEqualTo(mMin);

                SqlCondExpr condMan = (mExcludeBoundaryOfMax)
                        ? new SqlCondExpr(mColumn).lessThan(mMax)
                        : new SqlCondExpr(mColumn).lessThanOrEqualTo(mMax);

                expression = new SqlLogicExpr(condMin).and(condMan).formalize();

            } else {
                expression = mColumn + " BETWEEN " + mMin + " AND " + mMax;
            }
            return formalizeConsideringIsInBrancketAndNegation(expression);
        }
        throw new IllegalStateException("Syntax error");
    }
}
