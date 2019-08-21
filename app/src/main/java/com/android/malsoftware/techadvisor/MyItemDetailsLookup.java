package com.android.malsoftware.techadvisor;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class MyItemDetailsLookup extends ItemDetailsLookup {
    private RecycleViewScroll mRecycleViewScroll;
    public MyItemDetailsLookup(RecycleViewScroll recycleViewScroll) {
        mRecycleViewScroll = recycleViewScroll;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = mRecycleViewScroll.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            return ((AdapterSelector.SelectorHolder) mRecycleViewScroll.getChildViewHolder(view)).getItemDetails();
        }
        return null;
    }
}
