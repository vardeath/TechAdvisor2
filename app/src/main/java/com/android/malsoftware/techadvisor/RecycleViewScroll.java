package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
	private int mRecViewItemsQuantity; //Число видимых элементов в RecyclerView.
	/**
	 * Диапазоны значений для видимых областей скроллинга, текущий выделенны элемент отображается
	 * в пределах диапазона области.
	 */
	private List<RecycleRange> mRangesArray = new ArrayList<>();
	private AdapterSelector mAdapter = null;
	private int mOldSelectedPosition = 0;
	private int mCurrentSelectedPosition = 0;
	private int mRangeMinPosition;
	private int mRangeMaxPosition;
	private LinearLayoutManager mLinearLayoutManager = null;

	private int mFirstVisiblePosition;
	private int mLastVisiblePosition;
	private int mFirstPartiallyVisiblePosition;
	private int mLastPartiallyVisiblePosition;

	public void initRanges(int visibleItemsQuantity, int itemTotalQuantity) {
		mRecViewItemsQuantity = itemTotalQuantity;
		for (int i = 0; i < mRecViewItemsQuantity; i += visibleItemsQuantity + 1) {
			int maxPosition;
			if (i + visibleItemsQuantity < mRecViewItemsQuantity) maxPosition = i + visibleItemsQuantity;
			else maxPosition = itemTotalQuantity - 1;
			Log.d(TAG, "minpos = " + i + " | maxPosition = " + maxPosition);
			RecycleRange element = new RecycleRange(i, maxPosition);
			mRangesArray.add(element);
		}
		mAdapter = (AdapterSelector) getAdapter();
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

	private void defineRanges() {
		for (int i = 0; i < mRangesArray.size(); ++i) {
			mRangeMinPosition = mRangesArray.get(i).getMinPosition();
			mRangeMaxPosition = mRangesArray.get(i).getMaxPosition();
			if (mCurrentSelectedPosition >= mRangeMinPosition
					&& mCurrentSelectedPosition <= mRangeMaxPosition) {
				break;
			}
		}
	}

	public void setSelectedPosition(int position, boolean isHandPressed) {
		mOldSelectedPosition = mCurrentSelectedPosition;
		mCurrentSelectedPosition = position;
		defineRanges();
		if (isHandPressed) applySelectedPosition();
	}

	private void applySelectedPosition() {
		mAdapter.notifyItemChanged(mCurrentSelectedPosition);
		mAdapter.notifyItemChanged(mOldSelectedPosition);
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

		int lastSelectedPosition = mCurrentSelectedPosition;
		refreshLayoutPositions();

		if (mCurrentSelectedPosition + 1 > mRecViewItemsQuantity - 1) {
			setSelectedPosition(0, false);
		} else {
			setSelectedPosition(mCurrentSelectedPosition + 1, false);
		}

		if (mCurrentSelectedPosition == 0) {
			if (mFirstVisiblePosition == 0) applySelectedPosition();
			smoothScrollToPosition(0);
			return;
		}

		if (mRangeMinPosition < mFirstVisiblePosition) {
			if (mCurrentSelectedPosition == mFirstPartiallyVisiblePosition) applySelectedPosition();
			smoothScrollToPosition(mRangeMinPosition);
			return;
		}

		if (mFirstVisiblePosition == mRangeMinPosition && mLastVisiblePosition == mRangeMaxPosition
		|| mLastVisiblePosition == mAdapter.getItemCount() - 1) {
			applySelectedPosition();
		} else {
			if (mLastPartiallyVisiblePosition != mLastVisiblePosition
					|| lastSelectedPosition == mFirstVisiblePosition) applySelectedPosition();
            smoothScrollToPosition(mRangeMaxPosition);
        }
	}

	public void decrementSelectedPosition() {

		int lastSelectedPosition = mCurrentSelectedPosition;
		refreshLayoutPositions();

		if (mCurrentSelectedPosition - 1 < 0) {
			setSelectedPosition(mRecViewItemsQuantity - 1, false);
		} else {
			setSelectedPosition(mCurrentSelectedPosition - 1, false);
		}

		if (mCurrentSelectedPosition == mRecViewItemsQuantity - 1) {
			if (mLastVisiblePosition == mCurrentSelectedPosition) applySelectedPosition();
			smoothScrollToPosition(mRecViewItemsQuantity - 1);
			return;
		}

		if (mLastVisiblePosition < mRangeMaxPosition) {
			if (mCurrentSelectedPosition == mLastPartiallyVisiblePosition) applySelectedPosition();
			smoothScrollToPosition(mRangeMaxPosition);
			return;
		}

		if (mFirstVisiblePosition == mRangeMinPosition && mLastVisiblePosition == mRangeMaxPosition
				|| mLastVisiblePosition == 0) {
			applySelectedPosition();
		} else {
			if (mFirstPartiallyVisiblePosition != mFirstVisiblePosition
					|| lastSelectedPosition == mLastVisiblePosition) applySelectedPosition();
			smoothScrollToPosition(mRangeMinPosition);
		}
	}
}