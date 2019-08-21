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
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainMenuFragment extends Fragment {

	private Context mContext = null;
	private View rootView = null;
	private MillDetailValues mMillDetailValues = null;
	private DescriptionsPresets mDescriptionsPresets = null;
	private RecycleViewScroll mRecycleViewScroll = null;

	private SelectionTracker<Long> tracker = null;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getContext();
		mMillDetailValues = MillDetailValues.newInstance(mContext);
		mDescriptionsPresets = DescriptionsPresets.newInstance(mContext);
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
		Button btn2 = rootView.findViewById(R.id.button);
		defineViewSize(mRecycleViewScroll);
		updateUi();

		btn.setOnClickListener(v -> mRecycleViewScroll.decrementSelectedPosition());
		btn2.setOnClickListener(v -> mRecycleViewScroll.incrementSelectedPosition());

		return rootView;
	}

	private void updateUi() {
		AdapterSelector adapterSelector = new AdapterSelector(mDescriptionsPresets, mMillDetailValues);
		mRecycleViewScroll.setAdapter(adapterSelector);
		RecyclerView.ItemAnimator animator = mRecycleViewScroll.getItemAnimator();
		if (animator != null) animator.setChangeDuration(0);
		mRecycleViewScroll.setVerticalScrollBarEnabled(true);
		mRecycleViewScroll.setScrollbarFadingEnabled(true);

		tracker = new SelectionTracker.Builder<Long>(
				"mySelection",
				mRecycleViewScroll,
				new StableIdKeyProvider(mRecycleViewScroll),
                new MyItemDetailsLookup(mRecycleViewScroll),
				StorageStrategy.createLongStorage()
        		).withSelectionPredicate(
				SelectionPredicates.createSelectAnything()
		).build();

		adapterSelector.tracker = tracker;
	}

	private void defineViewSize(final View view) {
		ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {
					LinearLayout linearLayout = rootView.findViewById(R.id.framelay1);
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					final int viewHeight = view.getHeight();
					final int itemHeight = (int) (linearLayout.getMeasuredHeight() +
							getResources().getDimension(R.dimen.offcet));
					final int itemQuantity = (int) Math.floor((double) viewHeight
							/ (double) itemHeight) - 1;
					mRecycleViewScroll.initRanges(itemQuantity, mMillDetailValues.getPairsArray().size());
				}
			});
		}
	}
}