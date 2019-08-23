package com.android.malsoftware.techadvisor.RecycleViewSelect;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RecycleViewScroll extends RecyclerView {

    private final String TAG = "RecyclerVIewScroll";
    private SelectionTracker<String> mTracker;
    private int mCurrentSelectedPosition = 0;
    private LinearLayoutManager mLinearLayoutManager = null;
    private List<String> mStringKeys = null;
    private int mFirstVisiblePosition;
    private int mLastVisiblePosition;
    private int mFirstPartiallyVisiblePosition;
    private int mLastPartiallyVisiblePosition;
    private final int direction_UP = 0;
    private final int direction_DOWN = 1;
    private final int mScrollMinPosition = 0;
    private int mScrollMaxPosition = 0;

    public void initStringKeys(List<String> keys) {
        mStringKeys = keys;
        mScrollMaxPosition = mStringKeys.size() - 1;
    }

    public void init() {
        mLinearLayoutManager = (LinearLayoutManager) getLayoutManager();
    }

    public RecycleViewScroll(@NonNull Context context) {
        super(context);
    }

    public RecycleViewScroll(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewScroll(@NonNull Context context, @Nullable AttributeSet attrs,
                             int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setTracker(SelectionTracker<String> tracker) {
        mTracker = tracker;
        if (getAdapter() != null) ((AdapterSelector) getAdapter()).setTracker(mTracker);
    }

    public void setSelectedPosition(int position) {
        mCurrentSelectedPosition = position;
        //Log.d(TAG, "selected pos = " + position);
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }

    private void refreshLayoutPositions() {
        mFirstVisiblePosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        mLastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        mFirstPartiallyVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        mLastPartiallyVisiblePosition = mLinearLayoutManager.findLastVisibleItemPosition();
    }

    public void incrementSelectedPosition() {
        refreshLayoutPositions();
        if (mCurrentSelectedPosition + 1 < mStringKeys.size()) ++mCurrentSelectedPosition;
        else mCurrentSelectedPosition = mScrollMinPosition;

        if (isViewVisible()) return;

        int direction = getPositionDirection();

        switch (direction) {
            case direction_UP:
                smoothScrollToPosition(mCurrentSelectedPosition);
                break;
            case direction_DOWN:
                if (mCurrentSelectedPosition == mScrollMaxPosition) {
                    smoothScrollToPosition(mCurrentSelectedPosition);
                } else {
                    int pointerPosition = mCurrentSelectedPosition + (
                            mLastVisiblePosition - mFirstVisiblePosition);
                    if (pointerPosition > mScrollMaxPosition) pointerPosition = mScrollMaxPosition;
                    smoothScrollToPosition(pointerPosition);
                }
                break;
        }
    }

    public void decrementSelectedPosition() {
        refreshLayoutPositions();
        if (mCurrentSelectedPosition - 1 >= 0) --mCurrentSelectedPosition;
        else mCurrentSelectedPosition = mStringKeys.size() - 1;

        if (isViewVisible()) return;

        int direction = getPositionDirection();

        switch (direction) {
            case direction_UP:
                if (mCurrentSelectedPosition == mScrollMinPosition) {
                    smoothScrollToPosition(mCurrentSelectedPosition);
                } else {
                    int pointerPosition = mCurrentSelectedPosition -
                            (mLastVisiblePosition - mFirstVisiblePosition);
                    if (pointerPosition < mScrollMinPosition) pointerPosition = mScrollMinPosition;
                    smoothScrollToPosition(pointerPosition);
                }
                break;
            case direction_DOWN:
                smoothScrollToPosition(mCurrentSelectedPosition);
        }
    }

    private int getScrollableCount() {
        return mLastVisiblePosition - mFirstVisiblePosition;
    }

    private boolean isViewVisible() {
        if (isPositionCompletelyVisible()) {
            mTracker.select(mStringKeys.get(mCurrentSelectedPosition));
            return true;
        }

        if (isPositionPartiallyVisible()) {
            smoothScrollToPosition(mCurrentSelectedPosition);
            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }

    private boolean isPositionCompletelyVisible() {
        return mCurrentSelectedPosition >= mFirstVisiblePosition
                && mCurrentSelectedPosition <= mLastVisiblePosition;
    }

    private boolean isPositionPartiallyVisible() {
        return mCurrentSelectedPosition == mFirstPartiallyVisiblePosition
                || mCurrentSelectedPosition == mLastPartiallyVisiblePosition;
    }

    private int getPositionDirection() {
        if (mCurrentSelectedPosition < mFirstPartiallyVisiblePosition) return direction_UP;
        return direction_DOWN;
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) mTracker.select(mStringKeys.get(mCurrentSelectedPosition));
    }
}