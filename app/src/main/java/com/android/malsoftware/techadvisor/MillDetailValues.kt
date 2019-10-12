package com.android.malsoftware.techadvisor

import android.annotation.SuppressLint
import android.content.Context

import java.util.ArrayList

class MillDetailValues private constructor(private val mContext: Context) {
    private val mFieldNames = ArrayList<String>()
    private val mPairsArray = ArrayList<Pairs>()

    val pairsArray: List<Pairs>
        get() = mPairsArray

    val stringKeys: List<String>
        get() = mFieldNames

    init {
        val `val` = FieldType.values()
        for (i in FieldType.values().indices) {
            mPairsArray.add(Pairs(`val`[i], 0))
            mFieldNames.add(`val`[i].toString())
        }
    }

    fun getStringFieldValue(position: Int): String {
        return mPairsArray[position].value.toString()
    }

    fun getFieldType(pos: Int): FieldType {
        return mPairsArray[pos].fieldType
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mMillDetailValues: MillDetailValues? = null

        fun newInstance(context: Context?): MillDetailValues {
            if (mMillDetailValues == null) mMillDetailValues = context?.let { MillDetailValues(it) }
            return mMillDetailValues as MillDetailValues
        }
    }
}