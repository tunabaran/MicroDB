package com.tunabaranurut.microdb.base;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by tunabaranurut on 5.06.2018.
 */

public class KeySet {
    public List<String> keys = new LinkedList<>();

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
