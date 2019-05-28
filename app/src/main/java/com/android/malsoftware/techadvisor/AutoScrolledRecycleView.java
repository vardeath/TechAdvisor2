package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

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

    private int mItemQuantity;
    private int mItemCount;
    private List<RecycleRange> mRangesArray = new ArrayList<>();
    private int mOldSelectedPosition = 0;
    private int mCurrentSelectedPosition = 0;
    private int mSelectedRange = 0;
    private final Handler h = new Handler();
    private boolean mHandScrolled = false;

    public void initRanges(int itemQuantity, int itemCount) {
        mItemQuantity = itemQuantity;
        mItemCount = itemCount;
        int rangeNumber = 0;
        for (int i = 0; i < mItemCount; i+= mItemQuantity) {
            int minPosition = i;
            int maxPosition;
            if (i + mItemQuantity < itemCount) maxPosition = i + mItemQuantity;
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
        }
    }

    public int getCurrentSelectedPosition() {
        return mCurrentSelectedPosition;
    }

    public void incrementSelectedPosition() {
        final Thread tt = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    h.post(increment);
                    Log.d("mar2","thread increment started 1");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        final Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    h.post(scrollDown);
                    Log.d("mar2","thread scrollDown started 2");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });

        Executor tpe = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

        tpe.execute(new Runnable() {
            @Override
            public void run() {
                tt.start();
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                t.start();
            }
        });
    }

    Runnable increment= new Runnable() {
        @Override
        public void run() {
            if (mCurrentSelectedPosition + 1 > mItemCount -1) {
                setSelectedPosition(0);
            } else {
                setSelectedPosition(mCurrentSelectedPosition + 1);
            }
        }
    };

    Runnable scrollDown = new Runnable() {
        public void run() {
            if (mHandScrolled) {
                smoothScrollToPosition(mCurrentSelectedPosition);
                mHandScrolled = false;
            } else {
                if (mSelectedRange == 0) {
                    smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMinPosition());
                } else smoothScrollToPosition(mRangesArray.get(mSelectedRange).getMaxPosition());
            }
        }
    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mHandScrolled = true;
    }
}