package com.android.malsoftware.techadvisor;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


public class BaseItemViewModel extends BaseObservable {

	private ObservableField<String> mText = new ObservableField<>();
	private ObservableField<String> mDescription = new ObservableField<>();
	private ObservableInt mBackground = new ObservableInt();
	private int position;
	private RecycleViewScroll mRecycleViewScroll;

	public ObservableField<String> getDescription() {
		return mDescription;
	}

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
		mRecycleViewScroll.setSelectedPosition(position, true);
	}

	@Bindable
	void setPosition(int position) {
		this.position = position;
	}

	void setRecycleViewScroll(RecycleViewScroll recycleViewScroll) {
		mRecycleViewScroll = recycleViewScroll;
	}

	@Bindable
    void setDescription(String value) {
		mDescription.set(value);
	}
}