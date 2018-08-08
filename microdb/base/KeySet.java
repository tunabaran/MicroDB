package com.tunabaranurut.microdb.base;

import java.util.HashSet;

/**
 * Created by tunabaranurut on 5.06.2018.
 */

public class KeySet {
    public HashSet<String> keys = new HashSet<String>();

    public HashSet<String> getKeys() {
        return keys;
    }

    public void setKeys(HashSet<String> keys) {
        this.keys = keys;
    }
}
