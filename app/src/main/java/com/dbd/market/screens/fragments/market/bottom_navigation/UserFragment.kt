package com.dbd.market.screens.fragments.market.bottom_navigation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dbd.market.R
import com.dbd.market.data.Order
import com.dbd.market.data.User
import com.dbd.market.databinding.FragmentUserBinding
import com.dbd.market.room.entity.UserAvatarEntity
import com.dbd.market.screens.activities.LoginRegisterActivity
import com.dbd.market.screens.activities.MarketActivity
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.ALERT_DIALOG_PERMISSION_RATIONALE_TITLE
import com.dbd.market.utils.Constants.ENGLISH_LANGUAGE
import com.dbd.market.utils.Constants.FIREBASE_FIRESTORE_USER_IMAGE_FIELD
import com.dbd.market.utils.Constants.LANGUAGE_KEY
import com.dbd.market.utils.Constants.SHARED_PREFERENCES_LANGUAGE_KEY
import com.dbd.market.utils.Constants.PERMISSION_HAS_DENIED
import com.dbd.market.utils.Constants.PERMISSION_UNSUPPORTED_PHONE_VERSION
import com.dbd.market.utils.Constants.REQUEST_CODE_SELECT_IMAGES
import com.dbd.market.utils.Constants.REQUEST_CODE_STORAGE_PERMISSION
import com.dbd.market.utils.Constants.UKRAINIAN_LANGUAGE
import com.dbd.market.viewmodels.market.UserViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private val userViewModel by viewModels<UserViewModel>()
    private var isPhotoPicked = false
    private var recentOrder: Order? = null
    private lateinit var currentLanguage: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentLanguage = Locale.getDefault().language
        makeFloatingActionButtonVisibleWhileCollapsingToolbar()
        observeUserState()
        observeUpdatedUserImageFirebaseFirestoreState()
        observeUserResetPasswordState()
        observeLogoutUserState()
        onUserFloatingButtonClick()
        onUserAvatarsLinearLayoutClick()
        onUserAllOrdersLinearLayoutClick()
        onUserRecentOrderLinearLayoutClick()
        onUserAllAddressesLinearLayoutClick()
        onUserLanguageLinearLayoutClick()
        onUserResetPasswordLinearLayoutClick()
        onUserExitLinearLayoutClick()
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

    private fun onUserAvatarsLinearLayoutClick() { navigateToAnotherFragmentWithoutArguments(binding.userAvatarsLinearLayout, R.id.action_userFragment_to_userAvatarsFragment) }

    private fun onUserAllOrdersLinearLayoutClick() { navigateToAnotherFragmentWithoutArguments(binding.allOrdersLinearLayout, R.id.action_userFragment_to_ordersFragment) }

    private fun onUserRecentOrderLinearLayoutClick() {
        binding.recentOrderLinearLayout.setOnClickListener {
            userViewModel.getUserRecentOrder()
            observeUserRecentOrder()
            recentOrder?.let {
                val action = UserFragmentDirections.actionUserFragmentToOrderDetailFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun onUserAllAddressesLinearLayoutClick() { navigateToAnotherFragmentWithoutArguments(binding.allAddressesLinearLayout, R.id.action_userFragment_to_addressesFragment) }

    private fun onUserLanguageLinearLayoutClick() {
        binding.languageLinearLayout.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun onUserResetPasswordLinearLayoutClick() {
        binding.resetPasswordLinearLayout.setOnClickListener {
            showDialogForResettingPassword(
                requireContext(),
                getString(R.string.resetPasswordTitleDialogTextViewString),
                getString(R.string.resetPasswordDescriptionDialogTextViewString),
                getString(R.string.appCancelResetPasswordButtonTextString),
                getString(R.string.appSendResetPasswordButtonTextString),
                onPositiveButtonClick = { email -> userViewModel.resetUserPasswordViaEmail(email) }
            )
        }
    }

    private fun onUserExitLinearLayoutClick() {
        binding.userExitLinearLayout.setOnClickListener {
            showCustomAlertDialog(
                requireContext(),
                resources.getString(R.string.userLogoutTitleAlertDialogString),
                resources.getString(R.string.userLogoutMessageAlertDialogString),
                onPositiveButtonClick = { userViewModel.userLogout() }
            )
        }
    }

    private fun <T> navigationIntent(activity: Class<T>) = Intent(requireActivity(), activity).also { it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK) }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        val alertDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog_change_language_layout, null)
        alertDialog.apply {
            setView(alertDialogView)
            setCancelable(false)
        }

        val changeLanguageAlertDialogTitleTextView = alertDialogView.findViewById<TextView>(R.id.changeLanguageAlertDialogTitleTextView)
        val unitedStatesLinearLayout = alertDialogView.findViewById<LinearLayout>(R.id.unitedStatesLinearLayout)
        val ukrainianLinearLayout = alertDialogView.findViewById<LinearLayout>(R.id.ukrainianLinearLayout)
        val unitedStatesTextView = alertDialogView.findViewById<TextView>(R.id.unitedStatesTextView)
        val ukrainianTextView = alertDialogView.findViewById<TextView>(R.id.ukrainianTextView)
        val checkedUnitedStatesImageView = alertDialogView.findViewById<ImageView>(R.id.checkedUnitedStatesImageView)
        val checkedUkrainianImageView = alertDialogView.findViewById<ImageView>(R.id.checkedUkrainianImageView)
        val changeLanguageAlertDialogCancelButton = alertDialogView.findViewById<AppCompatButton>(R.id.changeLanguageAlertDialogCancelButton)

        changeLanguageAlertDialogTitleTextView.text = getString(R.string.changeLanguageAlertDialogTitleString)

        fun changeToEnglish() {
            unitedStatesLinearLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            checkedUnitedStatesImageView.visibility = View.VISIBLE
            ukrainianLinearLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            ukrainianTextView.apply {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                typeface = Typeface.SANS_SERIF
            }
            checkedUkrainianImageView.visibility = View.GONE
        }

        fun changeToUkrainian() {
            ukrainianLinearLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
            checkedUkrainianImageView.visibility = View.VISIBLE
            unitedStatesLinearLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red))
            unitedStatesTextView.apply {
                setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
                typeface = Typeface.SANS_SERIF
            }
            checkedUnitedStatesImageView.visibility = View.GONE
        }

        when(currentLanguage) {
            ENGLISH_LANGUAGE -> changeToEnglish()
            UKRAINIAN_LANGUAGE -> changeToUkrainian()
        }

        fun setLocal(langCode: String) {
            val locale = Locale(langCode)
            Locale.setDefault(locale)
            val resources = context?.resources
            val config = resources?.configuration
            config?.locale = locale
            resources?.updateConfiguration(config,resources.displayMetrics)
        }

        fun changeLanguage(language: String) {
            val intent = navigationIntent(MarketActivity::class.java)

            val sharedPref = activity?.getSharedPreferences(SHARED_PREFERENCES_LANGUAGE_KEY, Context.MODE_PRIVATE)
            sharedPref?.edit()?.putString(LANGUAGE_KEY, ENGLISH_LANGUAGE)?.apply()

            if (language == ENGLISH_LANGUAGE) {
                setLocal(ENGLISH_LANGUAGE)
                changeToEnglish()
                sharedPref?.edit()?.putString(LANGUAGE_KEY, ENGLISH_LANGUAGE)?.apply()
                startActivity(intent)
            } else if (language == UKRAINIAN_LANGUAGE) {
                setLocal(UKRAINIAN_LANGUAGE)
                changeToUkrainian()
                sharedPref?.edit()?.putString(LANGUAGE_KEY, UKRAINIAN_LANGUAGE)?.apply()
                startActivity(intent)
            }
        }

        unitedStatesLinearLayout.setOnClickListener { changeLanguage(ENGLISH_LANGUAGE) }

        ukrainianLinearLayout.setOnClickListener { changeLanguage(UKRAINIAN_LANGUAGE) }

        changeLanguageAlertDialogCancelButton.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
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
            val imageName = retrieveFileName(takenImageUri!!)
            userViewModel.uploadUserImageToFirebaseStorage(takenImageUri, imageName)
            userViewModel.insertUserAvatar(UserAvatarEntity(userAvatar = takenImageUri))
            observeUpdatedUserImageFirebaseStorageState()
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

    private fun observeUpdatedUserImageFirebaseStorageState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.updatedUserImageFirebaseStorage.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> {
                        hideUserProgressBar()
                        val userImage = mutableMapOf<String, Any>()
                        userImage[FIREBASE_FIRESTORE_USER_IMAGE_FIELD] = it.data.toString()
                        userViewModel.uploadUserImageToFirebaseFirestore(userImage)
                        observeUpdatedUserImageFirebaseFirestoreState()
                    }
                    is Resource.Loading -> showUserProgressBar()
                    is Resource.Error -> {
                        hideUserProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeUpdatedUserImageFirebaseFirestoreState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.updatedUserImageFirebaseFirestore.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> hideUserProgressBar()
                    is Resource.Loading -> showUserProgressBar()
                    is Resource.Error -> {
                        hideUserProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeUserRecentOrder() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userRecentOrder.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> {
                        hideUserProgressBar()
                        recentOrder = it.data
                    }
                    is Resource.Loading -> showUserProgressBar()
                    is Resource.Error -> {
                        hideUserProgressBar()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeUserResetPasswordState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.userResetPassword.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> showToast(requireActivity(), binding.root, R.drawable.ic_done_icon, getString(R.string.successResetPasswordToastMessageString))
                    is Resource.Error -> showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    is Resource.Loading -> Unit
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun observeLogoutUserState() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.logoutUser.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect {
                when(it) {
                    is Resource.Success -> {
                        hideUserProgressBar()
                        val intent = navigationIntent(LoginRegisterActivity::class.java)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                       hideUserProgressBar()
                       showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Loading -> showUserProgressBar()
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    private fun showUserProgressBar() { binding.userProgressBar.visibility = View.VISIBLE }

    private fun hideUserProgressBar() { binding.userProgressBar.visibility = View.GONE }

}