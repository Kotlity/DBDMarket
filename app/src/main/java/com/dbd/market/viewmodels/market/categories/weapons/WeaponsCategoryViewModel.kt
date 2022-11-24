package com.dbd.market.viewmodels.market.categories.weapons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.PagingInfo
import com.dbd.market.helpers.products_adder.data.Product
import com.dbd.market.repositories.market.categories.weapons.WeaponsCategoryRepository
import com.dbd.market.utils.Resource
import com.google.firebase.firestore.ktx.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeaponsCategoryViewModel @Inject constructor(private val weaponsCategoryRepository: WeaponsCategoryRepository): ViewModel() {

    private val _weaponsProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val weaponsProfitableProducts = _weaponsProfitableProducts.asStateFlow()

    private val _weaponsOtherProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val weaponsOtherProducts = _weaponsOtherProducts.asStateFlow()

    private val weaponsOtherPagingInfo = MutableStateFlow(PagingInfo())

    init {
        getWeaponsProfitableProducts()
        getWeaponsOtherProducts()
    }

    private fun getWeaponsProfitableProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            weaponsCategoryRepository.getWeaponsProfitableCategoryFromFirebaseFirestore(onSuccess = { weaponsProfitableQuerySnapshot ->
                val convertWeaponsProfitableQuerySnapshotToWeaponProductObject = weaponsProfitableQuerySnapshot.toObjects<Product>()
                _weaponsProfitableProducts.emit(Resource.Success(convertWeaponsProfitableQuerySnapshotToWeaponProductObject))
            },
            onFailure = { weaponsProfitableException ->
                _weaponsProfitableProducts.emit(Resource.Error(weaponsProfitableException.message.toString()))
            })
        }
    }

    fun getWeaponsOtherProducts() {
        if (!weaponsOtherPagingInfo.value.isEndOfPaging) {
            viewModelScope.launch(Dispatchers.IO) {
                weaponsCategoryRepository.getWeaponsOtherCategoryFromFirebaseFirestore(onSuccess = { weaponsOtherQuerySnapshot ->
                    val convertWeaponsOtherQuerySnapshotToWeaponProductObject = weaponsOtherQuerySnapshot.toObjects<Product>()
                    _weaponsOtherProducts.emit(Resource.Success(convertWeaponsOtherQuerySnapshotToWeaponProductObject))
                    weaponsOtherPagingInfo.value.isEndOfPaging = convertWeaponsOtherQuerySnapshotToWeaponProductObject == weaponsOtherPagingInfo.value.oldProducts
                    weaponsOtherPagingInfo.value.oldProducts = convertWeaponsOtherQuerySnapshotToWeaponProductObject
                    weaponsOtherPagingInfo.value.pageNumber++
                },
                onFailure = { weaponsOtherException ->
                    _weaponsOtherProducts.emit(Resource.Error(weaponsOtherException.message.toString()))
              },
              pageNumber = weaponsOtherPagingInfo.value.pageNumber)
            }
        }
    }
}