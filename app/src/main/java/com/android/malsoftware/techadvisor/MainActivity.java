package com.android.malsoftware.techadvisor;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MainMenuFragment.newInstance();
	}
}