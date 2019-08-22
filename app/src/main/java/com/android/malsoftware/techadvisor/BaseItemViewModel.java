package com.android.malsoftware.techadvisor;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


public class BaseItemViewModel extends BaseObservable {

	private ObservableField<String> mText = new ObservableField<>();
	private ObservableField<String> mDescription = new ObservableField<>();
	//private int position;

	public ObservableField<String> getDescription() {
		return mDescription;
	}

	public ObservableField<String> getText() {
		return mText;
	}

	@Bindable
    public void setText(String text) {
		mText.set(text);
	}

	/*@Bindable
	void setPosition(int position) {
		this.position = position;
	}*/

	@Bindable
	public void setDescription(String value) {
		mDescription.set(value);
	}
}