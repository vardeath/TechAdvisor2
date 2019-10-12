package com.android.malsoftware.techadvisor.RecycleViewSelect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import com.android.malsoftware.techadvisor.BaseItemViewModel
import com.android.malsoftware.techadvisor.DescriptionsPresets
import com.android.malsoftware.techadvisor.MillDetailValues
import com.android.malsoftware.techadvisor.R
import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding

class AdapterSelector(private val mDescriptionsPresets: DescriptionsPresets,
                      private val Array: MillDetailValues) :
        RecyclerView.Adapter<AdapterSelector.SelectorHolder>() {

    private val TAG = "AdapterSelector"
    private lateinit var mTracker: SelectionTracker<String>
    private lateinit var mStrings: List<String>

    fun setTracker(tracker: SelectionTracker<String>) {
        mTracker = tracker
    }

    fun setStringKeys(Keys: List<String>) {
        mStrings = Keys
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorHolder {
        val inflater = LayoutInflater.from(parent.context)
        val millItemDefaultBinding: MillItemDefaultBinding
                = DataBindingUtil.inflate(inflater, R.layout.mill_item_default, parent,
                false)
        return SelectorHolder(millItemDefaultBinding)
    }

    override fun onBindViewHolder(holder: SelectorHolder, position: Int) {
        val item = mStrings[position]
        mTracker.isSelected(item).run { holder.bind(position, Array, this) }
    }

    override fun getItemCount(): Int {
        return mStrings.size
    }

    override fun getItemId(position: Int): Long {
        return mStrings[position].hashCode().toLong()
    }

    open inner class SelectorHolder(millItemDefaultBinding: MillItemDefaultBinding) :
            RecyclerView.ViewHolder((millItemDefaultBinding as ViewDataBinding).root) {

        private var mMillItemDefaultBinding: MillItemDefaultBinding = millItemDefaultBinding
        private val mBaseItemViewModel = BaseItemViewModel()

        val itemDetails: StringItemDetails?
            get() = StringItemDetails(adapterPosition, mStrings[adapterPosition])

        init {
            mMillItemDefaultBinding.baseModel = mBaseItemViewModel
        }

        fun bind(position: Int, millDetailValues: MillDetailValues, activated: Boolean) {
            mBaseItemViewModel.apply {
                setText(millDetailValues.getStringFieldValue(position))
                mDescriptionsPresets.
                        getStringDescription(millDetailValues.
                                getFieldType(position))?.let { setDescription(it)}
            }
            itemView.isActivated = activated
        }
    }
}