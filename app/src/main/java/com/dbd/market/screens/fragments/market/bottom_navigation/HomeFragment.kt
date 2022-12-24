package com.dbd.market.screens.fragments.market.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dbd.market.R
import com.dbd.market.adapters.home.HomeViewPager2Adapter
import com.dbd.market.databinding.FragmentHomeBinding
import com.dbd.market.screens.fragments.market.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewPager2Adapter: HomeViewPager2Adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabLayoutAndViewPager2()
    }

    private fun setupTabLayoutAndViewPager2() {
        binding.homeViewPager2.isUserInputEnabled = false
        homeViewPager2Adapter = HomeViewPager2Adapter(childFragmentManager, lifecycle)
        homeViewPager2Adapter.addFragmentToTabLayout(MainCategoryFragment())
        homeViewPager2Adapter.addFragmentToTabLayout(SuitsFragment())
        homeViewPager2Adapter.addFragmentToTabLayout(WeaponFragment())
        homeViewPager2Adapter.addFragmentToTabLayout(HeaddressFragment())
        homeViewPager2Adapter.addFragmentToTabLayout(TorsoFragment())
        homeViewPager2Adapter.addFragmentToTabLayout(LegsFragment())
        binding.homeViewPager2.adapter = homeViewPager2Adapter
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.mainCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_main_tab_layout_icon)
                }
                1 -> {
                    tab.text = getString(R.string.suitsCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_suits_tab_layout_icon)
                }
                2 -> {
                    tab.text = getString(R.string.weaponCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_weapon_tab_layout_icon)
                }
                3 -> {
                    tab.text = getString(R.string.headdressCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_headdress_tab_layout_icon)
                }
                4 -> {
                    tab.text = getString(R.string.torsoCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_torso_tab_layout_icon)
                }
                5 -> {
                    tab.text = getString(R.string.legsCategoryTabLayoutTitleString)
                    tab.setIcon(R.drawable.ic_legs_tab_layout_icon)
                }
            }
        }.attach()
    }
}