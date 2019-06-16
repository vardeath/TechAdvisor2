package com.android.malsoftware.techadvisor;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;
import java.util.Objects;


public class MainMenuFragment extends Fragment {

	private Context mContext = null;
	private View rootView = null;
	private MillDetailValues mMillDetailValues = null;
	private DescriptionsPresets mDescriptionsPresets = null;
	private RecycleViewScroll mRecycleViewScroll = null;
	private static final int mViewTypeDefault = 0;
	private static final int mViewTypSelected = 1;

	private int mDefaultColor;
	private int mSelectColor;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getContext();
		mMillDetailValues = MillDetailValues.newInstance(mContext);
		mDescriptionsPresets = DescriptionsPresets.newInstance(mContext);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			mDefaultColor = getResources().getColor(R.color.colorItem,
					(Objects.requireNonNull(getActivity())).getTheme());
			mSelectColor = getResources().getColor(R.color.colorItemSelect,
					Objects.requireNonNull(getActivity()).getTheme());
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		mRecycleViewScroll = rootView.findViewById(R.id.main_resycleview);
		RecyclerView.LayoutManager mLayManager = new LinearLayoutManager(getActivity()) {
			@Override
			public void smoothScrollToPosition(RecyclerView recyclerView,
			                                   RecyclerView.State state, int position) {
				LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {

					private static final float SPEED = 50f;// Change this value (default=25f)

					@Override
					protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
						return SPEED / displayMetrics.densityDpi;
					}

				};
				smoothScroller.setTargetPosition(position);
				startSmoothScroll(smoothScroller);
			}
		};
		mRecycleViewScroll.setLayoutManager(mLayManager);
		Button btn = rootView.findViewById(R.id.button_up);
		Button btn2 = rootView.findViewById(R.id.button);
		defineViewSize(mRecycleViewScroll);
		updateUi();

		btn.setOnClickListener(v -> mRecycleViewScroll.decrementSelectedPosition());
		btn2.setOnClickListener(v -> mRecycleViewScroll.incrementSelectedPosition());

		return rootView;
	}

	private class MainMenuHolder extends RecyclerView.ViewHolder{

		private MillItemDefaultBinding mMillItemDefaultBinding;

		MainMenuHolder(MillItemDefaultBinding millItemDefaultBinding) {
			super(millItemDefaultBinding.getRoot());
			mMillItemDefaultBinding = millItemDefaultBinding;
			mMillItemDefaultBinding.setBaseModel(new BaseItemViewModel());
		}

		void bind(int position, MillDetailValues val, int itemViewType) {
			if (mMillItemDefaultBinding != null) {
				mMillItemDefaultBinding.getBaseModel().setText(val.getStringFieldValue(position));
				if (itemViewType == mViewTypeDefault) {
					mMillItemDefaultBinding.getBaseModel().setBackground(mDefaultColor);
				} else {
					mMillItemDefaultBinding.getBaseModel().setBackground(mSelectColor);
				}
				mMillItemDefaultBinding.getBaseModel().setPosition(position);
				mMillItemDefaultBinding.getBaseModel().
						setDescription(mDescriptionsPresets.
								getStringDescription(val.
										getPairsArray().
										get(position).
										getFieldType()));
				mMillItemDefaultBinding.getBaseModel().setRecycleViewScroll(mRecycleViewScroll);
			}
		}
	}

	protected class MainMenuAdaptor extends RecycleViewScroll.Adapter<MainMenuHolder> {

		private MillDetailValues Array;
		private MillItemDefaultBinding mMillItemDefaultBinding;
		private RecycleViewScroll mRecycleViewScroll;

		MainMenuAdaptor(MillDetailValues Array) {
			this.Array = Array;
		}

		@Override
		public int getItemViewType(int position) {
			super.getItemViewType(position);
			if (position == mRecycleViewScroll.getCurrentSelectedPosition()) {
				return mViewTypSelected;
			} else return mViewTypeDefault;
		}

		@NonNull
		@Override
		public MainMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

			LayoutInflater inflater = LayoutInflater.from(getActivity());
			mMillItemDefaultBinding = DataBindingUtil.inflate(inflater, R.layout.mill_item_default,
					parent, false);
			return new MainMenuHolder(mMillItemDefaultBinding);
		}

		@Override
		public void onBindViewHolder(@NonNull MainMenuHolder holder, int position) {
			holder.bind(position, mMillDetailValues, getItemViewType(position));
		}

		@Override
		public int getItemCount() {
			return mMillDetailValues.getPairsArray().size();
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
			super.onAttachedToRecyclerView(recyclerView);
			mRecycleViewScroll = (RecycleViewScroll) recyclerView;
		}
	}

	private void updateUi() {
		MainMenuAdaptor mainMenuAdaptor = new MainMenuAdaptor(mMillDetailValues);
		mRecycleViewScroll.setAdapter(mainMenuAdaptor);
		RecyclerView.ItemAnimator animator = mRecycleViewScroll.getItemAnimator();
		if (animator != null) animator.setChangeDuration(0);
		mRecycleViewScroll.setVerticalScrollBarEnabled(true);
		mRecycleViewScroll.setScrollbarFadingEnabled(true);
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
					final int itemHeight = (int) (linearLayout.getMeasuredHeight() + getResources().getDimension(R.dimen.offcet));
					final int itemQuantity = (int) Math.floor((double) viewHeight / (double) itemHeight) - 1;
					mRecycleViewScroll.initRanges(itemQuantity, mMillDetailValues.getPairsArray().size());
					/*Log.d("mar2", "itemQuantity = " + itemQuantity);
					Log.d("mar2", "itemHeight = " + viewHeight);
					Log.d("mar2", "viewHeight = " + itemHeight);*/
				}
			});
		}
	}
}