package com.android.malsoftware.techadvisor.RecycleViewSelect

import androidx.recyclerview.selection.ItemKeyProvider

/**
 * A basic implementation of [ItemKeyProvider] for String items.
 */

class StringItemKeyProvider(scope: Int, private val items: List<String>)
    : ItemKeyProvider<String>(scope) {

    override fun getKey(position: Int): String? {
        return items[position]
    }

    override fun getPosition(key: String): Int {
        return items.indexOf(key)
    }
}