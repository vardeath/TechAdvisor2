package com.android.malsoftware.techadvisor

import android.annotation.SuppressLint
import android.content.Context

import java.util.HashMap

import com.android.malsoftware.techadvisor.FieldType.*

class DescriptionsPresets private constructor(private val mContext: Context) {
    private val mGeneralPreset = HashMap<FieldType, String>()

    init {
        putHashMap(MillDiameter, R.string.Mill_Diameter_Description)
        putHashMap(MillCuttingSpeed, R.string.Mill_Cutting_Speed_Description)
        putHashMap(MillRevolutionQuantity, R.string.Mill_Revolution_Description)
        putHashMap(MillTeethQuantity, R.string.Mill_Teeth_Quantity_Description)
        putHashMap(MillToothFeed, R.string.Mill_Feed_Per_Tooth_Description)
        putHashMap(MillMinuteFeed, R.string.Mill_Minute_Feed_Description)
        putHashMap(MillRevolutionFeed, R.string.Mill_Revolution_Feed_Description)
        putHashMap(MillCuttingDepth, R.string.Mill_Cutting_Depth_Description)
        putHashMap(MillCuttingWidth, R.string.Mill_Cutting_Width_Description)
        putHashMap(MillGeneralAngle, R.string.Mill_General_Angle_Description)
        putHashMap(MillPathLength, R.string.Mill_Path_Length_Description)
        putHashMap(MillToolLength, R.string.Mill_Tool_Length_Description)
        putHashMap(MillAttackAngle, R.string.Mill_Front_Angle_Description)
        putHashMap(MillAverageChipWidth, R.string.Mill_Average_Chips_Width_Description)
        putHashMap(MillSpecificMaterialRemoval, R.string.Mill_Specific_Material_Removal_Description)
        putHashMap(MillCuttingTime, R.string.Mill_Cutting_Time_Description)
        putHashMap(MillMoment, R.string.Mill_Moment_Description)
        putHashMap(MillPower, R.string.Mill_Power_Description)
    }

    private fun getStringValue(id: Int): String {
        return mContext.resources?.getString(id).toString()
    }

    private fun putHashMap(fieldType: FieldType, id: Int) {
        mGeneralPreset[fieldType] = getStringValue(id)
    }

    fun getStringDescription(fieldType: FieldType): String? {
        return mGeneralPreset[fieldType]
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sDescriptionsPresets: DescriptionsPresets? = null

        internal fun newInstance(context: Context?): DescriptionsPresets {
            if (sDescriptionsPresets == null)
                sDescriptionsPresets = context?.let { DescriptionsPresets(it) }
            return sDescriptionsPresets as DescriptionsPresets
        }
    }
}