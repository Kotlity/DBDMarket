package com.dbd.market.screens.fragments.market.bottom_navigation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.dbd.market.R
import com.dbd.market.adapters.search.SearchAdapter
import com.dbd.market.databinding.FragmentSearchBinding
import com.dbd.market.utils.*
import com.dbd.market.utils.Constants.REQUEST_CODE_VOICE_PERMISSION
import com.dbd.market.utils.Constants.REQUEST_CODE_VOICE_RECORDING_RESPONSE
import com.dbd.market.viewmodels.market.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    private var searchProductsJob: Job? = null
    private var searchVoiceInput = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeHeaderSearchViewBehaviour()
        setupSearchAdapter()
        searchProducts()
        onHeaderSearchVoiceImageViewClick()
        observeSearchProductsState()
    }

    private fun changeHeaderSearchViewBehaviour() { binding.headerSearchView.setIconifiedByDefault(false) }

    private fun setupSearchAdapter() {
        searchAdapter = SearchAdapter(requireContext())
        binding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addItemDecoration(MarginItemDecoration(MarginItemDecorationType.PRODUCT, resources.getDimensionPixelSize(R.dimen.spaceBetweenEachItemInCartProductRecyclerView)))
        }
    }

    private fun searchProducts() {
        binding.headerSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchQuery ->
                    if (searchQuery.isNotEmpty()) {
                        hideHeaderSearchVoiceImageView()
                        searchProductsJob?.cancel()
                        searchProductsJob = viewLifecycleOwner.lifecycleScope.launch {
                            delay(500L)
                            searchViewModel.searchProducts(searchQuery)
                        }
                    } else {
                        showHeaderSearchVoiceImageView()
                        searchAdapter.differ.submitList(emptyList())
                    }
                }
                return true
            }
        })
    }

    private fun checkIfDeviceSupportsVoiceSearching(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(requireContext())
    }

    private fun voiceRecordingPermissionStuff(permission: String) {
        when {
            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> launchIntentForVoiceSearching()
            shouldShowRequestPermissionRationale(permission) -> {
                showCustomAlertDialog(requireContext(),
                resources.getString(R.string.voiceRecordingAlertDialogTitleString),
                resources.getString(R.string.voiceRecordingAlertDialogMessageString),
                onPositiveButtonClick = { ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_VOICE_PERMISSION) }) }
            else -> ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE_VOICE_PERMISSION)
        }
    }

    private fun launchIntentForVoiceSearching() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PROMPT, resources.getString(R.string.voiceRecordingTitleString))
        }
        startActivityForResult(intent, REQUEST_CODE_VOICE_RECORDING_RESPONSE)
    }

    private fun onHeaderSearchVoiceImageViewClick() {
        binding.headerSearchVoiceImageView.setOnClickListener {
            if (checkIfDeviceSupportsVoiceSearching())
                voiceRecordingPermissionStuff(Manifest.permission.RECORD_AUDIO)
            else showToast(requireContext(), binding.root, R.drawable.ic_error_icon, getString(R.string.deviceDoesNotSupportVoiceInput))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_VOICE_PERMISSION && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) launchIntentForVoiceSearching()
            else if (grantResults[0] == PackageManager.PERMISSION_DENIED) showToast(requireContext(), binding.root, R.drawable.ic_error_icon, resources.getString(R.string.voicePermissionHasDeniedString))
        }
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onActivityResult(requestCode, resultCode, data)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_VOICE_RECORDING_RESPONSE && resultCode == RESULT_OK && data != null) {
            val intentResponse = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            intentResponse?.let {
                searchVoiceInput = it[0].replaceFirstChar { firstChar ->
                    if (firstChar.isLowerCase()) firstChar.titlecase(Locale.ROOT) else it.toString() }
                setVoiceInputTextToSearchView(searchVoiceInput)
                searchViewModel.searchProducts(searchVoiceInput)
            }
        }
    }

    private fun setVoiceInputTextToSearchView(voiceInputText: String) {
        binding.headerSearchView.apply {
            setQuery(voiceInputText, false)
            clearFocus()
        }
    }

    private fun observeSearchProductsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchProducts.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collectLatest {
                when(it) {
                    is Resource.Success -> {
                        val listOfSearchedProducts = it.data
                        if (listOfSearchedProducts != null && listOfSearchedProducts.isNotEmpty()) {
                            hideSearchProgressBar()
                            hideEmptyWidgets()
                            searchAdapter.differ.submitList(listOfSearchedProducts)
                        } else {
                            hideSearchProgressBar()
                            showEmptyWidgets()
                        }
                    }
                    is Resource.Loading -> {
                        searchAdapter.differ.submitList(emptyList())
                        showSearchProgressBar()
                        hideEmptyWidgets()
                    }
                    is Resource.Error -> {
                        hideSearchProgressBar()
                        hideEmptyWidgets()
                        showToast(requireContext(), binding.root, R.drawable.ic_error_icon, it.message.toString())
                    }
                    is Resource.Undefined -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideEmptyWidgets()
    }

    private fun showSearchProgressBar() { binding.searchProgressBar.visibility = View.VISIBLE }

    private fun hideSearchProgressBar() { binding.searchProgressBar.visibility = View.GONE }

    private fun showHeaderSearchVoiceImageView() { binding.headerSearchVoiceImageView.visibility = View.VISIBLE }

    private fun hideHeaderSearchVoiceImageView() { binding.headerSearchVoiceImageView.visibility = View.GONE }

    private fun showEmptyWidgets() {
        binding.apply {
            searchEmptyImageView.visibility = View.VISIBLE
            searchEmptyTextView.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyWidgets() {
        binding.apply {
            searchEmptyImageView.visibility = View.GONE
            searchEmptyTextView.visibility = View.GONE
        }
    }
}