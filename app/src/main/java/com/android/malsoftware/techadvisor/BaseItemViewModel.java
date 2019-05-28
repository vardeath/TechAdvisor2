package com.android.malsoftware.techadvisor;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


public class BaseItemViewModel extends BaseObservable {

	private ObservableField<String> mText = new ObservableField<>();
	private ObservableInt mBackground = new ObservableInt();
	private int position;
	private AutoScrolledRecycleView mAutoScrolledRecycleView;

	public ObservableField<String> getText() {
		return mText;
	}

	@Bindable
	void setText(String text) {
		mText.set(text);
	}

	public int getBackground() {
		return mBackground.get();
	}

	@Bindable
	void setBackground(int background) {
		mBackground.set(background);
	}

	public void onClick() {
		Log.d("mar2","clicked " + position);
		mAutoScrolledRecycleView.setSelectedPosition(position);
	}

	@Bindable
	void setPosition(int position) {
		this.position = position;
	}

	void setAutoScrolledRecycleView(AutoScrolledRecycleView autoScrolledRecycleView) {
		mAutoScrolledRecycleView = autoScrolledRecycleView;
	}
}