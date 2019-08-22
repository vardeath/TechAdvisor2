package com.android.malsoftware.techadvisor;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.RecyclerView;
import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;
import androidx.recyclerview.selection.ItemDetailsLookup;

import java.lang.reflect.Array;
import java.util.Collections;

public class AdapterSelector extends RecycleViewScroll.Adapter<AdapterSelector.SelectorHolder>
        implements View.OnClickListener {

    private final String TAG = "AdapterSelector";
    SelectionTracker<Long> tracker;
    private DescriptionsPresets mDescriptionsPresets;
    private RecycleViewScroll mRecycleViewScroll = null;
    private MillDetailValues Array;
    private ItemDetailsLookup.ItemDetails mSelected = null;
    private int mLastPos = 0;


    ItemDetailsLookup.ItemDetails getSelected() {
        return mSelected;
    }

    AdapterSelector(DescriptionsPresets descriptionsPresets, MillDetailValues array){
        mDescriptionsPresets = descriptionsPresets;
        this.Array = array;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public SelectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MillItemDefaultBinding millItemDefaultBinding = DataBindingUtil.
                        inflate(inflater, R.layout.mill_item_default, parent, false);
        millItemDefaultBinding.getRoot().setOnClickListener(this);
        return new SelectorHolder(millItemDefaultBinding);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull SelectorHolder holder, int position) {
        Long x = (long) position;
        if (tracker != null) holder.bind(position, Array, tracker.isSelected(x));
    }

    @Override
    public int getItemCount() {
        return Array.getPairsArray().size();
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public void onClick(View view) {
        int pos = mLastPos;
        pos = mRecycleViewScroll.getChildAdapterPosition(view);
        tracker.select((long) pos);
        mLastPos = pos;
        Log.d(TAG, "Clicked");
    }

    class SelectorHolder extends RecyclerView.ViewHolder{

        private MillItemDefaultBinding mMillItemDefaultBinding;
        private BaseItemViewModel mBaseItemViewModel = new BaseItemViewModel();

        SelectorHolder(MillItemDefaultBinding millItemDefaultBinding) {
            super(millItemDefaultBinding.getRoot());
            mMillItemDefaultBinding = millItemDefaultBinding;
            mMillItemDefaultBinding.setBaseModel(mBaseItemViewModel);
        }

        public SelectorHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(int position, MillDetailValues val, boolean activated) {
            if (mMillItemDefaultBinding != null) {

                mBaseItemViewModel.setText(val.getStringFieldValue(position));
                itemView.setActivated(activated);
                mBaseItemViewModel.setDescription(mDescriptionsPresets.
                                getStringDescription(val.
                                        getPairsArray().
                                        get(position).
                                        getFieldType()));
            }
        }

        ItemDetailsLookup.ItemDetails getItemDetails() {
            mSelected = new ItemDetailsLookup.ItemDetails() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public Object getSelectionKey() {
                    return getItemId();
                }
            };
            return mSelected;
        }
    }
}