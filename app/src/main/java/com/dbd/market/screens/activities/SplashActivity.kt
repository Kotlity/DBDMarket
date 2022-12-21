package com.dbd.market.screens.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dbd.market.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (checkIfUserIsAlreadyRegistered()) navigateToActivity(MarketActivity()) else navigateToActivity(LoginRegisterActivity())
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
}