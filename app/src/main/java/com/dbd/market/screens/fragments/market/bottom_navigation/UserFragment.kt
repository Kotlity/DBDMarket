package com.dbd.market.screens.fragments.market.bottom_navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.User
import com.dbd.market.databinding.FragmentUserBinding
import com.dbd.market.utils.Resource
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeFloatingActionButtonVisibleWhileCollapsingToolbar()
        observeUserState()
    }

    private fun makeFloatingActionButtonVisibleWhileCollapsingToolbar() {
        val layoutParams = binding.userTakePictureFloatingActionButton.layoutParams as CoordinatorLayout.LayoutParams
        var fabBehavior = layoutParams.behavior as? FloatingActionButton.Behavior
        fabBehavior?.let { floatingActionButtonBehavior ->
            floatingActionButtonBehavior.isAutoHideEnabled = false
        } ?: run {
            fabBehavior = FloatingActionButton.Behavior()
            fabBehavior?.isAutoHideEnabled = false
            layoutParams.behavior = fabBehavior
        }
    }

    private fun setupInformationAboutUser(user: User) {
        if (checkIfUserPhotoExists(user)) {
            Glide.with(this).load(user.image).into(binding.collapsingUserImageView)
            setupTextsInformationAboutUser(user)
        } else {
            binding.collapsingUserImageView.setImageResource(R.drawable.ic_no_user_photo_icon)
            setupTextsInformationAboutUser(user)
        }
    }

    private fun setupTextsInformationAboutUser(user: User) { binding.collapsingUserToolbar.title = user.firstName.plus(" ${user.lastName}").plus("\n${user.email}") }

    private fun checkIfUserPhotoExists(user: User) = user.image.isNotEmpty()

    private fun observeUserState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.user.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> setupInformationAboutUser(it.data!!)
                    is Resource.Loading -> Unit
                    is Resource.Error -> showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

}