package com.f_candy_d.olga.infra.sql_utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by daichi on 17/08/30.
 * 
 * Conditional expression class.
 */

public class SqlCondExpr extends SqlWhere {

    private enum CondOp {
        LT("<"),       // a <  b (a is Less Than b)
        LTE("<="),     // a <= b (a is Less Than or Equal to b)
        GT(">"),       // a >  b (a is Grater Than b)
        GTE(">="),     // a >= b (a is Grater Than or Equal to b)
        EQ("="),       // a == b (a is EQual to b)
        NEQ("!=");     // a != b (a is Not EQual to b)

        private String mString;

        CondOp(final String string) {
            mString = string;
        }

        @Override
        public String toString() {
            return mString;
        }
    }

    @NonNull
    private CharSequence mColumn;
    @Nullable
    private CharSequence mCondition = null;

    public SqlCondExpr(@NonNull CharSequence column) {
        mColumn = column;
    }

    private SqlCondExpr join(@NonNull Object right, @NonNull CondOp operator) {
        if (right instanceof CharSequence) {
            mCondition = operator.toString() + SPACE + encloseBySingleQuote(right.toString());
        } else {
            mCondition = operator.toString() + SPACE + right.toString();
        }

        return this;
    }

    public SqlCondExpr lessThan(@NonNull Object right) {
        return join(right, CondOp.LT);
    }

    public SqlCondExpr lessThanOrEqualTo(@NonNull Object right) {
        return join(right, CondOp.LTE);
    }

    public SqlCondExpr graterThan(@NonNull Object right) {
        return join(right, CondOp.GT);
    }

    public SqlCondExpr graterThanOrEqualTo(@NonNull Object right) {
        return join(right, CondOp.GTE);
    }

    public SqlCondExpr equalTo(@NonNull Object right) {
        return join(right, CondOp.EQ);
    }

    public SqlCondExpr notEqualTo(@NonNull Object right) {
        return join(right, CondOp.NEQ);
    }

    public void setColumn(@NonNull CharSequence column) {
        mColumn = column;
    }

    public void setCondition(@Nullable CharSequence condition) {
        mCondition = condition;
    }

    @NonNull
    @Override
    public String formalize() {
        if (this.mCondition != null) {
            String expression = this.mColumn + SPACE + this.mCondition;
            return formalizeConsideringIsInBrancketAndNegation(expression);
        }
        throw new IllegalStateException("Syntax error");
    }
}

