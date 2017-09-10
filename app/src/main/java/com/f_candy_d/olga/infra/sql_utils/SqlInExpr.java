package com.f_candy_d.olga.infra.sql_utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by daichi on 17/08/30.
 * 
 * IN expression class.
 */

public class SqlInExpr extends SqlWhere {

    @NonNull
    private CharSequence mColumn;
    @Nullable
    private String mArgs;

    public SqlInExpr(@NonNull CharSequence column) {
        mColumn = column;
        mArgs = null;
    }

    public SqlInExpr(@NonNull CharSequence column, @NonNull Object... args) {
        this(column);
        addArgs(args);
    }

    public void resetArgs() {
        mArgs = null;
    }

    final public void resetArgs(@NonNull Object... args) {
        resetArgs();
        addArgs(args);
    }

    final public SqlInExpr addArgs(@NonNull Object... args) {

        if (args.length != 0) {
            String commaSep = ",";
            String newArgs;

            if (args instanceof CharSequence[]) {
                CharSequence[] stringArgs = new CharSequence[args.length];
                for (int i = 0; i < args.length; ++i) {
                    stringArgs[i] = encloseBySingleQuote(args[i].toString());
                }
                newArgs = TextUtils.join(commaSep, stringArgs);
            } else {
                newArgs = TextUtils.join(commaSep, args);
            }

            if (mArgs != null) {
                mArgs = mArgs.concat(commaSep + newArgs);
            } else {
                mArgs = newArgs;
            }
        }

        return this;
    }

    public SqlInExpr setColumn(@NonNull CharSequence column) {
        mColumn = column;
        return this;
    }

    @NonNull
    @Override
    public String formalize() {
        if (mArgs != null) {
            String expression = mColumn + " IN(" + mArgs + ")";
            return formalizeConsideringIsInBrancketAndNegation(expression);
        }
        throw new IllegalArgumentException("Syntax error");
    }
}

