@file:Suppress("DEPRECATION")

package com.example.dirverapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.dirverapp.ui.list.DeliveriesFragment
import com.example.dirverapp.ui.map.MapFragment

class DeliveriesPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            DeliveriesFragment()
        } else {
            MapFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0) "LIST" else "MAP"
    }
}
