package com.android.malsoftware.techadvisor.RecycleViewSelect;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.RecycleRange;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewScroll extends RecyclerView {
	private final String TAG = "RecyclerVIewScroll";

	/**
	 * Класс позволяет осуществлять автоматическую прокрутку к выделенному элементу
	 * RecyclerView.
	 * В классе существуют диапазоны прокрутки (зависят от экрана устройства
	 * и определяются автоматически), которые позволяют осуществлять скроллинг не по одному
	 * элементу, а сразу на количество элеентов, которое может содержать экран устройства, при
	 * этом выделенный элемент всегда будет отображенным на экране.
	 * Если пользователь осуществлял скроллинг вручную, то прокрутка будет только к выделенному
	 * элементу без учета диапазона.
	 */
	public SelectionTracker tracker;
	private int mCurrentSelectedPosition = 0;
	private LinearLayoutManager mLinearLayoutManager = null;
	private List<String> mStringKeys = null;

	private int mFirstVisiblePosition;
	private int mLastVisiblePosition;
	private int mFirstPartiallyVisiblePosition;
	private int mLastPartiallyVisiblePosition;

	public void initStringKeys(List<String> keys){
		mStringKeys = keys;
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

	public void setSelectedPosition(int position) {
		mCurrentSelectedPosition = position;
		Log.d(TAG, "selected pos = " + position);
		//defineRanges();
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
		else mCurrentSelectedPosition = 0;

		tracker.select(mStringKeys.get(mCurrentSelectedPosition));

		if (mFirstPartiallyVisiblePosition > mCurrentSelectedPosition
				&& mLastPartiallyVisiblePosition > mCurrentSelectedPosition) {
			smoothScrollToPosition(mCurrentSelectedPosition - getScrollableCount()/2);
			return;
		}

		if (mCurrentSelectedPosition == 0 && mCurrentSelectedPosition != mFirstVisiblePosition) {
			smoothScrollToPosition(mCurrentSelectedPosition);
			return;
		}

		if (mCurrentSelectedPosition == mLastVisiblePosition) {
			int pointerPosition = mStringKeys.size() - 1;
			if (mCurrentSelectedPosition + getScrollableCount() < mStringKeys.size() - 1) {
				pointerPosition = mCurrentSelectedPosition + getScrollableCount();
			}
			smoothScrollToPosition(pointerPosition);
		}
	}

	public void decrementSelectedPosition() {
		refreshLayoutPositions();
		if (mCurrentSelectedPosition - 1 >= 0) --mCurrentSelectedPosition;
		else mCurrentSelectedPosition = mStringKeys.size() - 1;

		tracker.select(mStringKeys.get(mCurrentSelectedPosition));

		smoothScrollToPosition(mCurrentSelectedPosition);
	}

	private int getScrollableCount() {
		return mLastVisiblePosition - mFirstVisiblePosition;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		init();
	}
}