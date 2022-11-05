package com.dbd.market.helpers.products_adder.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dbd.market.R
import com.dbd.market.databinding.ActivityProductsAdderBinding
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.helpers.products_adder.viewmodel.ProductsAdderViewModel
import com.dbd.market.utils.ValidationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProductsAdderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsAdderBinding
    private lateinit var productsAdderToolbar: Toolbar
    private lateinit var product: Product
    private val productsAdderViewModel by viewModels<ProductsAdderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsAdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationToolbar()
        observeRegisterValidationEditTextsStateAndProductsAdderToastState()
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
        if (item.itemId == R.id.addProductToFirebaseFirestore) {
            addNewProduct()
        }
        return true
    }

    private fun addNewProduct() {
        binding.apply {
            val name = nameEditTextProductsAdder.text.toString().trim()
            val category = categoryEditTextProductsAdder.text.toString().trim()
            val description = descriptionEditTextProductsAdder.text.toString().trim()
            val price = priceEditTextProductsAdder.text.toString().trim()
            val discount = discountEditTextProductsAdder.text.toString().trim()
            val size = getProductSizesList(sizeEditTextProductsAdder.text.toString())
            product = Product(UUID.randomUUID().toString(), name, category, description, price.toInt(), if (discount.isEmpty()) null else discount.toFloat(), size)
        }
        productsAdderViewModel.addProduct(product)
    }

    private fun observeRegisterValidationEditTextsStateAndProductsAdderToastState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    productsAdderViewModel.productsAdderValidationState.collect {
                        if (it.name is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                binding.nameEditTextProductsAdder.apply {
                                    requestFocus()
                                    error = it.name.errorMessage
                                }
                            }
                        }
                        if (it.category is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                binding.categoryEditTextProductsAdder.apply {
                                    requestFocus()
                                    error = it.category.errorMessage
                                }
                            }
                        }
                        if (it.description is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                binding.descriptionEditTextProductsAdder.apply {
                                    requestFocus()
                                    error = it.description.errorMessage
                                }
                            }
                        }
                        if (it.price is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                binding.priceEditTextProductsAdder.apply {
                                    requestFocus()
                                    error = it.price.errorMessage
                                }
                            }
                        }
                        if (it.size is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                binding.sizeEditTextProductsAdder.apply {
                                    requestFocus()
                                    error = it.size.errorMessage
                                }
                            }
                        }
                    }
                }
                launch {
                    productsAdderViewModel.productsAdderToastState.collect {
                        when(it) {
                            true -> {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@ProductsAdderActivity, "Product successfully added", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    private fun getProductSizesList(size: String): List<String> {
        return size.split(",")
    }

}