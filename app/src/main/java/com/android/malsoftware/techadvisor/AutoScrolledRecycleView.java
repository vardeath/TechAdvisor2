package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AutoScrolledRecycleView extends RecyclerView {

    /**
     * Кастомная реализация обеспечивает скроллинг вверх и вниз, и отображение выделенной позиции в видимой области RecyclerVIew.
     */

    private int mItemQuantity; /**Количество одновременно видимых элементов в RecyclerView.*/
    private int mItemCount; /**Общее число элементов в RecyclerView.*/
    private List<RecycleRange> mRangesArray = new ArrayList<>(); /**Диапазоны значений для скроллинга, для отображения выделенного элемента.*/
    private int mOldSelectedPosition = 0;
    private int mCurrentSelectedPosition = 0;
    private int mSelectedRange = 0;
    private int mHandScrollDirection = 0;
    private boolean isHandScrolled = false; /** Индикатор скроллинга в ручном режиме.*/
    private final Handler h = new Handler();
    private final int DOWN = 0;
    private final int UP = 1;
    private int mYLastValue = 0;


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
                //Log.d("mar2","scrolled Down");
                mYLastValue = (int) iEv.getY();
                break;

            case MotionEvent.ACTION_UP:
                //Log.d("mar2","scrolled Up");
                isHandScrolled = true;
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
                    //Log.d("mar2","scrolled Down");
                } else if (difY < MIN_VALUE) {
                    mHandScrollDirection = UP;
                    mYLastValue = newY;
                    //Log.d("mar2","scrolled Up");
                }
                break;
        }
    }

    public void initRanges(int itemQuantity, int itemCount) {
        mItemQuantity = itemQuantity;
        mItemCount = itemCount;
        int rangeNumber = 0;
        for (int i = 0; i < mItemCount; i += mItemQuantity) {
            int minPosition = i;
            int maxPosition;
            if (i + mItemQuantity < itemCount) maxPosition = i + mItemQuantity -1;
            else maxPosition = itemCount;
            /*Log.d("mar2", "rangeNumber = " + rangeNumber);
            Log.d("mar2", "minPosition = " + minPosition);
            Log.d("mar2", "maxPosition = " + maxPosition);*/
            RecycleRange element = new RecycleRange(rangeNumber, minPosition, maxPosition);
            mRangesArray.add(element);
            ++rangeNumber;
            //Log.d("mar2", "mItemQuantity = " + mItemQuantity);
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
        Objects.requireNonNull(getAdapter()).notifyItemChanged(mCurrentSelectedPosition);
        Objects.requireNonNull(getAdapter()).notifyItemChanged(mOldSelectedPosition);
        for (int i = 0; i < mRangesArray.size(); ++i) {
            int minPosition = mRangesArray.get(i).getMinPosition();
            int maxPosition = mRangesArray.get(i).getMaxPosition();
            int regionNumber = mRangesArray.get(i).getRegionNumber();
            if (mCurrentSelectedPosition >= minPosition && mCurrentSelectedPosition <= maxPosition) {
                mSelectedRange = regionNumber;
                //Log.d("mar2", "mSelectedRange = " + regionNumber);
                break;
            }
            //Log.d("mar2", "Ranges = " + mRangesArray.size());
        }
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }

    public void incrementSelectedPosition() {
        doActions(increment, scrollDown);
    }

    public void decrementSelectedPosition() {
        doActions(decrement, scrollUp);
    }

    private void doActions(Runnable action1, Runnable action2) {
        final Thread actionIncrementPosition = new Thread(() -> {
            {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    h.post(action1);
                    //Log.d("mar2", "thread increment started 1");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        final Thread actionAutoScroll = new Thread(() -> {
            {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    h.post(action2);
                    //Log.d("mar2", "thread scrollDown started 2");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Executor tpe = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        tpe.execute(() -> {
            {
                actionIncrementPosition.start();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                actionAutoScroll.start();
            }
        });
    }

    private Runnable increment = () -> {
        {
            if (mCurrentSelectedPosition + 1 > mItemCount - 1) {
                setSelectedPosition(0);
            } else {
                setSelectedPosition(mCurrentSelectedPosition + 1);
            }
        }
    };

    private Runnable decrement = () -> {
        {
            if (mCurrentSelectedPosition - 1 < 0) {
                setSelectedPosition(mItemCount - 1);
            } else {
                setSelectedPosition(mCurrentSelectedPosition - 1);
            }
        }
    };

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
        if (isHandScrolled) {
            if (mHandScrollDirection == DOWN) {
                smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMinPosition());
            }
            if (mHandScrollDirection == UP) {
                smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMaxPosition());
            }
            isHandScrolled = false;
            return false;
        }
        return true;
    }
}