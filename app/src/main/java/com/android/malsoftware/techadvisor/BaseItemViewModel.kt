package com.android.malsoftware.techadvisor

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField

class BaseItemViewModel : BaseObservable() {

    val text = ObservableField<String>()

    val description = ObservableField<String>()

    @Bindable
    fun setText(text: String) {
        this.text.set(text)
    }

    @Bindable
    fun setDescription(value: String) {
        description.set(value)
    }
}