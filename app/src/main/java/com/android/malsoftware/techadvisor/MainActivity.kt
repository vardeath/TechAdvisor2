package com.android.malsoftware.techadvisor

import androidx.fragment.app.Fragment

class MainActivity : SingleFragmentActivity() {

    override fun createFragment(): Fragment {
        return MainMenuFragment.newInstance()
    }
}