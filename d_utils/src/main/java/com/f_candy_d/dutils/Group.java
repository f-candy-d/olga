package com.f_candy_d.dutils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by daichi on 9/8/17.
 */

public class Group<T> {

    private ArrayList<T> mMembers;
    private String mName;

    public Group() {
        this(null, null);
    }

    public Group(String name) {
        this(name, null);
    }

    public Group(String name, Collection<T> init) {
        if (init != null) {
            mMembers = new ArrayList<>(init);
        } else {
            mMembers = new ArrayList<>();
        }

        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public ArrayList<T> getMembers() {
        return mMembers;
    }

    public void setMembers(ArrayList<T> members) {
        mMembers = members;
    }
}
