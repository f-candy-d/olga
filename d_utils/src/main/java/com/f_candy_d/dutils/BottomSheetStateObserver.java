package com.f_candy_d.dutils;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.View;

/**
 * Created by daichi on 9/16/17.
 */

public class BottomSheetStateObserver {

    abstract static public class StateChangeCallback {
        protected void onStateChanged(@NonNull View bottomSheet, int newState) {}
        protected void onSlide(@NonNull View bottomSheet, float slideOffset) {}
    }

    public static final int STATE_COLLAPSED = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 2;
    public static final int STATE_HIDDEN = 3;
    public static final int STATE_SETTLING = 4;
    public static final int STATE_START_EXPANDING = 5;
    public static final int STATE_START_COLLAPSING = 6;

    private BottomSheetBehavior mTarget;
    private StateChangeCallback mListener;
    private int mOldState;

    public BottomSheetStateObserver() {
        this(null, null);
    }

    public BottomSheetStateObserver(BottomSheetBehavior target, StateChangeCallback listener) {
        setTarget(target);
        setListener(listener);
    }

    public void setListener(StateChangeCallback listener) {
        mListener = listener;
    }

    public void setTarget(BottomSheetBehavior target) {
        mTarget = target;
        if (mTarget != null) {
            mOldState = target.getState();
            mTarget.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    onSheetStateChanged(bottomSheet, newState);
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    onSheetSlide(bottomSheet, slideOffset);
                }
            });
        }
    }

    public BottomSheetBehavior getTarget() {
        return mTarget;
    }

    private void onSheetStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                notifyNewState(bottomSheet, STATE_COLLAPSED);
                break;

            case BottomSheetBehavior.STATE_DRAGGING:
                if (mOldState == BottomSheetBehavior.STATE_COLLAPSED) {
                    notifyNewState(bottomSheet, STATE_START_EXPANDING);
                } else if (mOldState == BottomSheetBehavior.STATE_EXPANDED) {
                    notifyNewState(bottomSheet, STATE_START_COLLAPSING);
                } else {
                    notifyNewState(bottomSheet, STATE_DRAGGING);
                }
                break;

            case BottomSheetBehavior.STATE_EXPANDED:
                notifyNewState(bottomSheet, STATE_EXPANDED);
                break;

            case BottomSheetBehavior.STATE_HIDDEN:
                notifyNewState(bottomSheet, STATE_HIDDEN);
                break;

            case BottomSheetBehavior.STATE_SETTLING:
                notifyNewState(bottomSheet, STATE_SETTLING);
                break;
        }

        mOldState = newState;
    }

    private void onSheetSlide(@NonNull View bottomSheet, float slideOffset) {
        if (mListener != null) {
            mListener.onSlide(bottomSheet, slideOffset);
        }
    }

    private void notifyNewState(@NonNull View bottomSheet, int newState) {
//        logState(newState);
        if (mListener != null) {
            mListener.onStateChanged(bottomSheet, newState);
        }
    }

    // FOR DEBUG
    private void logState(int state) {
        String str = "NO_STATE";
        switch (state) {
            case STATE_COLLAPSED: str = "COLLAPSED"; break;
            case STATE_DRAGGING: str = "DRAGGING"; break;
            case STATE_EXPANDED: str = "EXPANDED"; break;
            case STATE_HIDDEN: str = "HIDDEN"; break;
            case STATE_SETTLING: str = "SETTLING"; break;
            case STATE_START_EXPANDING: str = "START EXPANDING"; break;
            case STATE_START_COLLAPSING: str = "START COLLAPSING"; break;
        }

        Log.d("mylog", "STATE CHANGED -> " + str);
    }
}
