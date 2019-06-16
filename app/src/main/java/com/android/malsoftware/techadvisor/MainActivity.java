package com.android.malsoftware.techadvisor;

import androidx.fragment.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MainMenuFragment.newInstance();
	}
}