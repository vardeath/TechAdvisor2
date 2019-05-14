package com.android.malsoftware.techadvisor;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainMenuFragment extends Fragment {

	private List<String> mItemsArray = new ArrayList<>();
	private RecyclerView mRecyclerView;
	private MainMenuAdaptor mainMenuAdaptor;
	private View rootView;
	private int mItemQuantity;
	private int mViewHeight;
	private int mItemHeight;
	private boolean isGlobalLayoutInflated = false;
	private int mCurrentSelectedPosition = 0;

	private static final int mViewTypeDefault = 0;
	private static final int mViewTypSelected = 1;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for (int i = 0; i < 17; ++i) {
			mItemsArray.add("item" + i);
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		mRecyclerView = rootView.findViewById(R.id.main_resycleview);
		RecyclerView.LayoutManager mLayManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayManager);
		Button btn = rootView.findViewById(R.id.button);
		getViewSize(mRecyclerView);
		updateUi();

		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isGlobalLayoutInflated) {
					int count = mRecyclerView.getScrollState();
					//mRecyclerView.smoothScrollToPosition(mItemQuantity + (mItemQuantity - 2));

					Log.d("mar2", "itemQuantity = " + mItemQuantity);
					Log.d("mar2", "itemHeight = " + mItemHeight);
					Log.d("mar2", "viewHeight = " + mViewHeight);
					Log.d("mar2", "count = " + count);
				}
			}
		});

		return rootView;
	}

	private class MainMenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private MillItemDefaultBinding mMillItemDefaultBinding;
		private int mDefaultColor;
		private int mSelectColor;

		MainMenuHolder(MillItemDefaultBinding millItemDefaultBinding) {
			super(millItemDefaultBinding.getRoot());
			mMillItemDefaultBinding = millItemDefaultBinding;
			mMillItemDefaultBinding.setBaseModel(new BaseItemViewModel());
			itemView.setOnClickListener(this);
		}

		void bind(int position, String val, int itemViewType) {
			mMillItemDefaultBinding.getBaseModel().setText(val);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				mDefaultColor = getResources().getColor(R.color.colorPrimary, Objects.requireNonNull(getActivity()).getTheme());
				mSelectColor = getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme());
			} else {
				mDefaultColor = getResources().getColor(R.color.colorPrimary);
				mSelectColor = getResources().getColor(R.color.colorAccent);
			}

			if (itemViewType == mViewTypeDefault) {
				mMillItemDefaultBinding.getBaseModel().setBackground(mDefaultColor);
			} else {
				mMillItemDefaultBinding.getBaseModel().setBackground(mSelectColor);
			}
		}

		@Override
		public void onClick(View v) {
			int oldPos = mCurrentSelectedPosition;
			mCurrentSelectedPosition = getLayoutPosition();
			mainMenuAdaptor.notifyItemChanged(mCurrentSelectedPosition);
			mainMenuAdaptor.notifyItemChanged(oldPos);
			Log.d("mar2", "mCurrentSelectedPosition" + mCurrentSelectedPosition);
			Log.d("mar2", "oldPos" + oldPos);
		}
	}

	private class MainMenuAdaptor extends RecyclerView.Adapter<MainMenuHolder> {

		private List<String> Array;
		private MillItemDefaultBinding mMillItemDefaultBinding;

		MainMenuAdaptor(List<String> Array) {
			this.Array = Array;
		}

		@Override
		public int getItemViewType(int position) {
			super.getItemViewType(position);
			if (position == mCurrentSelectedPosition) {
				return mViewTypSelected;
			} else return mViewTypeDefault;
		}

		@NonNull
		@Override
		public MainMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

			LayoutInflater inflater = LayoutInflater.from(getActivity());
			mMillItemDefaultBinding = DataBindingUtil.inflate(inflater, R.layout.mill_item_default, parent, false);
			return new MainMenuHolder(mMillItemDefaultBinding);
		}

		@Override
		public void onBindViewHolder(@NonNull MainMenuHolder holder, int position) {
			holder.bind(position, Array.get(position), getItemViewType(position));
		}

		@Override
		public int getItemCount() {
			return mItemsArray.size();
		}
	}

	private void updateUi() {
		mainMenuAdaptor = new MainMenuAdaptor(mItemsArray);
		mRecyclerView.setAdapter(mainMenuAdaptor);
		//Objects.requireNonNull(mRecyclerView.getItemAnimator()).setChangeDuration(0);
	}

	private void getViewSize(final View view) {
		ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
		if (viewTreeObserver.isAlive()) {
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

				@Override
				public void onGlobalLayout() {

					LinearLayout linearLayout = rootView.findViewById(R.id.framelay1);
					view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

					final int viewHeight = view.getHeight();
					final int itemHeight = (int) (linearLayout.getMeasuredHeight() + getResources().getDimension(R.dimen.offcet));
					mItemQuantity = (int) Math.floor((double) viewHeight / (double) itemHeight);
					mViewHeight = viewHeight;
					mItemHeight = itemHeight;

					isGlobalLayoutInflated = true;
				}
			});
		}
	}
}