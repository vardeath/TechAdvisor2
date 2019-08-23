package com.android.malsoftware.techadvisor.RecycleViewSelect;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;

public class MySelectionPredicate extends SelectionTracker.SelectionPredicate<String> {

    private Object mLastKey = null;

    @Override
    public boolean canSetStateForKey(@NonNull String key, boolean nextState) {
        /*if (mLastKey == key) return false;
        mLastKey = key;*/
        return true;
    }

    @Override
    public boolean canSetStateAtPosition(int position, boolean nextState) {
        return false;
    }

    @Override
    public boolean canSelectMultiple() {
        return false;
    }
}