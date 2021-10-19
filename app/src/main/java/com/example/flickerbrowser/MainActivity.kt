package com.example.flickerbrowser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction





class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView : BottomNavigationView


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_search

        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().add(R.id.fragment_content, SearchFragment(), "search").commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    if (fragmentManager.findFragmentByTag("search") != null) {//show search
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("search")!!).commit()
                    } else {
                        fragmentManager.beginTransaction().add(R.id.fragment_content, SearchFragment(), "search").commit()
                    }
                    if (fragmentManager.findFragmentByTag("favorite") != null) { //hide favorite
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("favorite")!!).commit()
                    }
                }
                R.id.nav_favorite -> {
                    if (fragmentManager.findFragmentByTag("favorite") != null) {//show search
                        fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("favorite")!!).commit()
                    } else {
                        fragmentManager.beginTransaction().add(R.id.fragment_content, FavoriteFragment(), "favorite").commit()
                    }
                    if (fragmentManager.findFragmentByTag("search") != null) { //hide favorite
                        fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("search")!!).commit()
                    }
                }
            }
            true
        }
    }

}

