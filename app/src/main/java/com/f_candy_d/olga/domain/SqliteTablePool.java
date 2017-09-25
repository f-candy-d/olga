package com.f_candy_d.olga.domain;

import android.support.v7.util.SortedList;

import com.f_candy_d.olga.domain.structure.SqlEntityObject;
import com.f_candy_d.olga.domain.usecase.SqlTableUseCase;
import com.f_candy_d.olga.infra.Repository;
import com.f_candy_d.olga.infra.SqlEntity;
import com.f_candy_d.olga.infra.sql_utils.SqlQuery;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by daichi on 9/25/17.
 */

abstract public class SqliteTablePool<T extends SqlEntityObject> {

    public interface Callback {
        void onPooled(int index, int count);
        void onReleased(int index, int count);
        void onChanged(int index, int count);
        void onMoved(int fromIndex, int toIndex);
    }

    private final String mTableName;
    private SortedList<T> mPool;
    private Callback mCallback;

    public SqliteTablePool(String tableName, Class<T> klass) {
        mTableName = tableName;
        mPool = new SortedList<>(klass, new SortedList.Callback<T>() {
            @Override
            public int compare(T o1, T o2) {
                return compareEntites(o1, o2);
            }

            @Override
            public void onChanged(int position, int count) {
                if (mCallback != null) {
                    mCallback.onChanged(position, count);
                }
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return !areColumnsTheSame(oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return areEntitesTheSame(item1, item2);
            }

            @Override
            public void onInserted(int position, int count) {
                if (mCallback != null) {
                    mCallback.onPooled(position, count);
                }
            }

            @Override
            public void onRemoved(int position, int count) {
                if (mCallback != null) {
                    mCallback.onReleased(position, count);
                }
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                if (mCallback != null) {
                    mCallback.onMoved(fromPosition, toPosition);
                }
            }
        });
    }

    protected int compareEntites(T entity1, T entity2) {
        return 0;
    }

    final public void setCallback(Callback callback) {
        mCallback = callback;
    }

    final public T getAt(int index) {
        return mPool.get(index);
    }

    final public T getById(long id) {
        for (int i = 0; i < mPool.size(); ++i) {
            if (mPool.get(i).getId() == id) {
                return mPool.get(i);
            }
        }
        return null;
    }

    final public int pool(long id) {
        SqlEntity entity = SqlTableUseCase.findById(id, mTableName);
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
        return mPool.add(entity);
    }

    final public void poolAll(Collection<T> entites) {
        mPool.addAll(entites);
    }

    @SafeVarargs
    final public void poolAll(T... entites) {
        mPool.addAll(entites);
    }

    final public void poolByQuery(SqlQuery query) {
        SqlEntity[] results = SqlTableUseCase.query(query);
        mPool.beginBatchedUpdates();
        for (SqlEntity result : results) {
            T entity = createEntityObject(result);
            if (entity != null) {
                mPool.add(entity);
            }
        }
        mPool.endBatchedUpdates();
    }

    abstract T createEntityObject(SqlEntity entity);
    abstract boolean areColumnsTheSame(T oldEntity, T newEntity);

    /**
     * Remove an entity from the pool.
     * This method does not delete it from the database.
     */
    final public T releaseAt(int index) {
        return mPool.removeItemAt(index);
    }

    final public boolean release(T entity) {
        return mPool.remove(entity);
    }

    final public void clear() {
        mPool.clear();
    }

    final public void swapPoolByQuery(SqlQuery query) {
        SqlEntity[] results = SqlTableUseCase.query(query);
        ArrayList<T> entites = new ArrayList<>(results.length);
        for (SqlEntity result : results) {
            T entity = createEntityObject(result);
            if (entity != null) {
                entites.add(entity);
            }
        }

        for (int i = mPool.size() - 1; 0 <= i; --i) {
            for (int j = 0; j < entites.size(); ++j) {
                if (mPool.get(i).getId() == entites.get(j).getId()) {
                    mPool.updateItemAt(i, entites.get(j));
                    entites.remove(j);
                    break;

                } else if (j == entites.size() - 1) {
                    mPool.removeItemAt(i);
                }
            }
        }

        mPool.addAll(entites);
    }

    /**
     * Insert an entity into the database.
     * If it is needs to be pooled, do it.
     */
    final public int insert(T entity) {
        final long id = SqlTableUseCase.insert(entity);
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
    final public int update(T entity) {
        boolean result = SqlTableUseCase.update(entity);
        if (result) {
            int index = indexOf(entity);
            if (SortedList.INVALID_POSITION != index) {
                mPool.updateItemAt(index, entity);
                return index;

            } else if (isNecessaryToPool(entity)) {
                return pool(entity);
            }
        }

        return -1;
    }

    /**
     * Delete an entity from the database.
     * If it is pooled, remove from the pool using {@link SqliteTablePool#releaseAt(int)}.
     */
    final public int delete(T entity) {
        boolean result = SqlTableUseCase.delete(entity);
        int index;
        if (result && SortedList.INVALID_POSITION != ((index = indexOf(entity)))) {
            releaseAt(index);
            return index;
        }

        return -1;
    }

    final public int indexOf(T entity) {
        return mPool.indexOf(entity);
    }

    private boolean areEntitesTheSame(T entity1, T entity2) {
        return (entity1 != null &&
                entity2 != null &&
                entity1.getId() == entity2.getId());
    }

    final public int size() {
        return mPool.size();
    }
}
