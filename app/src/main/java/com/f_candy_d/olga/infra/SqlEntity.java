package com.f_candy_d.olga.infra;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.f_candy_d.dutils.Quantizable;
import com.f_candy_d.dutils.QuantizableHelper;

import java.util.Calendar;
import java.util.Set;

/**
 * Created by daichi on 9/10/17.
 */

final public class SqlEntity {

    @Nullable
    private String mTableName;
    @NonNull private ContentValues mValueMap;

    public SqlEntity() {
        this(null, null);
    }

    public SqlEntity(String tableName) {
        this(tableName, null);
    }

    public SqlEntity(ContentValues valueMap) {
        this(null, valueMap);
    }

    public SqlEntity(String tableName, ContentValues valueMap) {
        mTableName = tableName;
        if (valueMap != null) {
            mValueMap = new ContentValues(valueMap);
        } else {
            mValueMap = new ContentValues();
        }
    }

    public Set<String> getColumns() {
        return mValueMap.keySet();
    }

    public boolean hasColumn(String column) {
        return (column != null && mValueMap.containsKey(column));
    }

    public void setTableName(@Nullable String tableName) {
        mTableName = tableName;
    }

    @Nullable
    public String getTableName() {
        return mTableName;
    }

    @NonNull
    public ContentValues getValueMap() {
        return mValueMap;
    }

    /**
     * region; Setter
     */

    public void put(@NonNull String column, int value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, long value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, short value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, float value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, double value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, boolean value) {
        if (value) {
            mValueMap.put(column, Repository.SQLITE_BOOL_TRUE);
        } else {
            mValueMap.put(column, Repository.SQLITE_BOOL_FALSE);
        }
    }

    public void put(@NonNull String column, String value) {
        mValueMap.put(column, value);
    }

    public void put(@NonNull String column, @NonNull Quantizable value) {
        mValueMap.put(column, value.quantize());
    }

    public void put(@NonNull String column, @NonNull Calendar calendar) {
        // Store as long value
        mValueMap.put(column, calendar.getTimeInMillis());
    }

    /**
     * region; Getter
     */

    public int getIntOrDefault(@NonNull String column, int defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsInteger(column);
        }
        return defult;
    }

    public long getLongOrDefault(@NonNull String column, long defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsLong(column);
        }
        return defult;
    }

    public short getShortOrDefault(@NonNull String column, short defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsShort(column);
        }
        return defult;
    }

    public float getFloatOrDefault(@NonNull String column, float defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsFloat(column);
        }
        return defult;
    }

    public double getDoubleOrDefault(@NonNull String column, double defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsDouble(column);
        }
        return defult;
    }

    public boolean getBooleanOrDefault(@NonNull String column, boolean defult) {
        if (mValueMap.containsKey(column)) {
            int intValue = mValueMap.getAsInteger(column);
            if (intValue == Repository.SQLITE_BOOL_TRUE) {
                return true;
            } else if (intValue == Repository.SQLITE_BOOL_FALSE) {
                return false;
            } else {
                throw new IllegalArgumentException();
            }
        }

        return defult;
    }

    public String getStringOrDefault(@NonNull String column, String defult) {
        if (mValueMap.containsKey(column)) {
            return mValueMap.getAsString(column);
        }
        return defult;
    }

    public <T extends Quantizable> T getQuantizableOrDefault
            (@NonNull String column, Quantizable.Converter<T> converter, T defult) {

        if (mValueMap.containsKey(column)) {
            final int quantity = mValueMap.getAsInteger(column);
            return converter.convertFromQuantity(quantity);
        }
        return defult;
    }

    public <T extends Enum<T> & Quantizable> T getQuantizableEnumOrDefault
            (@NonNull String column, Class<T> enumClass, T defult) {

        if (mValueMap.containsKey(column)) {
            final int quantity = mValueMap.getAsInteger(column);
            return QuantizableHelper.convertFromEnumClass(enumClass, quantity);
        }
        return defult;
    }

    public Calendar getCalendarOrDefault(@NonNull String column, Calendar defult) {
        if (mValueMap.containsKey(column)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(mValueMap.getAsLong(column));
            return calendar;
        }
        return defult;
    }
}
