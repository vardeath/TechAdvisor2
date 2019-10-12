package com.android.malsoftware.techadvisor

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.malsoftware.techadvisor.RecycleViewSelect.AdapterSelector
import com.android.malsoftware.techadvisor.RecycleViewSelect.MyItemDetailsLookup
import com.android.malsoftware.techadvisor.RecycleViewSelect.MySelectionPredicate
import com.android.malsoftware.techadvisor.RecycleViewSelect.RecycleViewScroll
import com.android.malsoftware.techadvisor.RecycleViewSelect.StringItemKeyProvider
import com.android.malsoftware.techadvisor.RecycleViewSelect.SwipeHelper

class MainMenuFragment : Fragment() {

    private val TAG = "MainMenuFragment"
    private lateinit var mMillDetailValues: MillDetailValues
    private lateinit var mDescriptionsPresets: DescriptionsPresets
    private lateinit var mRecycleViewScroll: RecycleViewScroll
    private lateinit var mStringKeys: List<String>
    private lateinit var mSelectedKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMillDetailValues = MillDetailValues.newInstance(context)
        mDescriptionsPresets = DescriptionsPresets.newInstance(context)
        mStringKeys = mMillDetailValues.stringKeys
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.menu_main_fragment, container, false)

        mRecycleViewScroll = rootView.findViewById(R.id.main_resycleview)
        val mLayManager = object : LinearLayoutManager(context) {}

        mRecycleViewScroll.layoutManager = mLayManager
        val btn = rootView.findViewById<Button>(R.id.button_up)
        val btn2 = rootView.findViewById<Button>(R.id.button_down)
        initializeRecycleView()

        btn.setOnClickListener {mRecycleViewScroll.decrementSelectedPosition() }
        btn2.setOnClickListener {mRecycleViewScroll.incrementSelectedPosition() }

        return rootView
    }

    private fun initializeRecycleView() {

        val adapterSelector = AdapterSelector(mDescriptionsPresets, mMillDetailValues)

        adapterSelector.setStringKeys(mStringKeys)

        val animator = mRecycleViewScroll.itemAnimator

        animator?.changeDuration = 0

        mRecycleViewScroll.also {
            it.adapter = adapterSelector
            it.isVerticalScrollBarEnabled = true
            it.isScrollbarFadingEnabled = true
            it.initStringKeys(mStringKeys)
        }

        val mSelectionTracker = SelectionTracker.Builder(
                "mySelection",
                mRecycleViewScroll,
                StringItemKeyProvider(1, mStringKeys),
                MyItemDetailsLookup(mRecycleViewScroll),
                StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
                MySelectionPredicate()).build()

        mSelectionTracker.addObserver(object : SelectionObserver<String>() {
            override fun onItemStateChanged(key: String, selected: Boolean) {
                super.onItemStateChanged(key, selected)
                if (selected) {
                    mSelectedKey = key
                    mRecycleViewScroll.setSelectedPosition(findPositionByKey(mSelectedKey))
                }
            }

            override fun onSelectionChanged() {
                super.onSelectionChanged()
                if (!mSelectionTracker.hasSelection()) mSelectionTracker.select(mSelectedKey)
            }
        })

        mRecycleViewScroll.run { setTracker(mSelectionTracker) }
        mSelectionTracker.select(mStringKeys[0])

        val swipeHelper = object : SwipeHelper(activity, mRecycleViewScroll) {
            override fun instantiateUnderlayButton(viewHolder: RecyclerView.ViewHolder,
                                                   underlayButtons: MutableList<UnderlayButton>) {
                underlayButtons.add(UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d(TAG, "clicked $pos")
                            }
                        }
                ))

                underlayButtons.add(UnderlayButton(
                        "Transfer",
                        0,
                        Color.parseColor("#FF9502"),
                        object : UnderlayButtonClickListener {
                            override fun onClick(pos: Int) {
                                Log.d(TAG, "clicked $pos")
                            }
                        }
                ))
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelper)
        itemTouchHelper.attachToRecyclerView(mRecycleViewScroll)
    }

    private fun findPositionByKey(key: String?): Int {
        for (i in mStringKeys.indices) {
            if (key == mStringKeys[i]) return i
        }
        return 0
    }

    companion object {
        internal fun newInstance(): MainMenuFragment {
            return MainMenuFragment()
        }
    }
}