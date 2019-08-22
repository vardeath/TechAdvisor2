package com.android.malsoftware.techadvisor;

public class RecycleRange {
    private int mMinPosition;
    private int mMaxPosition;

    public RecycleRange(int minPosition, int maxPosition) {
        mMinPosition = minPosition;
        mMaxPosition = maxPosition;
    }

    public int getMinPosition() {
        return mMinPosition;
    }

    public int getMaxPosition() {
        return mMaxPosition;
    }
}