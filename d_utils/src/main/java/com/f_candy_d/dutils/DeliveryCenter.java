package com.f_candy_d.dutils;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daichi on 9/11/17.
 */

public class DeliveryCenter {

    private static final DeliveryCenter mInstance = new DeliveryCenter();
    private Map<String, Bundle> mWareHouse;

    public static DeliveryCenter getInstance() {
        return mInstance;
    }

    private DeliveryCenter() {
        mWareHouse = new HashMap<>();
    }

    public void send(Object destination, Bundle data) {
        mWareHouse.put(getReceiverName(destination), data);
    }

    public boolean check(Object receiver) {
        return mWareHouse.containsKey(getReceiverName(receiver));
    }

    public Bundle receive(Object receiver) {
        return mWareHouse.get(getReceiverName(receiver));
    }

    private String getReceiverName(Object receiver) {
        return receiver.getClass().getName();
    }
}
