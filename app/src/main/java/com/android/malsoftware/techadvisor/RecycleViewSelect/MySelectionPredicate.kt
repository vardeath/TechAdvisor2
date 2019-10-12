package com.android.malsoftware.techadvisor.RecycleViewSelect

import androidx.recyclerview.selection.SelectionTracker

class MySelectionPredicate : SelectionTracker.SelectionPredicate<String>() {

    private val mLastKey: Any? = null

    override fun canSetStateForKey(key: String, nextState: Boolean): Boolean {
        /*if (mLastKey == key) return false;
        mLastKey = key;*/
        return true
    }

    override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean {
        return false
    }

    override fun canSelectMultiple(): Boolean {
        return false
    }
}