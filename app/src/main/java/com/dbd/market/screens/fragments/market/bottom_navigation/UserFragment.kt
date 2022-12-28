package com.dbd.market.screens.fragments.market.bottom_navigation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.User
import com.dbd.market.databinding.FragmentUserBinding
import com.dbd.market.utils.Constants
import com.dbd.market.utils.Constants.ALERT_DIALOG_PERMISSION_RATIONALE_TITLE
import com.dbd.market.utils.Constants.PERMISSION_HAS_DENIED
import com.dbd.market.utils.Constants.PERMISSION_UNSUPPORTED_PHONE_VERSION
import com.dbd.market.utils.Constants.REQUEST_CODE_SELECT_IMAGES
import com.dbd.market.utils.Constants.REQUEST_CODE_STORAGE_PERMISSION
import com.dbd.market.utils.Resource
import com.dbd.market.utils.showCustomAlertDialog
import com.dbd.market.utils.showToast
import com.dbd.market.viewmodels.market.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val userViewModel by viewModels<UserViewModel>()
    private var isPhotoPicked = false

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
        onUserFloatingButtonClick()
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
            if (!isPhotoPicked) {
                binding.collapsingUserImageView.setImageResource(R.drawable.ic_no_user_photo_icon)
                setupTextsInformationAboutUser(user)
            }
        }
    }

    private fun setupTextsInformationAboutUser(user: User) { binding.collapsingUserToolbar.title = user.firstName.plus(" ${user.lastName}").plus("\n${user.email}") }

    private fun checkIfUserPhotoExists(user: User) = user.image.isNotEmpty()

    private fun checkIfMobileSDKIsEnoughToUploadUserImage() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun handleDifferentVersionsOfRequestStoragePermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(requireActivity().applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> launchIntentToTakeUserImageFromGallery()
            shouldShowRequestPermissionRationale(permission) -> showCustomAlertDialog(requireContext(),
                ALERT_DIALOG_PERMISSION_RATIONALE_TITLE,
                "Permission to access your ${Constants.PERMISSION_TITLE} is required to upload a user image",
                onPositiveButtonClick = { ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION) })
            else -> ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_STORAGE_PERMISSION)
        }
    }

    private fun onUserFloatingButtonClick() {
        binding.userTakePictureFloatingActionButton.setOnClickListener {
            if (checkIfMobileSDKIsEnoughToUploadUserImage()) handleDifferentVersionsOfRequestStoragePermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            else showToast(requireContext(), binding.root, R.drawable.ic_error_icon, PERMISSION_UNSUPPORTED_PHONE_VERSION)
        }
    }

    private fun launchIntentToTakeUserImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGES)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) launchIntentToTakeUserImageFromGallery()
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED) showToast(requireContext(), binding.root, R.drawable.ic_error_icon, PERMISSION_HAS_DENIED)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_IMAGES && resultCode == RESULT_OK && data != null && data.data != null) {
            isPhotoPicked = true
            val takenImageUri = data.data
            val bitmapImage = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, takenImageUri)
            binding.collapsingUserImageView.setImageBitmap(bitmapImage)
        }
    }

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