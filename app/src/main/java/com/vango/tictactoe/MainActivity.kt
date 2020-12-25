package com.vango.tictactoe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vango.tictactoe.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private var currentFragment: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    fun replaceFragment(fragmentClass: Class<*>) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        currentFragment = fragmentClass.name
        Log.i("crere", currentFragment)
        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!).addToBackStack(
            fragmentClass.name
        )
            .commit()
    }

    fun replaceFragment(fragmentClass: Class<*>, type: String) {
        var fragment: Fragment? = null
        try {
            fragment = fragmentClass.newInstance() as Fragment
            val bundle = Bundle()
            bundle.putString("type", type)
            fragment.arguments = bundle
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Insert the fragment by replacing any existing fragment
        currentFragment = fragmentClass.name
        Log.i("crere", currentFragment)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container, fragment!!)
            .addToBackStack(fragmentClass.name)
            .commit()
    }

    private var i = 0
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            if (currentFragment == "com.vango.tictactoe.ui.singleGame.SingleGame") {
                i++
                if (i < 2) {
                    Toast.makeText(this, "Nacisnij jeszcze raz aby wyjsc", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    i = 0
                    currentFragment = ""
                    supportFragmentManager.popBackStack()
                }
            } else {
                currentFragment = ""
                supportFragmentManager.popBackStack()
            }
        }
    }
}