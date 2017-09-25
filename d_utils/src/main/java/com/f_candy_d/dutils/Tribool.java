package com.f_candy_d.dutils;

/**
 * Created by daichi on 9/23/17.
 */

public enum  Tribool {

    // 3-Status
    FALSE(-1),
    INDETERMINATE(0),
    TRUE(1);

    private final int mIntValue;

    Tribool(int intValue) {
        mIntValue = intValue;
    }

    public boolean asBool() {

        if (this == TRUE) {
            return true;
        } else if (this == FALSE) {
            return false;
        }

        throw new IllegalStateException(
                "State is " + INDETERMINATE.toString());
    }

    public int value() {
        return mIntValue;
    }

    public static Tribool from(int value) {
        Tribool[] tribools = values();
        for (Tribool tribool : tribools) {
            if (tribool.value() == value) {
                return tribool;
            }
        }

        throw new IllegalArgumentException(
                "Cannot resolve the parameter(" + value + ")");
    }
}
