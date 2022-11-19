package com.dbd.market.viewmodels.market.categories.main_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.PagingInfo
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.main_category.MainCategoryRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val mainCategoryRepository: MainCategoryRepository): ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val specialProducts = _specialProducts.asStateFlow()

    private val _beneficialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val beneficialProducts = _beneficialProducts.asStateFlow()

    private val _interestingProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val interestingProducts = _interestingProducts.asStateFlow()

    private val _specialProductsError = MutableSharedFlow<Boolean>()
    val specialProductsError = _specialProductsError.asSharedFlow()

    private val _beneficialProductsError = MutableSharedFlow<Boolean>()
    val beneficialProductsError = _beneficialProductsError.asSharedFlow()

    private val _interestingProductsError = MutableSharedFlow<Boolean>()
    val interestingProductsError = _interestingProductsError.asSharedFlow()

    private var interestingOtherProductsPagingInfoState = MutableStateFlow(PagingInfo())

    private var beneficialOtherProductsPagingInfoState = MutableStateFlow(PagingInfo())

    init {
        fetchSpecialProductsFromFirebaseFirestore()
        fetchBeneficialProductsFromFirebaseFirestore()
        fetchInterestingProductsFromFirebaseFirestore()
    }

    private fun fetchSpecialProductsFromFirebaseFirestore() {
        viewModelScope.launch(Dispatchers.IO) {
            mainCategoryRepository.getSpecialProductsFromFirebaseFirestore(onSuccess = { specialProductsQuerySnapshot ->
                if (specialProductsQuerySnapshot.isEmpty) {
                    viewModelScope.launch(Dispatchers.IO) {
                        _specialProductsError.emit(true)
                    }
                } else {
                    val convertQuerySnapshotToSpecialProductsList = specialProductsQuerySnapshot.toObjects(Product::class.java)
                    viewModelScope.launch(Dispatchers.IO) {
                        _specialProducts.emit(Resource.Success(convertQuerySnapshotToSpecialProductsList))
                    }
                }
            }, onFailure = { fetchingSpecialProductsException ->
                viewModelScope.launch(Dispatchers.IO) {
                    _specialProducts.emit(Resource.Error(fetchingSpecialProductsException.message.toString()))
                }
            })
        }
    }

    fun fetchBeneficialProductsFromFirebaseFirestore() {
        if (!beneficialOtherProductsPagingInfoState.value.isEndOfPaging) {
            viewModelScope.launch(Dispatchers.IO) {
                mainCategoryRepository.getBeneficialProductsFromFirebaseFirestore(onSuccess = { beneficialProductsQuerySnapshot ->
                    if (beneficialProductsQuerySnapshot.isEmpty) {
                        viewModelScope.launch(Dispatchers.IO) {
                            _beneficialProductsError.emit(true)
                        }
                    } else {
                        val convertQuerySnapshotToBeneficialProductsList = beneficialProductsQuerySnapshot.toObjects(Product::class.java)
                        viewModelScope.launch(Dispatchers.IO) {
                            _beneficialProducts.emit(Resource.Success(convertQuerySnapshotToBeneficialProductsList))
                            beneficialOtherProductsPagingInfoState.value.isEndOfPaging = convertQuerySnapshotToBeneficialProductsList == beneficialOtherProductsPagingInfoState.value.oldProducts
                            beneficialOtherProductsPagingInfoState.value.oldProducts = convertQuerySnapshotToBeneficialProductsList
                            beneficialOtherProductsPagingInfoState.value.pageNumber++
                        }
                    }
                }, onFailure = { fetchingBeneficialProductsException ->
                    viewModelScope.launch(Dispatchers.IO) {
                        _beneficialProducts.emit(Resource.Error(fetchingBeneficialProductsException.message.toString()))
                    }
                }, pageNumber = beneficialOtherProductsPagingInfoState.value.pageNumber)
            }
        }
    }

    fun fetchInterestingProductsFromFirebaseFirestore() {
        if (!interestingOtherProductsPagingInfoState.value.isEndOfPaging) {
            viewModelScope.launch(Dispatchers.IO) {
                mainCategoryRepository.getInterestingProductsFromFirebaseFirestore(onSuccess = { interestingProductsQuerySnapshot ->
                    if (interestingProductsQuerySnapshot.isEmpty) {
                        viewModelScope.launch(Dispatchers.IO) {
                            _interestingProductsError.emit(true)
                        }
                    } else {
                        val convertQuerySnapshotToInterestingProductsList = interestingProductsQuerySnapshot.toObjects(Product::class.java)
                        viewModelScope.launch(Dispatchers.IO) {
                            _interestingProducts.emit(Resource.Success(convertQuerySnapshotToInterestingProductsList))
                            interestingOtherProductsPagingInfoState.value.isEndOfPaging = convertQuerySnapshotToInterestingProductsList == interestingOtherProductsPagingInfoState.value.oldProducts
                            interestingOtherProductsPagingInfoState.value.oldProducts = convertQuerySnapshotToInterestingProductsList
                            interestingOtherProductsPagingInfoState.value.pageNumber++
                        }
                    }
                },
                onFailure = { fetchingInterestingProductsException ->
                    viewModelScope.launch(Dispatchers.IO) {
                        _interestingProducts.emit(Resource.Error(fetchingInterestingProductsException.message.toString()))
                    }
                },
                pageNumber = interestingOtherProductsPagingInfoState.value.pageNumber++)
            }
        }
    }
}