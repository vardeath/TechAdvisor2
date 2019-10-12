package com.android.malsoftware.techadvisor.RecycleViewSelect

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecycleViewScroll : RecyclerView {

    private val TAG = "RecyclerVIewScroll"
    private lateinit var mTracker: SelectionTracker<String>
    var currentSelectedPosition = 0
        private set
    private lateinit var mLinearLayoutManager: LinearLayoutManager
    private lateinit var mStringKeys: List<String>
    private var mFirstVisiblePosition: Int = 0
    private var mLastVisiblePosition: Int = 0
    private var mFirstPartiallyVisiblePosition: Int = 0
    private var mLastPartiallyVisiblePosition: Int = 0
    private val directionUp = 0
    private val directionDown = 1
    private val mScrollMinPosition = 0
    private var mScrollMaxPosition = 0

    /*private val scrollableCount: Int
        get() = mLastVisiblePosition - mFirstVisiblePosition*/

    private val isViewVisible: Boolean
        get() {
            if (isPositionCompletelyVisible) {
                mTracker.select(mStringKeys[currentSelectedPosition])
                return true
            }

            if (isPositionPartiallyVisible) {
                smoothScrollToPosition(currentSelectedPosition)
                return true
            }
            return false
        }

    private val isPositionCompletelyVisible: Boolean
        get() = currentSelectedPosition in mFirstVisiblePosition..mLastVisiblePosition

    private val isPositionPartiallyVisible: Boolean
        get() = currentSelectedPosition == mFirstPartiallyVisiblePosition
                || currentSelectedPosition == mLastPartiallyVisiblePosition

    private val positionDirection: Int
        get() = if (currentSelectedPosition < mFirstPartiallyVisiblePosition)
            directionUp else directionDown

    fun initStringKeys(keys: List<String>) {
        mStringKeys = keys
        mScrollMaxPosition = mStringKeys.size - 1
    }

    private fun init() {
        mLinearLayoutManager = layoutManager as LinearLayoutManager
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?,
                defStyle: Int) : super(context, attrs, defStyle)

    fun setTracker(tracker: SelectionTracker<String>) {
        mTracker = tracker
        if (adapter != null) (adapter as AdapterSelector).setTracker(mTracker)
    }

    fun setSelectedPosition(position: Int) {
        currentSelectedPosition = position
        //Log.d(TAG, "selected pos = " + position);
    }

    private fun refreshLayoutPositions() {
        mLinearLayoutManager.also {
            mFirstVisiblePosition = it.findFirstCompletelyVisibleItemPosition()
            mLastVisiblePosition = it.findLastCompletelyVisibleItemPosition()
            mFirstPartiallyVisiblePosition = it.findFirstVisibleItemPosition()
            mLastPartiallyVisiblePosition = it.findLastVisibleItemPosition()
        }
    }

    fun incrementSelectedPosition() {
        refreshLayoutPositions()
        if (currentSelectedPosition + 1 < mStringKeys.size)
            ++currentSelectedPosition
        else
            currentSelectedPosition = mScrollMinPosition

        if (isViewVisible) return

        when (positionDirection) {
            directionUp -> smoothScrollToPosition(currentSelectedPosition)
            directionDown -> if (currentSelectedPosition == mScrollMaxPosition) {
                smoothScrollToPosition(currentSelectedPosition)
            } else {
                var pointerPosition =
                        currentSelectedPosition + (mLastVisiblePosition - mFirstVisiblePosition)

                if (pointerPosition > mScrollMaxPosition) pointerPosition = mScrollMaxPosition
                smoothScrollToPosition(pointerPosition)
            }
        }
    }

    fun decrementSelectedPosition() {
        refreshLayoutPositions()
        if (currentSelectedPosition - 1 >= 0)
            --currentSelectedPosition
        else
            currentSelectedPosition = mStringKeys.size - 1

        if (isViewVisible) return

        when (positionDirection) {
            directionUp -> if (currentSelectedPosition == mScrollMinPosition) {
                smoothScrollToPosition(currentSelectedPosition)
            } else {
                var pointerPosition =
                        currentSelectedPosition - (mLastVisiblePosition - mFirstVisiblePosition)

                if (pointerPosition < mScrollMinPosition) pointerPosition = mScrollMinPosition
                smoothScrollToPosition(pointerPosition)
            }
            directionDown -> smoothScrollToPosition(currentSelectedPosition)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        init()
    }


    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == 0) mTracker.select(mStringKeys[currentSelectedPosition])
    }
}