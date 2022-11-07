package com.dbd.market.helpers.products_adder.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dbd.market.R
import com.dbd.market.databinding.ActivityProductsAdderBinding
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.helpers.products_adder.viewmodel.ProductsAdderViewModel
import com.dbd.market.utils.Constants.ALERT_DIALOG_PERMISSION_RATIONALE_TITLE
import com.dbd.market.utils.Constants.PERMISSION_HAS_DENIED
import com.dbd.market.utils.Constants.PERMISSION_HAS_GRANTED
import com.dbd.market.utils.Constants.PERMISSION_TITLE
import com.dbd.market.utils.Constants.PRODUCT_SUCCESSFULLY_ADDED
import com.dbd.market.utils.Constants.REQUEST_CODE_STORAGE_PERMISSION
import com.dbd.market.utils.ValidationStatus
import com.dbd.market.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ProductsAdderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsAdderBinding
    private lateinit var productsAdderToolbar: Toolbar
    private lateinit var product: Product
    private val productsAdderViewModel by viewModels<ProductsAdderViewModel>()
    private var selectedImagesListFromGallery = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsAdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationToolbar()
        takeImagesFromGallery(Manifest.permission.READ_EXTERNAL_STORAGE)
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

    private fun takeImagesFromGallery(permission: String) {
        binding.productsAdderAddImagesButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                when {
                    ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_done_icon, PERMISSION_HAS_GRANTED)
                    shouldShowRequestPermissionRationale(permission) -> showDialog(permission)
                    else -> ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_done_icon, PERMISSION_HAS_GRANTED)
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED) showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_error_icon, PERMISSION_HAS_DENIED)
        }
    }

    private fun showDialog(permission: String) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Permission to access your $PERMISSION_TITLE is required to take photos for the product")
            setTitle(ALERT_DIALOG_PERMISSION_RATIONALE_TITLE)
            setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(this@ProductsAdderActivity, arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
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
        productsAdderViewModel.addProduct(product, selectedImagesListFromGallery)
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
                        if (it.imagesList is ValidationStatus.Error) {
                            withContext(Dispatchers.Main) {
                                showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_error_icon, it.imagesList.errorMessage)
                            }
                        }
                    }
                }
                launch {
                    productsAdderViewModel.productsAdderToastState.collect {
                        when(it) {
                            true -> {
                                withContext(Dispatchers.Main) {
                                    showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_done_icon, PRODUCT_SUCCESSFULLY_ADDED)
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