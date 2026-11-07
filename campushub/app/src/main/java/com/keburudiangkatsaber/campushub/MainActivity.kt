package com.keburudiangkatsaber.campushub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.keburudiangkatsaber.campushub.databinding.ActivityMainBinding
import com.keburudiangkatsaber.campushub.fragment.BookmarkFragment
import com.keburudiangkatsaber.campushub.fragment.EventFragment
import com.keburudiangkatsaber.campushub.fragment.FilterFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            tampilkanFragment(EventFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_event -> {
                    tampilkanFragment(EventFragment())
                    true
                }
                R.id.menu_filter -> {
                    tampilkanFragment(FilterFragment())
                    true
                }
                R.id.menu_bookmark -> {
                    tampilkanFragment(BookmarkFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun tampilkanFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}