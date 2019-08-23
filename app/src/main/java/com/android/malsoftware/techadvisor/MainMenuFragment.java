package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.RecycleViewSelect.AdapterSelector;
import com.android.malsoftware.techadvisor.RecycleViewSelect.MyItemDetailsLookup;
import com.android.malsoftware.techadvisor.RecycleViewSelect.MySelectionPredicate;
import com.android.malsoftware.techadvisor.RecycleViewSelect.RecycleViewScroll;
import com.android.malsoftware.techadvisor.RecycleViewSelect.StringItemKeyProvider;

import java.util.List;

public class MainMenuFragment extends Fragment {

	private final String TAG = "MainMenuFragment";
	private Context mContext = null;
	private View rootView = null;
	private MillDetailValues mMillDetailValues = null;
	private DescriptionsPresets mDescriptionsPresets = null;
	private RecycleViewScroll mRecycleViewScroll = null;
	private List<String> mStringKeys = null;
	private Object mSelectedKey = null;

	private SelectionTracker mSelectionTracker = null;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getContext();
		mMillDetailValues = MillDetailValues.newInstance(mContext);
		mDescriptionsPresets = DescriptionsPresets.newInstance(mContext);
		mStringKeys = mMillDetailValues.getStringKeys();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		mRecycleViewScroll = rootView.findViewById(R.id.main_resycleview);
		RecyclerView.LayoutManager mLayManager = new LinearLayoutManager(getActivity());
		mRecycleViewScroll.setLayoutManager(mLayManager);
		Button btn = rootView.findViewById(R.id.button_up);
		Button btn2 = rootView.findViewById(R.id.button_down);
		updateUi();

		btn.setOnClickListener(v -> mRecycleViewScroll.decrementSelectedPosition());
		btn2.setOnClickListener(v -> mRecycleViewScroll.incrementSelectedPosition());

		return rootView;
	}

	private void updateUi() {
		AdapterSelector adapterSelector =
				new AdapterSelector(mDescriptionsPresets, mMillDetailValues);
		adapterSelector.setStringKeys(mStringKeys);
		mRecycleViewScroll.setAdapter(adapterSelector);
		RecyclerView.ItemAnimator animator = mRecycleViewScroll.getItemAnimator();
		if (animator != null) animator.setChangeDuration(0);
		mRecycleViewScroll.setVerticalScrollBarEnabled(true);
		mRecycleViewScroll.setScrollbarFadingEnabled(true);
		mRecycleViewScroll.initStringKeys(mStringKeys);

		mSelectionTracker = new SelectionTracker.Builder<String>(
				"mySelection",
				mRecycleViewScroll,
				new StringItemKeyProvider(1, mStringKeys),
                new MyItemDetailsLookup(mRecycleViewScroll),
				StorageStrategy.createStringStorage()
        		).withSelectionPredicate(
				new MySelectionPredicate()).build();

		adapterSelector.tracker = mSelectionTracker;
		mRecycleViewScroll.tracker = mSelectionTracker;

		mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
			@Override
			public void onItemStateChanged(@NonNull Object key, boolean selected) {
				super.onItemStateChanged(key, selected);
				if (selected)  {
					mSelectedKey = key;
					mRecycleViewScroll.setSelectedPosition(findPositionByKey(mSelectedKey));
				}
			}

			@Override
			public void onSelectionRefresh() {
				super.onSelectionRefresh();
			}

			@Override
			public void onSelectionChanged() {
				super.onSelectionChanged();
				if (!mSelectionTracker.hasSelection()) mSelectionTracker.select(mSelectedKey);
			}
		});


		mSelectionTracker.select(mStringKeys.get(0));
	}

	private int findPositionByKey(Object key) {
		String onj = (String) key;
		for (int i = 0; i < mStringKeys.size(); ++i) {
			if (onj == mStringKeys.get(i)) return i;
		}
		return Integer.parseInt(null);
	}
}