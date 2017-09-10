package com.f_candy_d.dutils;

import android.support.annotation.NonNull;

/**
 * Created by daichi on 17/08/30.
 */

public class QuantizableHelper {

    public static <T extends Enum<T> & Quantizable> T convertFromEnumClass(@NonNull Class<T> clazz, int quantity) {
        for (T enumValue : clazz.getEnumConstants()) {
            if (enumValue.quantize() == quantity) {
                return enumValue;
            }
        }
        return null;
    }
}