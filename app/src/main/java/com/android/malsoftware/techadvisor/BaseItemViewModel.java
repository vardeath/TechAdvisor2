package com.android.malsoftware.techadvisor;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


public class BaseItemViewModel extends BaseObservable {

	private ObservableField<String> mText = new ObservableField<>();
	private ObservableInt mBackground = new ObservableInt();

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
}