package com.vango.tictactoe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vango.tictactoe.ui.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        if(currentFragment == "com.vango.tictactoe.ui.createLobby.CreateLobby" || currentFragment == "com.vango.tictactoe.ui.onlineFragment.Online_fragment"){
            fragmentManager.beginTransaction().replace(R.id.container, fragment!!)
                .commit()
        }else{
            fragmentManager.beginTransaction().replace(R.id.container, fragment!!).addToBackStack(
                fragmentClass.name
            )
                .commit()
        }

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
        val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.container, fragment!!)
                .commit()

    }

    private var i = 0
    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if ((count == 0 && currentFragment !="com.vango.tictactoe.ui.singleGame.SingleGame") || currentFragment == "com.vango.tictactoe.ui.main.MainFragment") {
            finish()
        } else {
            if (currentFragment == "com.vango.tictactoe.ui.singleGame.SingleGame") {
                i++
                if (i < 2) {
                    Toast.makeText(this, "Nacisnij jeszcze raz aby wyjsc", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    i = 0
                    currentFragment = ""
                    replaceFragment(MainFragment::class.java)
                }
            } else {
                currentFragment = ""
                supportFragmentManager.popBackStack()
            }
        }
    }
}