package com.android.malsoftware.techadvisor;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MillDetailValues {
    private Context mContext;
    private List<String> mFieldNames = new ArrayList<String>();
    private List<Pairs> mPairsArray = new ArrayList<>();

    private MillDetailValues(Context context) {
        mContext = context;
        FieldType[] val = FieldType.values();
        for (int i = 0; i < FieldType.values().length; ++i) {
            mPairsArray.add(new Pairs(val[i], 0));
            mFieldNames.add(String.valueOf(val[i]));
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static MillDetailValues mMillDetailValues;

    public static MillDetailValues newInstance(Context context) {
        if (mMillDetailValues == null) mMillDetailValues = new MillDetailValues(context);
        return mMillDetailValues;
    }

    public List<Pairs> getPairsArray() {
        return mPairsArray;
    }

    public String getStringFieldValue(int position) {
        return String.valueOf(mPairsArray.get(position).getValue());
    }

    public List<String> getStringKeys() {
        return mFieldNames;
    }

    public FieldType getFieldType(int pos){
        return mPairsArray.get(pos).getFieldType();
    }
}