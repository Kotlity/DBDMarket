package com.dbd.market.helpers.products_adder.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.helpers.products_adder.validation.*
import com.dbd.market.utils.AddProductFieldsState
import com.dbd.market.utils.ValidationStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ProductsAdderViewModel: ViewModel() {

    private val _productsAdderValidationState = MutableSharedFlow<AddProductFieldsState>()
    val productsAdderValidationState = _productsAdderValidationState.asSharedFlow()

    private val _productsAdderToastState = MutableSharedFlow<Boolean>()
    val productsAdderToastState = _productsAdderToastState.asSharedFlow()

    fun addProduct(product: Product, imageList: List<Uri>) {
        if (correctEditTextsInput(product, imageList)) {
            viewModelScope.launch(Dispatchers.IO) {
                _productsAdderToastState.emit(true)
            }
        } else {
            val name = checkNameEditTextInputValidation(product.name)
            val category = checkCategoryEditTextInputValidation(product.category)
            val description = checkDescriptionEditTextInputValidation(product.description)
            val price = checkPriceEditTextInputValidation(product.price.toString())
            val size = checkSizeEditTextInputValidation(product.size)
            val imagesList = checkSelectedImageValidation(imageList)
            val productsAdderState = AddProductFieldsState(name, category, description, price, size, imagesList)
            viewModelScope.launch(Dispatchers.IO) {
                _productsAdderToastState.emit(false)
                _productsAdderValidationState.emit(productsAdderState)
            }
        }
    }

    private fun correctEditTextsInput(product: Product, imageList: List<Uri>): Boolean {
        return (checkNameEditTextInputValidation(product.name) is ValidationStatus.Success &&
                checkCategoryEditTextInputValidation(product.category) is ValidationStatus.Success &&
                checkDescriptionEditTextInputValidation(product.description) is ValidationStatus.Success &&
                checkPriceEditTextInputValidation(product.price.toString()) is ValidationStatus.Success &&
                checkSizeEditTextInputValidation(product.size) is ValidationStatus.Success &&
                checkSelectedImageValidation(imageList) is ValidationStatus.Success)
    }

}