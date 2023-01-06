package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.helpers.operations.UserUploadingImageFirebaseFirestoreOperation
import com.dbd.market.repositories.market.user_avatar.UserAvatarRepository
import com.dbd.market.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAvatarsViewModel @Inject constructor(
    private val userAvatarRepository: UserAvatarRepository,
    private val userUploadingImageFirebaseFirestoreOperation: UserUploadingImageFirebaseFirestoreOperation
    ): ViewModel() {

    private val _updatedUserImageFirebaseFirestore = MutableSharedFlow<Resource<Boolean>>()
    val updatedUserImageFirebaseFirestore = _updatedUserImageFirebaseFirestore.asSharedFlow()

    val userAvatars = userAvatarRepository.getAllUserAvatars().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun uploadUserImageToFirebaseFirestore(userImage: MutableMap<String, Any>) {
        viewModelScope.launch(Dispatchers.IO) {
            _updatedUserImageFirebaseFirestore.emit(Resource.Loading())
            userUploadingImageFirebaseFirestoreOperation.uploadUserImageToFirebaseFirestore(userImage, onSuccess = {
                viewModelScope.launch(Dispatchers.IO) {
                    _updatedUserImageFirebaseFirestore.emit(Resource.Success(true))
                }
            },
                onFailure = { updatingUserImageError ->
                    viewModelScope.launch(Dispatchers.IO) {
                        _updatedUserImageFirebaseFirestore.emit(Resource.Error(updatingUserImageError))
                    }
                }
            )
        }
    }
}