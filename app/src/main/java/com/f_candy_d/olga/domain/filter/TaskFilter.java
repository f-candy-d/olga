package com.f_candy_d.olga.domain.filter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.TaskTable;
import com.f_candy_d.olga.domain.usecase.SqlTableUseCase;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlCondExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlLikeExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlLogicExpr;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;
import com.f_candy_d.olga.infra.sql_utils.SqlWhere;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by daichi on 9/23/17.
 */

public class TaskFilter implements Parcelable {

    private String mFilterName;
    @NonNull private ArrayList<String> mKeywordsInTitleOrDescription;
    private int mPickUpAchievementFlag;

    // For 'mPickUpAchievementFlag'
    public static final int FLAG_PICKUP_ONLY_NOT_ACHIEVED = -1;
    public static final int FLAG_PICKUP_BOTH = 0;
    public static final int FLAG_PICKUP_ONLY_ACHIEVED = 1;

    public TaskFilter() {
        mKeywordsInTitleOrDescription = new ArrayList<>();
        mPickUpAchievementFlag = FLAG_PICKUP_BOTH;
    }

    public void addKeyword(String keyword) {
        // Remove null value
        if (keyword != null) {
            mKeywordsInTitleOrDescription.add(keyword);
        }
    }

    public String getFilterName() {
        return mFilterName;
    }

    public void setFilterName(String filterName) {
        mFilterName = filterName;
    }

    public void removeKeyword(String keyword) {
        mKeywordsInTitleOrDescription.remove(keyword);
    }

    public void setPickUpAchievementFlag(int flag) {
        mPickUpAchievementFlag = flag;
    }

    public int getPickUpAchievementFlag() {
        return mPickUpAchievementFlag;
    }

    @NonNull
    public SqlQuery toQuery() {
        SqlQuery query = new SqlQuery();
        query.putTables(TaskTable.TABLE_NAME);
        ArrayList<SqlWhere> conditions = new ArrayList<>();
        SqlWhere condition;

        condition = makeAchievementFlagCondition(mPickUpAchievementFlag);
        if (condition != null) {
            conditions.add(condition);
        }

        condition = makeKeywordCondition(mKeywordsInTitleOrDescription, TaskTable._TITLE, TaskTable._DESCRIPTION);
        if (condition != null) {
            conditions.add(condition);
        }

        condition = joinConditionsByAnd(conditions);
        if (condition != null) {
            query.setSelection(condition);
        }

        return query;
    }

    @Nullable
    private SqlWhere makeAchievementFlagCondition(int pickUpachievementFlag) {
        switch (pickUpachievementFlag) {
            case FLAG_PICKUP_ONLY_ACHIEVED:
                return new SqlCondExpr(TaskTable._IS_ACHIEVED).equalTo(Repository.SQLITE_BOOL_TRUE);
            case FLAG_PICKUP_ONLY_NOT_ACHIEVED:
                return new SqlCondExpr(TaskTable._IS_ACHIEVED).equalTo(Repository.SQLITE_BOOL_FALSE);
            default:
                return null;
        }
    }

    @Nullable
    private SqlWhere makeKeywordCondition(Collection<String> keywords, String... columns) {
        if (keywords.size() == 0 || columns.length == 0) {
            return null;
        }

        ArrayList<SqlLikeExpr> likeExprs = new ArrayList<>(keywords.size() * columns.length);
        for (String column : columns) {
            for (String keyword : keywords) {
                likeExprs.add(new SqlLikeExpr(column).setRegex("%" + keyword + "%"));
            }
        }

        Iterator<SqlLikeExpr> iterator = likeExprs.iterator();
        SqlLogicExpr condition = new SqlLogicExpr(iterator.next());
        while (iterator.hasNext()) {
            // Join by OR
            condition.or(iterator.next());
        }

        return condition;
    }

    @Nullable
    private SqlWhere joinConditionsByAnd(Collection<SqlWhere> conditions) {
        if (conditions.size() == 0) {
            return null;
        }

        Iterator<SqlWhere> iterator = conditions.iterator();
        SqlWhere condition = iterator.next();
        condition.setInBracket(true);
        SqlLogicExpr joined = new SqlLogicExpr(condition);
        while (iterator.hasNext()) {
            condition = iterator.next();
            condition.setInBracket(true);
            joined.and(condition);
        }

        return joined;
    }

    public boolean isMutch(long id) {
        SqlQuery query = toQuery();
        SqlCondExpr idIs = new SqlCondExpr(TaskTable._ID).equalTo(id);
        idIs.setInBracket(true);
        SqlLogicExpr where = new SqlLogicExpr(idIs).and(query.getSelection());
        query.setSelection(where);

        return (SqlTableUseCase.query(query).length != 0);
    }

    /**
     * Parcelable implementation
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.mKeywordsInTitleOrDescription);
        dest.writeInt(this.mPickUpAchievementFlag);
        dest.writeString(this.mFilterName);
    }

    protected TaskFilter(Parcel in) {
        this.mKeywordsInTitleOrDescription = in.createStringArrayList();
        this.mPickUpAchievementFlag = in.readInt();
        this.mFilterName = in.readString();
    }

    public static final Parcelable.Creator<TaskFilter> CREATOR = new Parcelable.Creator<TaskFilter>() {
        @Override
        public TaskFilter createFromParcel(Parcel source) {
            return new TaskFilter(source);
        }

        @Override
        public TaskFilter[] newArray(int size) {
            return new TaskFilter[size];
        }
    };
}
