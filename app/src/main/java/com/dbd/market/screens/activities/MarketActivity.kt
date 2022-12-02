package com.dbd.market.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dbd.market.R
import com.dbd.market.databinding.ActivityMarketBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        handleBottomNavigationViewVisibility()
    }

    private fun findNavController() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostIntroductionFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupNavControllerWithBottomNavigationView() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun handleBottomNavigationViewVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.productDescriptionFragment) binding.bottomNavigationView.visibility = View.GONE
            else binding.bottomNavigationView.visibility = View.VISIBLE
        }
    }

}