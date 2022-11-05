package com.dbd.market.helpers.products_adder.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.dbd.market.R
import com.dbd.market.databinding.ActivityProductsAdderBinding

class ProductsAdderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsAdderBinding
    private lateinit var productsAdderToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsAdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationToolbar()
    }

    private fun initializationToolbar() {
        productsAdderToolbar = binding.productsAdderToolbar
        setSupportActionBar(productsAdderToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.products_adder_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
    
}