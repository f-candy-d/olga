package com.f_candy_d.olga.presentation.view_model;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.olga.data_store.DbContract;
import com.f_candy_d.olga.presentation.fragment.FormFragment;
import com.f_candy_d.vvm.ActivityViewModel;

import java.util.ArrayList;

/**
 * Created by daichi on 9/12/17.
 */

abstract public class FormViewModel extends ActivityViewModel {

    /**
     * Host activities must implement this interface.
     */
    public interface RequestReplyListener {
        void onNormalFinish(long contentId);
        void onAbnormalFinish();
    }

    private RequestReplyListener mRequestReplyListener;

    public FormViewModel(Context context, RequestReplyListener listener, long contentId) {
        super(context);
        mRequestReplyListener = listener;
        onInitWithId(contentId);
    }

    abstract protected void onInitWithId(long contentId);

    @NonNull
    abstract public ArrayList<FormFragment> getFormFragments();

    /**
     * @return Error messages if any data is invalid, null otherwise.
     */
    @Nullable
    abstract public String[] onDataInput(Bundle data, String fragmentTag);

    /**
     * 1. Apply data validation to a content.
     *
     * 2a. If there are no errors...
     *     -> Save content data, and then if successful call #onNormalFinish(), or call #onAbnormalFinish() otherwise.
     *        Finally, return null.
     *
     * 2b. If there are any errors...
     *     -> Return error messages.
     */
    public String[] onRequestFinish() {
        String[] errors = isContentValid();
        if (errors == null || errors.length == 0) {
            long idResult = onSaveContent();
            if (idResult != DbContract.NULL_ID) {
                mRequestReplyListener.onNormalFinish(idResult);
            } else {
                mRequestReplyListener.onAbnormalFinish();
            }
            return null;

        } else {
            return errors;
        }
    }

    /**
     * Apply data validation.
     * @return Error message if any data is invalid, null otherwise
     */
    @Nullable
    abstract protected String[] isContentValid();

    /**
     * Save content data in this method.
     * @return Row ID of a content if saving data succeeds, {@link DbContract#NULL_ID} otherwise
     */
    abstract protected long onSaveContent();
}
