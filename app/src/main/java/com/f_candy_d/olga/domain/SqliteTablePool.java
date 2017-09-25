package com.f_candy_d.olga.domain;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.SqliteRepository;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by daichi on 9/25/17.
 */

abstract public class SqliteTablePool<T extends SqlEntityObject> {

    public interface Callback {
        void onPooled(int index, int count);
        void onReleased(int index, int count);
        void onChanged(int index, int count);

    }

    private final String mTableName;
    private ArrayList<T> mPool;
    private Callback mCallback;

    public SqliteTablePool(String tableName) {
        mTableName = tableName;
        mPool = new ArrayList<>();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public T getAt(int index) {
        return mPool.get(index);
    }

    public T getById(long id) {
        for (T entity : mPool) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    public int pool(long id) {
        SqlEntity entity = Repository.getSqlite().selectRowById(mTableName, id);
        if (entity != null) {
            T entityObject = createEntityObject(entity);
            if (entityObject != null) {
                return pool(entityObject);
            }
        }

        return -1;
    }

    /**
     * Pool an entity or update if it is already pooled.
     * DO NOT pool an entity which does not exist in the database.
     */
    private int pool(T entity) {
        int index = indexOf(entity);
        if (0 <= index && !areColumnsTheSame(entity, getAt(index))) {
            mPool.set(index, entity);
            if (mCallback != null) {
                mCallback.onChanged(index, 1);
            }
            return index;
        } else {
            mPool.add(entity);
            index = mPool.size() - 1;
            if (mCallback != null) {
                mCallback.onPooled(index, 1);
            }
            return index;
        }
    }

    private void poolAll(Collection<T> entites) {
        Iterator<T> iterator = entites.iterator();
        while (iterator.hasNext()) {
            T entity = iterator.next();
            int index = indexOf(entity);
            if (0 <= index) {
                mPool.set(index, entity);
                if (mCallback != null) {
                    mCallback.onChanged(index, 1);
                }
                iterator.remove();
            }
        }

        int start = mPool.size();
        mPool.addAll(entites);
        if (mCallback != null) {
            mCallback.onPooled(start, entites.size());
        }
    }

    public void poolByQuery(SqlQuery query) {
        SqlEntity[] results = Repository.getSqlite().select(query);
        ArrayList<T> entites = new ArrayList<>(results.length);
        for (SqlEntity result : results) {
            T entity = createEntityObject(result);
            if (entity != null) {
                entites.add(entity);
            }
        }

        poolAll(entites);
    }

    abstract T createEntityObject(SqlEntity entity);
    abstract boolean areColumnsTheSame(T oldEntity, T newEntity);

    /**
     * Remove an entity from the pool.
     * This method does not delete it from the database.
     */
    public T release(int index) {
        T entity = mPool.remove(index);
        if (mCallback != null) {
            mCallback.onReleased(index, 1);
        }
        return entity;
    }

    public int release(T entity) {
        int index = indexOf(entity);
        mPool.remove(index);
        if (mCallback != null) {
            mCallback.onReleased(index, 1);
        }
        return index;
    }

    /**
     * Insert an entity into the database.
     * If it is needs to be pooled, do it.
     */
    public int insert(T entity) {
        SqlEntity e = entity.toSqlEntity(false);
        final long id = Repository.getSqlite().insert(e);
        if (id != Repository.SQLITE_NULL_ID) {
            entity.setId(id);
            if (isNecessaryToPool(entity)) {
                return pool(entity);
            }
        }

        return -1;
    }

    abstract boolean isNecessaryToPool(T entity);

    /**
     * Update an entity in the database.
     */
    public int update(T entity) {
        SqlEntity e = entity.toSqlEntity(true);
        boolean result = Repository.getSqlite().update(e);
        if (result && isNecessaryToPool(entity)) {
            return pool(entity);
        }

        return -1;
    }

    /**
     * Delete an entity from the database.
     * If it is pooled, remove from the pool using {@link SqliteTablePool#release(int)}.
     */
    public int delete(T entity) {
        boolean result = Repository.getSqlite().delete(entity.toSqlEntity(true));
        int index;
        if (result && 0 <= ((index = indexOf(entity)))) {
            release(index);
            return index;
        }

        return -1;
    }

    public int indexOf(T entity) {
        for (int i = 0; i < size(); ++i) {
            if (isEntityTheSame(entity, getAt(i))) {
                return i;
            }
        }

        return -1;
    }

    private boolean isEntityTheSame(T entity1, T entity2) {
        return (entity1 != null &&
                entity2 != null &&
                entity1.getId() == entity2.getId());
    }

    public int size() {
        return mPool.size();
    }
}
