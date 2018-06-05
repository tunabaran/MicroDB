package com.tunabaranurut.microdb.core;

/**
 * Created by tunabaranurut on 5.06.2018.
 */

public class ReservedKeyException extends Exception{

    public ReservedKeyException(String key) {
        super(">" + key + "<" + "is reserved for internal uses.");
    }
}
