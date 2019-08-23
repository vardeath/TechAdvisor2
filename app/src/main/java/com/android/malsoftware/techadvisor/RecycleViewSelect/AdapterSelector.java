package com.android.malsoftware.techadvisor.RecycleViewSelect;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.BaseItemViewModel;
import com.android.malsoftware.techadvisor.DescriptionsPresets;
import com.android.malsoftware.techadvisor.MillDetailValues;
import com.android.malsoftware.techadvisor.R;
import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;

import java.util.List;

public class AdapterSelector extends RecycleViewScroll.Adapter<AdapterSelector.SelectorHolder> {

    private final String TAG = "AdapterSelector";
    private SelectionTracker<String> mTracker;
    private DescriptionsPresets mDescriptionsPresets;
    private MillDetailValues Array;
    private List<String> mStrings;

    public AdapterSelector(DescriptionsPresets descriptionsPresets, MillDetailValues array){
        mDescriptionsPresets = descriptionsPresets;
        this.Array = array;
    }

    public void setTracker(SelectionTracker<String> tracker) {
        mTracker = tracker;
    }

    public void setStringKeys(List<String> Keys) {
        mStrings = Keys;
    }

    @NonNull
    @Override
    public SelectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MillItemDefaultBinding millItemDefaultBinding = DataBindingUtil.
                        inflate(inflater, R.layout.mill_item_default, parent, false);
        return new SelectorHolder(millItemDefaultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorHolder holder, int position) {
        String item = mStrings.get(position);
        if (mTracker != null) holder.bind(position, Array, mTracker.isSelected(item));
    }

    @Override
    public int getItemCount() {
        return mStrings == null ? 0 : mStrings.size();
    }

    @Override
    public long getItemId(int position) {
        return mStrings.get(position).hashCode();
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
                        getStringDescription(val.getFieldType(position)));
            }
        }

        StringItemDetails getItemDetails() {
            return new StringItemDetails(getAdapterPosition(), mStrings.get(getAdapterPosition()));
        }
    }
}