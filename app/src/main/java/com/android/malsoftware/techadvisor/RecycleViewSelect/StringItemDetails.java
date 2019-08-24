package com.android.malsoftware.techadvisor.RecycleViewSelect;


import androidx.recyclerview.selection.ItemDetailsLookup;

/**
 * An {@link ItemDetailsLookup.ItemDetails} that holds details about a {@link String}
 * item like its position and its value.
 */

public class StringItemDetails extends ItemDetailsLookup.ItemDetails<String> {

    private int position;
    private String item;

    StringItemDetails(int position, String item) {
        this.position = position;
        this.item = item;
    }

    @Override
    public int getPosition() {
        return position;
    }


    @Override
    public String getSelectionKey() {
        return item;
    }
}