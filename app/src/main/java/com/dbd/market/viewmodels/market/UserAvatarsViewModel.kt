package com.dbd.market.viewmodels.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbd.market.repositories.market.user_avatar.UserAvatarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserAvatarsViewModel @Inject constructor(private val userAvatarRepository: UserAvatarRepository): ViewModel() {

    val userAvatars = userAvatarRepository.getAllUserAvatars().stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}