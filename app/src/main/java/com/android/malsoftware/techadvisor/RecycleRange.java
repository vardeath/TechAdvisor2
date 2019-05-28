package com.android.malsoftware.techadvisor;

public class RecycleRange {
    private int mRegionNumber;
    private int mMinPosition;
    private int mMaxPosition;

    RecycleRange (int regionNumber, int minPosition, int maxPosition) {
        mRegionNumber = regionNumber;
        mMinPosition = minPosition;
        mMaxPosition = maxPosition;
    }

    public int getRegionNumber() {
        return mRegionNumber;
    }

    public int getMinPosition() {
        return mMinPosition;
    }

    public int getMaxPosition() {
        return mMaxPosition;
    }
}