package com.dbd.market.helpers.products_adder.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbd.market.R
import com.dbd.market.databinding.ActivityProductsAdderBinding
import com.dbd.market.helpers.products_adder.adapter.ProductsAdderAdapter
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.helpers.products_adder.data.SelectedImage
import com.dbd.market.helpers.products_adder.viewmodel.ProductsAdderViewModel
import com.dbd.market.utils.Constants.ALERT_DIALOG_PERMISSION_RATIONALE_TITLE
import com.dbd.market.utils.Constants.DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_MESSAGE
import com.dbd.market.utils.Constants.DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_TITLE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_PRODUCTS_COLLECTION_PATH
import com.dbd.market.utils.Constants.IMAGES_ARE_NOT_SELECTED
import com.dbd.market.utils.Constants.PERMISSION_HAS_DENIED
import com.dbd.market.utils.Constants.PERMISSION_TITLE
import com.dbd.market.utils.Constants.PRODUCT_SUCCESSFULLY_ADDED
import com.dbd.market.utils.Constants.REQUEST_CODE_SELECT_IMAGES
import com.dbd.market.utils.Constants.REQUEST_CODE_STORAGE_PERMISSION
import com.dbd.market.utils.Constants.SUCCESSFULLY_DELETED_ALL_TAKEN_IMAGES
import com.dbd.market.utils.ValidationStatus
import com.dbd.market.utils.showCustomAlertDialog
import com.dbd.market.utils.showToast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProductsAdderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductsAdderBinding
    private lateinit var productsAdderToolbar: Toolbar
    private val productsAdderViewModel by viewModels<ProductsAdderViewModel>()
    private var selectedImagesListFromGallery = mutableListOf<Uri>()
    private var selectedImagesListForRecyclerView = mutableListOf<SelectedImage>()
    private lateinit var productsAdderAdapter: ProductsAdderAdapter

    @Inject lateinit var firebaseStorage: StorageReference
    @Inject lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsAdderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializationToolbar()
        takeImagesFromGallery(Manifest.permission.READ_EXTERNAL_STORAGE)
        setupSelectedImagesRecyclerView()
        deleteAllImages()
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
                    ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> launchIntentForTakingImagesFromGallery()
                    shouldShowRequestPermissionRationale(permission) -> { showDialog(permission) }
                    else -> ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) launchIntentForTakingImagesFromGallery()
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED) showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_error_icon, PERMISSION_HAS_DENIED)
        }
    }

    private fun showDialog(permission: String) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setCancelable(false)
            setTitle(ALERT_DIALOG_PERMISSION_RATIONALE_TITLE)
            setMessage("Permission to access your $PERMISSION_TITLE is required to take photos for the product")
            setPositiveButton("OK") { _, _ ->
                ActivityCompat.requestPermissions(this@ProductsAdderActivity, arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun launchIntentForTakingImagesFromGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES)
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.appcompat.app.AppCompatActivity"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK) {
            if (data?.clipData != null) {
                val listOfSelectedImages = data.clipData?.itemCount ?: 0
                (0 until listOfSelectedImages).forEach { imageCount ->
                    val imageUri = data.clipData?.getItemAt(imageCount)?.uri
                    imageUri?.let { uriOfImage ->
                        selectedImagesListFromGallery.add(uriOfImage)
                        selectedImagesListForRecyclerView.add(SelectedImage(imageCount, uriOfImage))
                    }
                    updateDataFromAdapter()
                }
            } else {
                val imageUri = data?.data
                imageUri?.let { uri ->
                    selectedImagesListFromGallery.add(uri)
                    selectedImagesListForRecyclerView.add(SelectedImage(0, uri))
                }
                updateDataFromAdapter()
            }
            updateImagesSelectedCountTextView()
        }
    }

    private fun setupSelectedImagesRecyclerView() {
        productsAdderAdapter = ProductsAdderAdapter()
        binding.apply {
            selectedImagesRecyclerView.apply {
                adapter = productsAdderAdapter
                layoutManager = LinearLayoutManager(this@ProductsAdderActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    private fun updateDataFromAdapter() {
        productsAdderAdapter.apply {
            differ.submitList(selectedImagesListForRecyclerView)
            notifyDataSetChanged()
        }
    }

    private fun updateImagesSelectedCountTextView() {
        binding.productAdderImagesSelectedCountTextView.text = "Images selected: ${selectedImagesListFromGallery.size}"
    }

    private fun deleteAllImages() {
        binding.productsDeleteAllImagesButton.setOnClickListener {
            if (selectedImagesListFromGallery.isNotEmpty()) {
                requestUserToDeleteAllTakenImages()
            }
            else showToast(this, binding.root, R.drawable.ic_error_icon, IMAGES_ARE_NOT_SELECTED)
        }
    }

    private fun requestUserToDeleteAllTakenImages() {
        showCustomAlertDialog(this, DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_TITLE, DELETE_ALL_TAKEN_IMAGES_ALERT_DIALOG_MESSAGE) {
            selectedImagesListFromGallery.clear()
            selectedImagesListForRecyclerView.clear()
            updateDataFromAdapter()
            updateImagesSelectedCountTextView()
            showToast(this, binding.root, R.drawable.ic_done_icon, SUCCESSFULLY_DELETED_ALL_TAKEN_IMAGES)
        }
    }

    private fun addNewProduct() {
        val name = binding.nameEditTextProductsAdder.text.toString().trim()
        val category = binding.categoryEditTextProductsAdder.text.toString().trim()
        val description = binding.descriptionEditTextProductsAdder.text.toString().trim()
        val price = binding.priceEditTextProductsAdder.text.toString().trim()
        val discount = binding.discountEditTextProductsAdder.text.toString().trim()
        val size = getProductSizesList(binding.sizeEditTextProductsAdder.text.toString())
        val selectedImagesByteArrayList = convertSelectedImagesToByteArrayList()
        val imagesListToSaveItToFirebaseFirestore = mutableListOf<String>()

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                showProgressBar()
            }
            try {
                async {
                    selectedImagesByteArrayList.forEach { byteArray ->
                        val id = UUID.randomUUID().toString()
                            val imagePath = firebaseStorage.child("products/images/$id")
                            val uploadResult = imagePath.putBytes(byteArray).await()
                            val downloadImageUrl = uploadResult.storage.downloadUrl.await().toString()
                            imagesListToSaveItToFirebaseFirestore.add(downloadImageUrl)
                    }
                }.await()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    hideProgressBar()
                    showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_error_icon, e.stackTraceToString())
                }
            }
            val product = Product(UUID.randomUUID().toString(), name, category, description, price.toInt(), if (discount.isEmpty()) null else discount.toFloat(), size, imagesListToSaveItToFirebaseFirestore)
            firebaseFirestore.collection(FIREBASE_FIRESTORE_PRODUCTS_COLLECTION_PATH).add(product).addOnSuccessListener {
                hideProgressBar()
            }.addOnFailureListener { exception ->
                hideProgressBar()
                showToast(this@ProductsAdderActivity, binding.root, R.drawable.ic_error_icon, exception.message.toString())
            }
            productsAdderViewModel.addProduct(product, imagesListToSaveItToFirebaseFirestore)
        }
    }

    private fun getProductSizesList(size: String): List<String> {
        return size.split(",")
    }

    private fun convertSelectedImagesToByteArrayList(): List<ByteArray> {
        val listOfByteArrayImages = mutableListOf<ByteArray>()
        selectedImagesListFromGallery.forEach { uri ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            if (imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)) {
                listOfByteArrayImages.add(byteArrayOutputStream.toByteArray())
            }
        }
        return listOfByteArrayImages
    }

    private fun showProgressBar() {
        binding.productsAdderProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.productsAdderProgressBar.visibility = View.GONE
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

}