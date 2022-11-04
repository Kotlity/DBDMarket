package com.dbd.market.helpers.products_adder.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dbd.market.R
import com.dbd.market.databinding.ActivityProductsAdderBinding

class ProductsAdderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsAdderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsAdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}