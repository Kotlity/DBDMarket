package com.dbd.market.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.dbd.market.R
import com.dbd.market.databinding.ActivitySplashBinding
import com.dbd.market.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startLoading()

    }

    private fun checkIfUserIsAlreadyRegistered(): Boolean {
        return firebaseAuth.currentUser?.uid != null
    }

    private fun navigateToActivity(activity: AppCompatActivity) {
        Intent(this, activity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

    private fun startLoading() {
        val countDownTimer = object: CountDownTimer(Constants.LOADING_SPLASH_SCREEN_PERIOD, Constants.LOADING_SPLASH_SCREEN_TICK_PERIOD) {
            override fun onTick(time: Long) {
                when (binding.loadingTextView.text) {
                    getString(R.string.initialLoadingTextViewStateString) -> binding.loadingTextView.text = getString(R.string.firstLoadingTextViewStateString)
                    getString(R.string.firstLoadingTextViewStateString) -> binding.loadingTextView.text = getString(R.string.secondLoadingTextViewStateString)
                    getString(R.string.secondLoadingTextViewStateString) -> binding.loadingTextView.text = getString(R.string.thirdLoadingTextViewStateString)
                }
            }
            override fun onFinish() {
                if (checkIfUserIsAlreadyRegistered()) navigateToActivity(MarketActivity()) else navigateToActivity(LoginRegisterActivity())
            }
        }
        countDownTimer.start()
    }

}