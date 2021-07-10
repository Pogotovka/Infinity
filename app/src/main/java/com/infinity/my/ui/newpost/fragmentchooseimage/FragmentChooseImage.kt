package com.infinity.my.ui.newpost.fragmentchooseimage

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.infinity.my.R
import com.infinity.my.data.model.dto.GalleryFolder
import com.infinity.my.data.model.dto.MediaItem
import com.infinity.my.databinding.FragmentChooseImageBinding
import com.infinity.my.ui.base.BaseFragment
import com.infinity.my.ui.decorators.DividerItemDecorator
import com.infinity.my.utils.*
import com.infinity.my.utils.Constants.DEBOUNCE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FragmentChooseImage : BaseFragment<FragmentChooseImageBinding>(R.layout.fragment_choose_image),
    GalleryAdapter.CallbackGallery,
    FolderAdapter.CallbackFolder {

    val viewModel: ChooseImageViewModel by viewModel()
    private val galleryAdapter: GalleryAdapter by inject()
    private val folderAdapter: FolderAdapter by inject()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        viewModel.loadGallery()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun afterCreateView() {
        binding.viewModel = viewModel

        requestPermissions.launch(PERMISSION_STORAGE)

        binding.postImage
            .onClicked()
            .debounce(DEBOUNCE)
            .onEach { requestPermissions.launch(PERMISSION_STORAGE) }
            .launchIn(lifecycleScope)

        with(binding.galleryRv) {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, GALLERY_GRID_VIEW)
            adapter = galleryAdapter
            addItemDecoration(DividerItemDecorator(context))
        }
        with(binding.bottomSheetLayout.rvGalleryFolder) {
            setHasFixedSize(true)
            adapter = folderAdapter
//            addItemDecoration(DividerItemDecorator(context))
        }

        galleryAdapter.initCallback(this)
        folderAdapter.initCallback(this)

        binding.wrapperRecyclerView.setStyleToMaterialCardView()
        binding.bottomSheetLayout.wrapperRecyclerViewBottomSheet.setStyleToMaterialCardView()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheet)
    }

    override fun initObservers() {
        binding.root.showSnackbar(this, viewModel.showMessage, Snackbar.LENGTH_LONG)

        observe(viewModel.galleryResources, ::swapAdapterData)

        viewModel._onTouched observe EventObserver {
            when (it) {
                binding.takePicture -> findNavController().navigate(FragmentChooseImageDirections.actionFragmentChooseImageToFragmentTakePicture())
                binding.back -> requireActivity().onBackPressed()
                binding.selectFolder -> bottomSheetBehavior.state =
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) BottomSheetBehavior.STATE_COLLAPSED
                    else BottomSheetBehavior.STATE_EXPANDED
                binding.choose -> findNavController().navigate(FragmentChooseImageDirections.actionFragmentChooseImageToFragmentAddDescription(viewModel.formBinding.imageUri))
            }
        }
    }

    override fun onImageSelected(m: MediaItem) {
        viewModel.formBinding.imageUri = m.thumb
    }

    private fun swapAdapterData(data: List<GalleryFolder>) {
        folderAdapter.swapData(data)
    }

    override fun onFolderSelected(data: GalleryFolder) {
        binding.folder = data
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        galleryAdapter.swapData(data.mediaItems)
    }

    companion object {
        const val PERMISSION_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val GALLERY_GRID_VIEW = 4
    }
}