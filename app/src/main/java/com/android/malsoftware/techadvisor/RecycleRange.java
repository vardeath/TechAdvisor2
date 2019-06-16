package com.android.malsoftware.techadvisor;

class RecycleRange {
    private int mMinPosition;
    private int mMaxPosition;

    RecycleRange (int minPosition, int maxPosition) {
        mMinPosition = minPosition;
        mMaxPosition = maxPosition;
    }

    int getMinPosition() {
        return mMinPosition;
    }

    int getMaxPosition() {
        return mMaxPosition;
    }
}