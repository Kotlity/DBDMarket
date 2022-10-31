package com.dbd.market.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dbd.market.R
import com.dbd.market.databinding.ActivityMarketBinding

class MarketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMarketBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController()
        setupNavControllerWithBottomNavigationView()
    }

    private fun findNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostIntroductionFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupNavControllerWithBottomNavigationView() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

}