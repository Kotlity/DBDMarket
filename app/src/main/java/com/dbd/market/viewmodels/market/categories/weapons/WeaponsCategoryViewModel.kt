package com.dbd.market.viewmodels.market.categories.weapons

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private var _weaponsProfitableProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val weaponsProfitableProducts = _weaponsProfitableProducts.asStateFlow()

    init {
        getWeaponsProfitableProducts()
    }

    private fun getWeaponsProfitableProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            weaponsCategoryRepository.getWeaponsProfitableCategoryFromFirebaseFirestore(onSuccess = { weaponsProfitableQuerySnapshot ->
                val convertWeaponsProfitableQuerySnapshotToWeaponProductObject = weaponsProfitableQuerySnapshot.toObjects<Product>()
                _weaponsProfitableProducts.emit(Resource.Success(convertWeaponsProfitableQuerySnapshotToWeaponProductObject))
            },
            onFailure = { weaponsException ->
                _weaponsProfitableProducts.emit(Resource.Error(weaponsException.message.toString()))
            })
        }
    }
}