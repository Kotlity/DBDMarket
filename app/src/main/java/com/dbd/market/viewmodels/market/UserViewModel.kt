package com.dbd.market.viewmodels.market

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

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(onSuccess = { user -> _user.value = Resource.Success(user) }, onFailure = { gettingUserError -> _user.value = Resource.Error(gettingUserError) })
        }
    }

}