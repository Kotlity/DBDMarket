package com.dbd.market.screens.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dbd.market.R
import com.dbd.market.databinding.ActivityMarketBinding
import com.dbd.market.utils.BottomNavigationViewBadgeState
import com.dbd.market.utils.Resource
import com.dbd.market.utils.updateBottomNavigationViewBadge
import com.dbd.market.viewmodels.market.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MarketActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMarketBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val cartViewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findNavController()
        setupNavControllerWithBottomNavigationView()
        handleBottomNavigationViewVisibility()
        observeCartProductsSize()
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

    private fun observeCartProductsSize() {
        lifecycleScope.launch {
            cartViewModel.cartProductsSize.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data != 0) binding.bottomNavigationView.updateBottomNavigationViewBadge(BottomNavigationViewBadgeState.NOTZERO, it.data!!)
                        else binding.bottomNavigationView.updateBottomNavigationViewBadge(BottomNavigationViewBadgeState.ZERO)
                    }
                    is Resource.Loading -> Unit
                    is Resource.Error -> { binding.bottomNavigationView.updateBottomNavigationViewBadge(BottomNavigationViewBadgeState.ERROR) }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

}