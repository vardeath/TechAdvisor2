package com.android.malsoftware.techadvisor;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.HashMap;

import static com.android.malsoftware.techadvisor.FieldType.*;

public class DescriptionsPresets {
    private Context mContext;
    @SuppressLint("StaticFieldLeak")
    private static DescriptionsPresets sDescriptionsPresets;
    private HashMap <FieldType, String> mGeneralPreset = new HashMap<>();

    public static DescriptionsPresets newInstance(Context context) {
        if (sDescriptionsPresets == null)
            sDescriptionsPresets = new DescriptionsPresets(context);
        return sDescriptionsPresets;
    }

    private DescriptionsPresets(Context context) {
        mContext = context;
        putHashMap(MillDiameter, R.string.Mill_Diameter_Description);
        putHashMap(MillCuttingSpeed, R.string.Mill_Cutting_Speed_Description);
        putHashMap(MillRevolutionQuantity, R.string.Mill_Revolution_Description);
        putHashMap(MillTeethQuantity, R.string.Mill_Teeth_Quantity_Description);
        putHashMap(MillToothFeed, R.string.Mill_Feed_Per_Tooth_Description);
        putHashMap(MillMinuteFeed, R.string.Mill_Minute_Feed_Description);
        putHashMap(MillRevolutionFeed, R.string.Mill_Revolution_Feed_Description);
        putHashMap(MillCuttingDepth, R.string.Mill_Cutting_Depth_Description);
        putHashMap(MillCuttingWidth, R.string.Mill_Cutting_Width_Description);
        putHashMap(MillGeneralAngle, R.string.Mill_General_Angle_Description);
        putHashMap(MillPathLength, R.string.Mill_Path_Length_Description);
        putHashMap(MillToolLength, R.string.Mill_Tool_Length_Description);
        putHashMap(MillAttackAngle, R.string.Mill_Front_Angle_Description);
        putHashMap(MillAverageChipWidth, R.string.Mill_Average_Chips_Width_Description);
    }

    private String getStringValue(int id) {
        return mContext.getResources().getString(id);
    }

    private void putHashMap (FieldType fieldType, int id) {
        mGeneralPreset.put((fieldType), getStringValue(id));
    }

    String getStringDescription(FieldType fieldType) {
        return mGeneralPreset.get(fieldType);
    }
}