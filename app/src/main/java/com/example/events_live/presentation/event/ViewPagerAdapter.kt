package com.example.events_live.presentation.event

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, var fragsList:List<Fragment>): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return fragsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragsList[position]
    }
}