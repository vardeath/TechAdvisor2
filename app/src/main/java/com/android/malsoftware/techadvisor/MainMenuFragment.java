package com.android.malsoftware.techadvisor;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.selection.*;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.android.malsoftware.techadvisor.RecycleViewSelect.AdapterSelector;
import com.android.malsoftware.techadvisor.RecycleViewSelect.MyItemDetailsLookup;
import com.android.malsoftware.techadvisor.RecycleViewSelect.MySelectionPredicate;
import com.android.malsoftware.techadvisor.RecycleViewSelect.RecycleViewScroll;
import com.android.malsoftware.techadvisor.RecycleViewSelect.StringItemKeyProvider;
import com.android.malsoftware.techadvisor.RecycleViewSelect.SwipeController;
import com.android.malsoftware.techadvisor.RecycleViewSelect.SwipeControllerActions;

import java.util.List;

public class MainMenuFragment extends Fragment {

	private final String TAG = "MainMenuFragment";
	private MillDetailValues mMillDetailValues = null;
	private DescriptionsPresets mDescriptionsPresets = null;
	private RecycleViewScroll mRecycleViewScroll = null;
	private List<String> mStringKeys = null;
	private String mSelectedKey = null;
	private SelectionTracker<String> mSelectionTracker = null;
	private SwipeController swipeController = null;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMillDetailValues = MillDetailValues.newInstance(getActivity());
		mDescriptionsPresets = DescriptionsPresets.newInstance(getActivity());
		mStringKeys = mMillDetailValues.getStringKeys();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		mRecycleViewScroll = rootView.findViewById(R.id.main_resycleview);
		RecyclerView.LayoutManager mLayManager = new LinearLayoutManager(getContext()) {
			@Override
			public void smoothScrollToPosition(RecyclerView recyclerView,
											   RecyclerView.State state, int position) {
				super.smoothScrollToPosition(recyclerView, state, position);
				Activity activity = getActivity();
				if (activity != null) {
					LinearSmoothScroller smoothScroller = new LinearSmoothScroller(activity) {

						private static final float SPEED = 25f;// Change this value (default=25f)

						@Override
						protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
							return SPEED / displayMetrics.densityDpi;
						}

					};
					smoothScroller.setTargetPosition(position);
					startSmoothScroll(smoothScroller);
				}
			}
		};

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

		mSelectionTracker = new SelectionTracker.Builder<>(
				"mySelection",
				mRecycleViewScroll,
				new StringItemKeyProvider(1, mStringKeys),
                new MyItemDetailsLookup(mRecycleViewScroll),
				StorageStrategy.createStringStorage()
        		).withSelectionPredicate(
				new MySelectionPredicate()).build();

		mRecycleViewScroll.setTracker(mSelectionTracker);

		mSelectionTracker.addObserver(new SelectionObserver<String>() {
			@Override
			public void onItemStateChanged(@NonNull String key, boolean selected) {
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

		mSelectionTracker.select(mStringKeys.get(0)); //First selected element

		swipeController = new SwipeController(new SwipeControllerActions());
		ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
		itemTouchhelper.attachToRecyclerView(mRecycleViewScroll);

		mRecycleViewScroll.addItemDecoration(new RecyclerView.ItemDecoration() {
			@Override
			public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
							   @NonNull RecyclerView.State state) {
				swipeController.onDraw(c);
			}
		});
	}

	private int findPositionByKey(String key) {
		for (int i = 0; i < mStringKeys.size(); ++i) {
			if (key.equals(mStringKeys.get(i))) return i;
		}
		return 0;
	}
}