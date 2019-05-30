package com.android.malsoftware.techadvisor;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.databinding.MillItemDefaultBinding;

import java.util.Objects;

public class MainMenuFragment extends Fragment {

	//private List<String> mItemsArray = new ArrayList<>();
	private MainMenuAdaptor mainMenuAdaptor;
	private View rootView;
	private MillDetailValues mMillDetailValues;
	private DescriptionsPresets mDescriptionsPresets;

	private AutoScrolledRecycleView mAutoScrolledRecycleView;


	private static final int mViewTypeDefault = 0;
	private static final int mViewTypSelected = 1;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMillDetailValues = MillDetailValues.newInstance(getContext());
		mDescriptionsPresets = DescriptionsPresets.newInstance(getContext());
		mMillDetailValues.getPairsArray().size();
		/*for (int i = 0; i < 10; ++i) {
			mItemsArray.add("item" + i);
		}*/
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		mAutoScrolledRecycleView = (AutoScrolledRecycleView) rootView.findViewById(R.id.main_resycleview);
		RecyclerView.LayoutManager mLayManager = new LinearLayoutManager(getActivity());
		mAutoScrolledRecycleView.setLayoutManager(mLayManager);
		Button btn = rootView.findViewById(R.id.button_up);
		Button btn2 = rootView.findViewById(R.id.button);
		getViewSize(mAutoScrolledRecycleView);
		updateUi();

		btn.setOnClickListener(v -> mAutoScrolledRecycleView.decrementSelectedPosition());
		btn2.setOnClickListener(v -> mAutoScrolledRecycleView.incrementSelectedPosition());

		return rootView;
	}

	private class MainMenuHolder extends RecyclerView.ViewHolder{

		private MillItemDefaultBinding mMillItemDefaultBinding;
		private int mDefaultColor;
		private int mSelectColor;

		MainMenuHolder(MillItemDefaultBinding millItemDefaultBinding) {
			super(millItemDefaultBinding.getRoot());
			mMillItemDefaultBinding = millItemDefaultBinding;
			mMillItemDefaultBinding.setBaseModel(new BaseItemViewModel());
		}

		void bind(int position, MillDetailValues val, int itemViewType) {
			mMillItemDefaultBinding.getBaseModel().setText(String.valueOf(val.getPairsArray().get(position).getValue()));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				mDefaultColor = getResources().getColor(R.color.colorItem, Objects.requireNonNull(getActivity()).getTheme());
				mSelectColor = getResources().getColor(R.color.colorItemSelect, Objects.requireNonNull(getActivity()).getTheme());
			}
			if (itemViewType == mViewTypeDefault) {
				mMillItemDefaultBinding.getBaseModel().setBackground(mDefaultColor);
			} else {
				mMillItemDefaultBinding.getBaseModel().setBackground(mSelectColor);
			}
			mMillItemDefaultBinding.getBaseModel().setPosition(position);
			mMillItemDefaultBinding.getBaseModel().setDescription(mDescriptionsPresets.getStringDescription(val.getPairsArray().get(position).getFieldType()));
			mMillItemDefaultBinding.getBaseModel().setAutoScrolledRecycleView(mAutoScrolledRecycleView);
		}
	}

	private class MainMenuAdaptor extends AutoScrolledRecycleView.Adapter<MainMenuHolder> {

		private MillDetailValues Array;
		private MillItemDefaultBinding mMillItemDefaultBinding;
		private AutoScrolledRecycleView mAutoScrolledRecycleView;

		MainMenuAdaptor(MillDetailValues Array) {
			this.Array = Array;
		}

		@Override
		public int getItemViewType(int position) {
			super.getItemViewType(position);
			if (position == mAutoScrolledRecycleView.getCurrentSelectedPosition()) {
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
			holder.bind(position, mMillDetailValues, getItemViewType(position));
		}

		@Override
		public int getItemCount() {
			return mMillDetailValues.getPairsArray().size();
		}

		@Override
		public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
			super.onAttachedToRecyclerView(recyclerView);
			mAutoScrolledRecycleView = (AutoScrolledRecycleView) recyclerView;
		}
	}

	private void updateUi() {
		mainMenuAdaptor = new MainMenuAdaptor(mMillDetailValues);
		mAutoScrolledRecycleView.setAdapter(mainMenuAdaptor);
		Objects.requireNonNull(mAutoScrolledRecycleView.getItemAnimator()).setChangeDuration(0);
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
					final int itemQuantity = (int) Math.floor((double) viewHeight / (double) itemHeight) - 1;

					mAutoScrolledRecycleView.initRanges(itemQuantity, mMillDetailValues.getPairsArray().size());

					Log.d("mar2", "itemQuantity = " + itemQuantity);
					Log.d("mar2", "itemHeight = " + viewHeight);
					Log.d("mar2", "viewHeight = " + itemHeight);
				}
			});
		}
	}
}