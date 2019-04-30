package com.android.malsoftware.techadvisor;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class BaseItemViewModel extends BaseObservable {


	public String getText() {
		return mText;
	}

	@Bindable
	public void setText(String text) {
		mText = text;
		notifyChange();
	}

	private String mText = "Text of Rabbit";
}
