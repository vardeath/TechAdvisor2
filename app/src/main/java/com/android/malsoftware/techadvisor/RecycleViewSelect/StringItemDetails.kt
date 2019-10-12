package com.android.malsoftware.techadvisor.RecycleViewSelect


import androidx.recyclerview.selection.ItemDetailsLookup

/**
 * An [ItemDetailsLookup.ItemDetails] that holds details about a [String]
 * item like its position and its value.
 */

class StringItemDetails internal constructor(private val position: Int, private val item: String) :
        ItemDetailsLookup.ItemDetails<String>() {

    override fun getPosition(): Int {
        return position
    }


    override fun getSelectionKey(): String? {
        return item
    }
}