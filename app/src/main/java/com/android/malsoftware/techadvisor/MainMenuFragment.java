package com.android.malsoftware.techadvisor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.malsoftware.techadvisor.databinding.MillItemSimpleBinding;

public class MainMenuFragment extends Fragment {

	private int pos = 0;
	private int mCount = 0;
	private MainMenuAdaptor mMainMenuAdaptor;

	static MainMenuFragment newInstance() {
		return new MainMenuFragment();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.menu_main_fragment, container, false);

		final RecyclerView recyclerView = rootView.findViewById(R.id.main_resycleview);
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
		mMainMenuAdaptor = new MainMenuAdaptor();
		recyclerView.setAdapter(mMainMenuAdaptor);


		Button btn = rootView.findViewById(R.id.button);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d("resview", "clicked_button");
				recyclerView.setNestedScrollingEnabled(true);
				recyclerView.scrollToPosition(pos);
				recyclerView.performClick();
				if (pos < 20) ++pos;
				else pos = 0;
			}
		});

		return rootView;
	}

	private class MainMenuHolder extends RecyclerView.ViewHolder {

		private MillItemSimpleBinding mMillItemSimpleBinding;

		MainMenuHolder(MillItemSimpleBinding illItemSimpleBinding) {
			super(illItemSimpleBinding.getRoot());
			mMillItemSimpleBinding = illItemSimpleBinding;
			mMillItemSimpleBinding.setBaseModel(new BaseItemViewModel());
		}

		@SuppressLint("SetTextI18n")
		void bind(int position) {
			mMillItemSimpleBinding.getBaseModel().setText("Item: " + mCount);
			if (mCount < 20) ++mCount;
			//else mCount = 0;
		}
	}

	private class MainMenuAdaptor extends RecyclerView.Adapter<MainMenuHolder> {

		@NonNull
		@Override
		public MainMenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

			LayoutInflater inflater = LayoutInflater.from(getActivity());
			MillItemSimpleBinding mMillItemSimpleBinding = DataBindingUtil.inflate(inflater,R.layout.mill_item_simple, parent, false);
			return new MainMenuHolder(mMillItemSimpleBinding);
		}

		@Override
		public void onBindViewHolder(@NonNull MainMenuHolder holder, int position) {
			holder.bind(position);
		}

		@Override
		public int getItemCount() {
			return 20;
		}
	}
}