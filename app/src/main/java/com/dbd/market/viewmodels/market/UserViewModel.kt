package com.dbd.market.viewmodels.market

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.User
import com.dbd.market.repositories.market.user.UserRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Undefined())
    val user = _user.asStateFlow()

    private val _updatedUserImage = MutableStateFlow<Resource<Uri>>(Resource.Undefined())
    val updatedUserImage = _updatedUserImage.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(onSuccess = { user -> _user.value = Resource.Success(user) }, onFailure = { gettingUserError -> _user.value = Resource.Error(gettingUserError) })
        }
    }

    fun updateUserImage(imageUri: Uri, imageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updatedUserImage.value = Resource.Loading()
            userRepository.updateUserImage(imageUri, imageName, onSuccess = { uploadedUri -> _updatedUserImage.value = Resource.Success(uploadedUri) },
                onFailure = { addingOrDownloadingImageFromFirebaseStorageError -> _updatedUserImage.value = Resource.Error(addingOrDownloadingImageFromFirebaseStorageError) })
        }
    }

}