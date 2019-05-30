package com.android.malsoftware.techadvisor;

public class Pairs {

    private FieldType mFieldType;
    private int mValue;

    public Pairs(FieldType fieldType, int value) {
        mFieldType = fieldType;
        mValue = value;
    }

    public FieldType getFieldType() {
        return mFieldType;
    }

    public int getValue() {
        return mValue;
    }
}
