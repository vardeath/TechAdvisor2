package com.android.malsoftware.techadvisor;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

import java.util.Objects;

public class MainActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		return MainMenuFragment.newInstance();
	}
}