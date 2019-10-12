package com.android.malsoftware.techadvisor.RecycleViewSelect

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup

class MyItemDetailsLookup(private val mRecycleViewScroll: RecycleViewScroll) :
        ItemDetailsLookup<String>() {

    private val TAG = "MyItemDetailsLookup"

    override fun getItemDetails(e: MotionEvent): StringItemDetails? {
        val view = mRecycleViewScroll.findChildViewUnder(e.x, e.y)
        return if (view != null) {
            (mRecycleViewScroll.
                    getChildViewHolder(view) as AdapterSelector.SelectorHolder).itemDetails
        } else null
    }
}