package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.Product
import com.dbd.market.repositories.market.search.SearchRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository): ViewModel() {

    private val _searchProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Undefined())
    val searchProducts = _searchProducts.asStateFlow()

    fun searchProducts(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _searchProducts.value = Resource.Loading()
            searchRepository.searchProducts(searchQuery, onSuccess = { listOfSearchedProducts -> _searchProducts.value = Resource.Success(listOfSearchedProducts) },
            onFailure = { searchProductsError -> _searchProducts.value = Resource.Error(searchProductsError) })
        }
    }

}