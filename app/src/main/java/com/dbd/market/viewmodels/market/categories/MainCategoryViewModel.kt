package com.dbd.market.viewmodels.market.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.main_category.MainCategoryRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val mainCategoryRepository: MainCategoryRepository): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val specialProducts = _specialProducts.asStateFlow()

    private val _beneficialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val beneficialProducts = _beneficialProducts.asStateFlow()

    init {
        fetchSpecialProductsFromFirebaseFirestore()
        fetchBeneficialProductsFromFirebaseFirestore()
    }

    private fun fetchSpecialProductsFromFirebaseFirestore() {
        viewModelScope.launch(Dispatchers.IO) {
            mainCategoryRepository.getSpecialProductsFromFirebaseFirestore(onSuccess = { specialProductsQuerySnapshot ->
                val convertQuerySnapshotToSpecialProductsList = specialProductsQuerySnapshot.toObjects(Product::class.java)
                viewModelScope.launch(Dispatchers.IO) {
                    _specialProducts.emit(Resource.Success(convertQuerySnapshotToSpecialProductsList))
                }
            }, onFailure = { fetchingSpecialProductsException ->
                viewModelScope.launch(Dispatchers.IO) {
                    _specialProducts.emit(Resource.Error(fetchingSpecialProductsException.message.toString()))
                }
            })
        }
    }

    private fun fetchBeneficialProductsFromFirebaseFirestore() {
        viewModelScope.launch(Dispatchers.IO) {
            mainCategoryRepository.getBeneficialProductsFromFirebaseFirestore(onSuccess = { beneficialProductsQuerySnapshot ->
                val convertQuerySnapshotToBeneficialProductsList = beneficialProductsQuerySnapshot.toObjects(Product::class.java)
                viewModelScope.launch(Dispatchers.IO) {
                    _beneficialProducts.emit(Resource.Success(convertQuerySnapshotToBeneficialProductsList))
                }
            }, onFailure = { fetchingBeneficialProductsException ->
                viewModelScope.launch(Dispatchers.IO) {
                    _beneficialProducts.emit(Resource.Error(fetchingBeneficialProductsException.message.toString()))
                }
            })
        }
    }
}