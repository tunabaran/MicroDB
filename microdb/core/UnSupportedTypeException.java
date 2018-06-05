package com.tunabaranurut.microdb.core;

/**
 * Created by tunabaranurut on 5.06.2018.
 */

public class UnSupportedTypeException extends Exception{

    public UnSupportedTypeException(String type) {
        super(type + " is not supported by DB.");
    }
}
