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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AutoScrolledRecycleView extends RecyclerView {

	/**Общее число элементов в RecyclerView.*/
    private int mItemCount;

	/**Диапазоны значений для видимых областей скроллинга, текущий выделенны элемент отображается
	 * в пределах диапазона области.*/
    private List<RecycleRange> mRangesArray = new ArrayList<>();


    private int mOldSelectedPosition = 0;
    private int mCurrentSelectedPosition = 0;
    private int mSelectedRange = 0;
    private int mHandScrollDirection;
    private boolean mHandScrolled = false; /** Индикатор скроллинга в ручном режиме.*/
    private final Handler h = new Handler();
    private final int DOWN = 0;
    private final int UP = 1;
    private int mYLastValue = 0;

    public void setHandScrolled(boolean handScrolled) {
        mHandScrolled = handScrolled;
        mHandScrollDirection = UP;
    }

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
                mYLastValue = (int) iEv.getY();
                break;

            case MotionEvent.ACTION_UP:
                mHandScrolled = true;
                mYLastValue = (int) iEv.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int newY = (int) iEv.getY();
                int difY = mYLastValue - newY;

                int MAX_VALUE = 20;
                int MIN_VALUE = -20;
                if (difY > MAX_VALUE) {
                    mHandScrollDirection = DOWN;
                    mYLastValue = newY;
                } else if (difY < MIN_VALUE) {
                    mHandScrollDirection = UP;
                    mYLastValue = newY;
                }
                break;
        }
    }

    public void initRanges(int itemQuantity, int itemCount) {
        /*
          Кастомная реализация обеспечивает скроллинг вверх и вниз, и отображение выделенной позиции
          в видимой области RecyclerVIew.
         */
        mItemCount = itemCount;
        int rangeNumber = 0;
        for (int i = 0; i < mItemCount; i += itemQuantity) {
            int maxPosition;
            if (i + itemQuantity < itemCount) maxPosition = i + itemQuantity -1;
            else maxPosition = itemCount;
            RecycleRange element = new RecycleRange(rangeNumber, i, maxPosition);
            mRangesArray.add(element);
            ++rangeNumber;
        }
    }

    public AutoScrolledRecycleView(@NonNull Context context) {
        super(context);
    }

    public AutoScrolledRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrolledRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSelectedPosition(int position) {
        mOldSelectedPosition = mCurrentSelectedPosition;
        mCurrentSelectedPosition = position;
        for (int i = 0; i < mRangesArray.size(); ++i) {
            int minPosition = mRangesArray.get(i).getMinPosition();
            int maxPosition = mRangesArray.get(i).getMaxPosition();
            int regionNumber = mRangesArray.get(i).getRegionNumber();
            if (mCurrentSelectedPosition >= minPosition && mCurrentSelectedPosition <= maxPosition) {
                mSelectedRange = regionNumber;
                break;
            }
        }
        if (mHandScrolled) applySelectedPosition();
    }

    private void applySelectedPosition() {
        Objects.requireNonNull(getAdapter()).notifyItemChanged(mCurrentSelectedPosition);
        Objects.requireNonNull(getAdapter()).notifyItemChanged(mOldSelectedPosition);
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }

    public void incrementSelectedPosition() {

        if (mCurrentSelectedPosition + 1 > mItemCount - 1) {
            setSelectedPosition(0);
        } else {
            setSelectedPosition(mCurrentSelectedPosition + 1);
        }

        doAutoScroll(scrollDown);
        applySelectedPosition();
    }

    public void decrementSelectedPosition() {

        if (mCurrentSelectedPosition - 1 < 0) {
            setSelectedPosition(mItemCount - 1);
        } else {
            setSelectedPosition(mCurrentSelectedPosition - 1);
        }

        doAutoScroll(scrollUp);
        applySelectedPosition();
    }

    private void doAutoScroll(Runnable actionScroll) {
        final Thread actionAutoScroll = new Thread(() -> {
            {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    h.post(actionScroll);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        actionAutoScroll.start();
    }

    private Runnable scrollDown = () -> {
        {
            if (isHandScrolled()) {
                if (mSelectedRange == 0) {
                    smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMinPosition());
                } else
                    smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMaxPosition());
            }
        }
    };

    private Runnable scrollUp = () -> {
        {
            if (isHandScrolled()) {
                if (mSelectedRange == mRangesArray.size()-1) {
                    smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMaxPosition());
                } else
                    smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMinPosition());
            }
        }
    };

    private boolean isHandScrolled() {
        if (mHandScrolled) {
            if (mHandScrollDirection == DOWN) {
                smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMinPosition());
            }
            if (mHandScrollDirection == UP) {
                smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMaxPosition());
            }
            mHandScrolled = false;
            return false;
        }
        return true;
    }
}