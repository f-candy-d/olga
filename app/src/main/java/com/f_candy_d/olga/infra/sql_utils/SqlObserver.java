package com.f_candy_d.olga.infra.sql_utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/28/17.
 */

public class SqlObserver {

    abstract public static class ChangeListener {
        public void onRowInserted(String table, long id) {}
        public void onRowUpdated(String table, long id) {}
        public void onRowDeleted(String table, long id) {}
        public void onSomeRowsInserted(String table, int count) {}
        public void onSomeRowsDeleted(String table, int count) {}
        public void onSomeRowsUpdated(String table, int count) {}
        public void onTableModifired(String table) {}
        public void onDatabaseModifired() {}
    }

    private static final SqlObserver mInstance = new SqlObserver();

    private final Map<ChangeListener, ArrayList<String>> mListenerMap;

    public static SqlObserver getInstance() {
        return mInstance;
    }

    private SqlObserver() {
        mListenerMap = new HashMap<>();
    }

    public void registerChangeListener(ChangeListener listener, String... tables) {
        registerChangeListener(listener, Arrays.asList(tables));
    }

    public void registerChangeListener(ChangeListener listener, Collection<String> tables) {
        mListenerMap.put(listener, new ArrayList<>(tables));
    }

    public void unregisterChangeListener(ChangeListener listener) {
        mListenerMap.remove(listener);
    }

    public void clear() {
        mListenerMap.clear();
    }

    public void notifyRowInserted(String table, long id) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onRowInserted(table, id);
                    listener.onSomeRowsInserted(table, 1);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifySomeRowsInserted(String table, int count) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onSomeRowsInserted(table, count);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifyRowDeleted(String table, long id) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onRowDeleted(table, id);
                    listener.onSomeRowsDeleted(table, 1);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifySomeRowsDeleted(String table, int count) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onSomeRowsDeleted(table, count);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifyRowUpdated(String table, long id) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onRowUpdated(table, id);
                    listener.onSomeRowsUpdated(table, 1);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifySomeRowsUpdated(String table, int count) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onSomeRowsUpdated(table, count);
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifySomeRowsModifired(String table) {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                if (mListenerMap.get(listener).contains(table)) {
                    listener.onTableModifired(table);
                    listener.onDatabaseModifired();
                }
            }
        }
    }

    public void notifyDatabaseModifired() {
        for (ChangeListener listener : mListenerMap.keySet()) {
            if (listener != null) {
                listener.onDatabaseModifired();
            }
        }
    }
}
