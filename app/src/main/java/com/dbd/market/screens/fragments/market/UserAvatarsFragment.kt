package com.dbd.market.screens.fragments.market

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.user_avatars.UserAvatarsAdapter
import com.dbd.market.databinding.FragmentUserAvatarsBinding
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_USER_IMAGE_FIELD
import com.dbd.market.viewmodels.market.UserAvatarsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserAvatarsFragment : Fragment() {
    private lateinit var binding: FragmentUserAvatarsBinding
    private val userAvatarsViewModel by viewModels<UserAvatarsViewModel>()
    private lateinit var userAvatarsAdapter: UserAvatarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAvatarsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserAvatarsRecyclerView()
        onUserSetAvatarImageViewClick()
        onCloseUserAvatarsImageViewClick()
        observeUserAvatarsState()
        observeUpdatedUserImageFirebaseFirestoreState()
    }

    private fun setupUserAvatarsRecyclerView() {
        userAvatarsAdapter = UserAvatarsAdapter()
        binding.userAvatarsRecyclerView.apply {
            adapter = userAvatarsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.PRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInProductsRecyclerView)))
        }
    }

    private fun onUserSetAvatarImageViewClick() {
        userAvatarsAdapter.onRecyclerViewItemClick { avatar ->
            showCustomAlertDialog(requireContext(), getString(R.string.userAvatarAlertDialogTitleString), getString(R.string.userAvatarAlertDialogMessageString),
                onPositiveButtonClick = {
                    val userAvatar = avatar as Uri
                    val userAvatarMutableMap = mutableMapOf<String, Any>()
                    userAvatarMutableMap[FIREBASE_FIRESTORE_USER_IMAGE_FIELD] = userAvatar
                    userAvatarsViewModel.uploadUserImageToFirebaseFirestore(userAvatarMutableMap)
                })
        }
    }

    private fun onCloseUserAvatarsImageViewClick() { binding.closeUserAvatars.setOnClickListener { findNavController().navigateUp() } }

    private fun observeUserAvatarsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userAvatarsViewModel.userAvatars.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect { listOfUserAvatars ->
                if (listOfUserAvatars.isNotEmpty()) {
                    hideEmptyWidgets()
                    userAvatarsAdapter.differ.submitList(listOfUserAvatars)
                } else showEmptyWidgets()
            }
        }
    }

    private fun observeUpdatedUserImageFirebaseFirestoreState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userAvatarsViewModel.updatedUserImageFirebaseFirestore.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> {
                        showToast(requireContext(), binding.root, R.drawable.ic_done_icon, getString(R.string.userAvatarSuccessfullyUpdatedString))
                        hideUserAvatarsProgressBar()
                    }
                    is Resource.Loading -> showUserAvatarsProgressBar()
                    is Resource.Error -> {
                        hideUserAvatarsProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showUserAvatarsProgressBar() { binding.userAvatarsProgressBar.visibility = View.VISIBLE }

    private fun hideUserAvatarsProgressBar() { binding.userAvatarsProgressBar.visibility = View.GONE }

    private fun showEmptyWidgets() {
        binding.apply {
            userAvatarsEmptyImageView.visibility = View.VISIBLE
            userAvatarsEmptyTextView.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyWidgets() {
        binding.apply {
            userAvatarsEmptyImageView.visibility = View.GONE
            userAvatarsEmptyTextView.visibility = View.GONE
        }
    }
}