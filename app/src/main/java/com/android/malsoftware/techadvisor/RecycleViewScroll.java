package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewScroll extends RecyclerView {

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
	private int mItemCount; //Общее число элементов в RecyclerView.

	private final Handler h = new Handler();
	/**
	 * Диапазоны значений для видимых областей скроллинга, текущий выделенны элемент отображается
	 * в пределах диапазона области.
	 */
	private List<RecycleRange> mRangesArray = new ArrayList<>();

	private MainMenuFragment.MainMenuAdaptor mAdapter = null;

	private int mOldSelectedPosition = 0;
	private int mCurrentSelectedPosition = 0;
	private boolean mHandScrolled = false;

	private int mRangeMinPosition;
	private int mRangeMaxPosition;

	private int mBaseValueAxisY = 0;

	@Override
	public boolean dispatchTouchEvent(MotionEvent iEv) {
		if (isEnabled()) {
			processEvent(iEv);
			super.dispatchTouchEvent(iEv);
			return true; //to keep receive event that follow down event
		}

		return super.dispatchTouchEvent(iEv);
	}

	private void processEvent(final MotionEvent iEv) {
		switch (iEv.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mBaseValueAxisY = (int) iEv.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				int newY = (int) iEv.getY();
				int difY = mBaseValueAxisY - newY;
				int MAX_VALUE = 10;
				if (Math.abs(difY) > MAX_VALUE) mHandScrolled = true;
				break;
		}
	}

	public void initRanges(int itemQuantity, int itemCount) {
		mItemCount = itemCount;
		for (int i = 0; i < mItemCount; i += itemQuantity) {
			int maxPosition;
			if (i + itemQuantity < mItemCount) maxPosition = i + itemQuantity - 1;
			else maxPosition = itemCount;
			RecycleRange element = new RecycleRange(i, maxPosition);
			mRangesArray.add(element);
		}
		mAdapter = (MainMenuFragment.MainMenuAdaptor) getAdapter();
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

	public void incrementSelectedPosition() {

		if (mCurrentSelectedPosition + 1 > mItemCount - 1) {
			setSelectedPosition(0, false);
		} else {
			setSelectedPosition(mCurrentSelectedPosition + 1, false);
		}
		doRunnableAction(scrollDown);
		applySelectedPosition();
	}

	public void decrementSelectedPosition() {

		if (mCurrentSelectedPosition - 1 < 0) {
			setSelectedPosition(mItemCount - 1, false);
		} else {
			setSelectedPosition(mCurrentSelectedPosition - 1, false);
		}
		doRunnableAction(scrollUp);
		applySelectedPosition();
	}

	private void doRunnableAction(Runnable actionScroll) {
		final Thread actionAutoScroll = new Thread(() -> {
			h.postDelayed(actionScroll, 20);
		}
		);
		actionAutoScroll.start();
	}

	private Runnable scrollDown = () -> {
		{
			if (mCurrentSelectedPosition == 0) {
				smoothScrollToPosition(mRangeMinPosition);
			} else if (verifyHandScroll())
				smoothScrollToPosition(mRangeMaxPosition);

		}
	};

	private Runnable scrollUp = () -> {
		{
			if (mCurrentSelectedPosition == mItemCount - 1) {
				smoothScrollToPosition(mRangeMaxPosition);
			} else if (verifyHandScroll())
				smoothScrollToPosition(mRangeMinPosition);

		}
	};

	private boolean verifyHandScroll() {
		if (mHandScrolled) {
			smoothScrollToPosition(mCurrentSelectedPosition);
			mHandScrolled = false;
			return false;
		}
		return true;
	}
}