package com.f_candy_d.vvm;

import android.os.Bundle;
import android.util.SparseArray;

/**
 * Created by daichi on 9/11/17.
 */

public class ActivityResultReceiver {

    private static final ActivityResultReceiver mInstance = new ActivityResultReceiver();
    private SparseArray<ViewActivity.OnResultListener> mResultListeners;

    public static ActivityResultReceiver getInstance() {
        return mInstance;
    }

    private ActivityResultReceiver() {
        mResultListeners = new SparseArray<>();
    }

    void registerListener(int requestCode, ViewActivity.OnResultListener listener) {
        if (listener != null) {
            mResultListeners.put(requestCode, listener);
        }
    }

    /**
     * Call this method in Activity#onActivityResult()
     */
    void onResult(int requestCode, int resultCode, Bundle data) {
        ViewActivity.OnResultListener listener = mResultListeners.get(requestCode, null);
        if (listener != null) {
            listener.onResult(resultCode, data);
        }
    }

    void unregisterListener(int requestCode) {
        mResultListeners.remove(requestCode);
    }
}
