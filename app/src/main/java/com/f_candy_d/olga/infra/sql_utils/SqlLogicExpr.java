package com.f_candy_d.olga.infra.sql_utils;

/**
 * Created by daichi on 17/08/30.
 */

import android.support.annotation.NonNull;

/**
 * Logical expression class
 */
public class SqlLogicExpr extends SqlWhere {

    private enum LogicOp {
        AND,
        OR
    }

    @NonNull
    private String mExpression;

    public SqlLogicExpr(@NonNull SqlWhere mostLeftExpr) {
        mExpression = mostLeftExpr.formalize();
    }

    /**
     * Add '[logicOp] [expr]' expression
     */
    private SqlLogicExpr join(@NonNull SqlWhere expr, @NonNull LogicOp logicOp) {
        mExpression = mExpression.concat(SPACE + logicOp.toString() + SPACE + expr.formalize());
        return this;
    }

    /**
     * Add '[outerLogicOp] ([mColumn] [innerLogicOp] [mCondition])' expression
     */
    private SqlLogicExpr joinLogicExpr(@NonNull LogicOp outerLogicOp, @NonNull SqlWhere left,
                                    @NonNull SqlWhere right, @NonNull LogicOp innerLogicOp) {

        SqlLogicExpr inner = new SqlLogicExpr(left).join(right, innerLogicOp);
        inner.setInBracket(true);
        return join(inner, outerLogicOp);
    }

    /**
     * add 'AND [expr]' expression
     */
    public SqlLogicExpr and(@NonNull SqlWhere expr) {
        return join(expr, LogicOp.AND);
    }

    /**
     * add 'OR [expr]' expression
     */
    public SqlLogicExpr or(@NonNull SqlWhere expr) {
        return join(expr, LogicOp.OR);
    }

    /**
     * add 'AND ([mColumn] AND [mCondition])' expression
     */
    public SqlLogicExpr andAndExpr(@NonNull SqlWhere left, @NonNull SqlWhere right) {
        return joinLogicExpr(LogicOp.AND, left, right, LogicOp.AND);
    }

    /**
     * add 'AND ([mColumn] OR [mCondition])' expression
     */
    public SqlLogicExpr andOrExpr(@NonNull SqlWhere left, @NonNull SqlWhere right) {
        return joinLogicExpr(LogicOp.AND, left, right, LogicOp.OR);
    }

    /**
     * add 'OR ([mColumn] AND [mCondition])' expression
     */
    public SqlLogicExpr orAndExpr(@NonNull SqlWhere left, @NonNull SqlWhere right) {
        return joinLogicExpr(LogicOp.OR, left, right, LogicOp.AND);
    }

    /**
     * add 'OR ([mColumn] OR [mCondition])' expression
     */
    public SqlLogicExpr orOrExpr(@NonNull SqlWhere left, @NonNull SqlWhere right) {
        return joinLogicExpr(LogicOp.OR, left, right, LogicOp.OR);
    }

    public void reset(@NonNull SqlWhere mostLeftExpr) {
        mExpression = mostLeftExpr.formalize();
    }

    @NonNull
    @Override
    public String formalize() {
        return formalizeConsideringIsInBrancketAndNegation(mExpression);
    }
}

