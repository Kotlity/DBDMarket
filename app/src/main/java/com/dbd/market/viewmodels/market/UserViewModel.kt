package com.dbd.market.viewmodels.market

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.data.Order
import com.dbd.market.data.User
import com.dbd.market.helpers.operations.UserResettingPasswordOperation
import com.dbd.market.helpers.operations.UserUploadingImageFirebaseFirestoreOperation
import com.dbd.market.repositories.market.user.UserRepository
import com.dbd.market.room.entity.UserAvatarEntity
import com.dbd.market.utils.Constants.USER_SUCCESSFULLY_LOGOUT
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
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userResettingPasswordOperation: UserResettingPasswordOperation,
    private val userUploadingImageFirebaseFirestoreOperation: UserUploadingImageFirebaseFirestoreOperation): ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Undefined())
    val user = _user.asStateFlow()

    private val _updatedUserImageFirebaseStorage = MutableStateFlow<Resource<Uri>>(Resource.Undefined())
    val updatedUserImageFirebaseStorage = _updatedUserImageFirebaseStorage.asStateFlow()

    private val _updatedUserImageFirebaseFirestore = MutableStateFlow<Resource<Boolean>>(Resource.Undefined())
    val updatedUserImageFirebaseFirestore = _updatedUserImageFirebaseFirestore.asStateFlow()

    private val _userRecentOrder = MutableStateFlow<Resource<Order>>(Resource.Undefined())
    val userRecentOrder = _userRecentOrder.asStateFlow()

    private val _userResetPassword = MutableSharedFlow<Resource<String>>()
    val userResetPassword = _userResetPassword.asSharedFlow()

    private val _logoutUser = MutableSharedFlow<Resource<String>>()
    val logoutUser = _logoutUser.asSharedFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(onSuccess = { user -> _user.value = Resource.Success(user) }, onFailure = { gettingUserError -> _user.value = Resource.Error(gettingUserError) })
        }
    }

    fun uploadUserImageToFirebaseStorage(imageUri: Uri, imageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _updatedUserImageFirebaseStorage.value = Resource.Loading()
            userRepository.uploadUserImageToFirebaseStorage(imageUri, imageName, onSuccess = { uploadedUri -> _updatedUserImageFirebaseStorage.value = Resource.Success(uploadedUri) },
                onFailure = { addingOrDownloadingImageFromFirebaseStorageError -> _updatedUserImageFirebaseStorage.value = Resource.Error(addingOrDownloadingImageFromFirebaseStorageError) })
        }
    }

    fun uploadUserImageToFirebaseFirestore(userImage: MutableMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            _updatedUserImageFirebaseFirestore.value = Resource.Loading()
            userUploadingImageFirebaseFirestoreOperation.uploadUserImageToFirebaseFirestore(userImage, onSuccess = { _updatedUserImageFirebaseFirestore.value = Resource.Success(true) },
            onFailure = { updatingUserImageError -> _updatedUserImageFirebaseFirestore.value = Resource.Error(updatingUserImageError) })
        }
    }

    fun getUserRecentOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            _userRecentOrder.value = Resource.Loading()
            userRepository.getUserRecentOrder(onSuccess = { recentOrder -> _userRecentOrder.value = Resource.Success(recentOrder) },
                onFailure = { gettingUserRecentOrderError -> _userRecentOrder.value = Resource.Error(gettingUserRecentOrderError) })
        }
    }

    fun resetUserPasswordViaEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userResetPassword.emit(Resource.Loading())
            userResettingPasswordOperation.resetUserPasswordViaEmail(email,
                onSuccess = { viewModelScope.launch(Dispatchers.IO) { _userResetPassword.emit(Resource.Success(email)) } },
                onFailure = { resettingUserPasswordError -> viewModelScope.launch(Dispatchers.IO) { _userResetPassword.emit(Resource.Error(resettingUserPasswordError)) } }
            )
        }
    }

    fun userLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            _logoutUser.emit(Resource.Loading())
            userRepository.userLogout(onSuccess = {
                viewModelScope.launch { _logoutUser.emit(Resource.Success(USER_SUCCESSFULLY_LOGOUT)) }
            },
            onFailure = { userLogoutingError ->
                viewModelScope.launch { _logoutUser.emit(Resource.Error(userLogoutingError)) }
            })
        }
    }

    fun insertUserAvatar(userAvatarEntity: UserAvatarEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUserAvatar(userAvatarEntity)
        }
    }

}