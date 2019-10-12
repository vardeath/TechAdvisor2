package com.android.malsoftware.techadvisor

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class SingleFragmentActivity : AppCompatActivity() {

    private val layoutResId: Int
        get() = R.layout.activity_main

    protected abstract fun createFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragmentContainer)

        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit()
        }
    }
}