package com.f_candy_d.dutils.view;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.f_candy_d.dutils.R;

public class ToggleColorBackground extends FrameLayout {

    private static final int DEFAULT_COLOR = Color.TRANSPARENT;
    private static final long DEFAULT_ANIM_DURATION = 300;
    private static final long DEFAULT_ANIM_DELAY = 0;

    private View mBaseView;
    private View mColorMaskView;

    private int mPreviousColor = DEFAULT_COLOR;
    private int mCurrentColor = mPreviousColor;
    private Point mRevealPoint = null;
    private long mAnimDuration = DEFAULT_ANIM_DURATION;
    private long mAnimDelay = DEFAULT_ANIM_DELAY;


    public ToggleColorBackground(Context context) {
        super(context);
        init(null, 0);
    }

    public ToggleColorBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ToggleColorBackground(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        loadAttributes(attrs, defStyle);

        // # Views

        mBaseView = new View(getContext());
        FrameLayout.LayoutParams params = new FrameLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mBaseView.setLayoutParams(params);
        mBaseView.setBackgroundColor(mCurrentColor);

        mColorMaskView = new View(getContext());
        params = new FrameLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mColorMaskView.setLayoutParams(params);
        mColorMaskView.setVisibility(INVISIBLE);

        addView(mBaseView);
        addView(mColorMaskView);
    }

    private void loadAttributes(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ToggleColorBackground, defStyle, 0);

        // Default color
        mPreviousColor = a.getColor(
                R.styleable.ToggleColorBackground_defaultColor, mPreviousColor);
        mCurrentColor = mPreviousColor;

        a.recycle();
    }

    public void toggleColor(final int color, int cx, int cy, long duration, long delay) {
        if (color == mCurrentColor) {
            return;
        }

        if (21 <= Build.VERSION.SDK_INT) {
            int finalRadius = Math.max(mColorMaskView.getWidth(), mColorMaskView.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(mColorMaskView, cx, cy, 0, finalRadius);
            anim.setDuration(duration);
            anim.setStartDelay(delay);
            anim.addListener(new Animator.AnimatorListener() {
                @Override public void onAnimationStart(Animator animator) {
                    mColorMaskView.setBackgroundColor(color);
                    mColorMaskView.setVisibility(VISIBLE);
                }
                @Override public void onAnimationCancel(Animator animator) {}
                @Override public void onAnimationRepeat(Animator animator) {}

                @Override
                public void onAnimationEnd(Animator animator) {
                    mBaseView.setBackgroundColor(color);
                    mColorMaskView.setVisibility(INVISIBLE);
                    // Update colors
                    mPreviousColor = mCurrentColor;
                    mCurrentColor = color;
                }
            });

            anim.start();

        } else {
            throw new RuntimeException(
                    "required API level is 21, but current is " + Build.VERSION.SDK_INT);
        }
    }

    public void toggleColor(final int color, int cx, int cy, long duration) {
        toggleColor(color, cx, cy, duration, mAnimDelay);
    }

    public void toggleColor(final int color, int cx, int cy) {
        toggleColor(color, cx, cy, mAnimDuration, mAnimDelay);
    }

    public void toggleColor(int color) {
        if (mRevealPoint != null) {
            toggleColor(color, mRevealPoint.x, mRevealPoint.y, mAnimDuration, mAnimDelay);
        } else {
            // By default, reveal point is the center of this view
            toggleColor(color, getWidth() / 2, getHeight() / 2, mAnimDuration, mAnimDelay);
        }
    }

    public void toggleColor() {
        // Use the previous background color as a new color
        toggleColor(mPreviousColor);
    }

    public void setRevealPoint(int x, int y) {
        if (mRevealPoint == null) {
            mRevealPoint = new Point(x, y);
        } else {
            mRevealPoint.set(x, y);
        }
    }

    public void setAnimDuration(long duration) {
        mAnimDuration = duration;
    }

    public long getAnimDuration() {
        return mAnimDuration;
    }

    public void setAnimDelay(long animDelay) {
        mAnimDelay = animDelay;
    }

    public long getAnimDelay() {
        return mAnimDelay;
    }

    public void setCurrentColor(int color) {
        mPreviousColor = mCurrentColor;
        mCurrentColor = color;
        mBaseView.setBackgroundColor(color);
    }

    public int getCurrentColor() {
        return mCurrentColor;
    }

    /**
     * Save Status
     * ---------------------------------------------------------- */

    private static class SavedState extends View.BaseSavedState {

        private int previousColor;
        private int currentColor;
        private long animDuration;
        private long animDelay;
        private Point revealPoint;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            previousColor = in.readInt();
            currentColor = in.readInt();
            animDuration = in.readLong();
            animDelay = in.readLong();

            boolean isRevealPointNotNull = (in.readInt() != 0);
            revealPoint = null;
            if (isRevealPointNotNull) {
                revealPoint = new Point();
                revealPoint.x = in.readInt();
                revealPoint.y = in.readInt();
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(previousColor);
            out.writeInt(currentColor);
            out.writeLong(animDuration);
            out.writeLong(animDelay);

            boolean isRevealPointNotNull = (revealPoint != null);
            // true == 1, false == 0
            out.writeInt(isRevealPointNotNull ? 1 : 0);
            if (isRevealPointNotNull) {
                out.writeInt(revealPoint.x);
                out.writeInt(revealPoint.y);
            }
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.previousColor = mPreviousColor;
        savedState.currentColor = mCurrentColor;
        savedState.animDuration = mAnimDuration;
        savedState.animDelay = mAnimDelay;
        savedState.revealPoint = mRevealPoint;

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mPreviousColor = savedState.previousColor;
        mCurrentColor = savedState.currentColor;
        mAnimDuration = savedState.animDuration;
        mAnimDelay = savedState.animDelay;
        mRevealPoint = savedState.revealPoint;

        mBaseView.setBackgroundColor(mCurrentColor);
    }
}
