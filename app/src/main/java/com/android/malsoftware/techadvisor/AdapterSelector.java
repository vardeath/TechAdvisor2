package com.android.malsoftware.techadvisor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.RecyclerView;
import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class AdapterSelector extends RecycleViewScroll.Adapter<AdapterSelector.SelectorHolder> {

    public SelectionTracker<Long> tracker;
    private DescriptionsPresets mDescriptionsPresets;
    private RecycleViewScroll mRecycleViewScroll = null;
    private int mSelectedPosition = 0;
    private MillDetailValues Array;
    private MillItemDefaultBinding mMillItemDefaultBinding;

    public AdapterSelector(DescriptionsPresets descriptionsPresets, MillDetailValues array){
        mDescriptionsPresets = descriptionsPresets;
        this.Array = array;
    }

    @NonNull
    @Override
    public SelectorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        mMillItemDefaultBinding = DataBindingUtil.inflate(inflater, R.layout.mill_item_default,
                parent, false);
        return new SelectorHolder(mMillItemDefaultBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectorHolder holder, int position) {
        holder.bind(position, Array);
    }

    @Override
    public int getItemCount() {
        return Array.getPairsArray().size();
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

        void bind(int position, MillDetailValues val) {
            if (mMillItemDefaultBinding != null) {
                mBaseItemViewModel.setText(val.getStringFieldValue(position));
                if (mSelectedPosition == position) itemView.setActivated(true);
                mBaseItemViewModel.setPosition(position);
                mBaseItemViewModel.setDescription(mDescriptionsPresets.
                                getStringDescription(val.
                                        getPairsArray().
                                        get(position).
                                        getFieldType()));
                if (mRecycleViewScroll != null) mBaseItemViewModel.setRecycleViewScroll(mRecycleViewScroll);
            }
        }

        ItemDetailsLookup.ItemDetails getItemDetails() {
            ItemDetailsLookup.ItemDetails object = new ItemDetailsLookup.ItemDetails() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Nullable
                @Override
                public Object getSelectionKey() {
                    return getItemId();
                }
            };
            return object;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleViewScroll = (RecycleViewScroll) recyclerView;
    }
}
