package com.android.malsoftware.techadvisor;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MillDetailValues {
    private Context mContext;

    private List<Pairs> mPairsArray = new ArrayList<>();

    private MillDetailValues(Context context) {
        mContext = context;
        FieldType[] val = FieldType.values();
        for (int i = 0; i < FieldType.values().length; ++i) {
            mPairsArray.add(new Pairs(val[i], 0));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static MillDetailValues mMillDetailValues;

    public static MillDetailValues newInstance(Context context) {
        if (mMillDetailValues == null) mMillDetailValues = new MillDetailValues(context);
        return mMillDetailValues;
    }

    List<Pairs> getPairsArray() {
        return mPairsArray;
    }
}