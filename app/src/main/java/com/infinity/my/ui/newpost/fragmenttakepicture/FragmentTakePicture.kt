package com.infinity.my.ui.newpost.fragmenttakepicture

import android.Manifest
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.infinity.my.R
import com.infinity.my.databinding.FragmentTakePictureBinding
import com.infinity.my.ui.base.BaseFragment
import com.infinity.my.utils.*
import com.infinity.my.utils.Constants.DEBOUNCE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class FragmentTakePicture : BaseFragment<FragmentTakePictureBinding>(R.layout.fragment_take_picture) {

    private val viewModel: TakePictureViewModel by viewModel()

    private val outputDirectory: File by lazy { requireActivity().getOutputDirectory() }

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var cameraExecutor: ExecutorService

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        viewModel.formBinding.isCameraPermissionAccess.set(it)
        if (it) initCamera()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun afterCreateView() {
        binding.viewModel = viewModel
        requestPermissions.launch(PERMISSION_CAMERA)

        binding.cameraContainer.tryToAccessCameraPermission
            .onClicked()
            .debounce(DEBOUNCE)
            .onEach { requestPermissions.launch(PERMISSION_CAMERA) }
            .launchIn(lifecycleScope)

        binding.cameraContainer.cameraCapture
            .onClicked()
            .debounce(DEBOUNCE)
            .onEach { takePicture() }
            .launchIn(lifecycleScope)

        binding.cameraContainer.back
            .onClicked()
            .debounce(DEBOUNCE)
            .onEach { findNavController().popBackStack() }
            .launchIn(lifecycleScope)
    }

    override fun initObservers() {
        binding.root.showToast(this, viewModel.showMessage, Toast.LENGTH_LONG)
    }

    private fun initCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.viewFinder.post {
            displayId = binding.viewFinder.display.displayId
            updateCameraUi()
            setUpCamera()
        }
    }

    private fun updateCameraUi() {
        binding.cameraContainer.switchCamera.let {
            viewModel.formBinding.isCaptureEnabled.set(false)
            it.setOnClickListener {
                lensFacing = if (CameraSelector.LENS_FACING_FRONT == lensFacing) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT
                bindCameraUseCases()
            }
        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when {
                cameraProvider?.hasBackCamera() other false -> CameraSelector.LENS_FACING_BACK
                cameraProvider?.hasFrontCamera() other false -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            updateCameraSwitchButton()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireActivity()))
    }

    private fun bindCameraUseCases() {
        val metrics = DisplayMetrics().also { binding.viewFinder.display.getRealMetrics(it) }
        Log.d(TAG, "Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        Log.d(TAG, "Preview aspect ratio: $screenAspectRatio")

        val rotation = binding.viewFinder.display.rotation
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalyzer)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            viewModel.showMessagePrivate.postValue(SingleEvent("Use case binding failed"))
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun takePicture() {
        imageCapture?.let { imageCapture ->
            val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
            val metadata = ImageCapture.Metadata().apply {
                isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
            }
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
                .setMetadata(metadata)
                .build()

            imageCapture.takePicture(
                outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        viewModel.showMessagePrivate.postValue(SingleEvent("Photo capture failed: ${exc.message}"))
                        Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                    }

                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(savedUri.toFile().extension)
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(savedUri.toFile().absolutePath),
                            arrayOf(mimeType)
                        ) { p, uri ->
                            goToFragmentTakePictureDirections(p)
                            Log.d(TAG, "Image capture scanned into media store: $uri")
                        }
                    }
                })

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.root.postDelayed({
                    binding.root.foreground = ColorDrawable(Color.WHITE)
                    binding.root.postDelayed(
                        { binding.root.foreground = null }, ANIMATION_FAST_MILLIS
                    )
                }, ANIMATION_SLOW_MILLIS)
            }
        }
    }

    private fun goToFragmentTakePictureDirections(p: String?) {
        findNavController().navigate(FragmentTakePictureDirections.actionFragmentTakePictureToFragmentAddDescription(p))
    }

    private fun updateCameraSwitchButton() {
        runCatching { viewModel.formBinding.isCaptureEnabled.set(cameraProvider?.hasBackCamera() other false && cameraProvider?.hasFrontCamera() other false) }
                .onFailure { viewModel.formBinding.isCaptureEnabled.set(false) }
        /* try {
            viewModel.formBinding.isCaptureEnabled.set(cameraProvider?.hasBackCamera() other false && cameraProvider?.hasFrontCamera() other false)
        } catch (exception: CameraInfoUnavailableException) {
            viewModel.formBinding.isCaptureEnabled.set(false)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::cameraExecutor.isInitialized) cameraExecutor.shutdown()
        preview = null
        imageCapture = null
        imageAnalyzer = null
        camera = null
        cameraProvider = null
    }

    companion object {

        const val PERMISSION_CAMERA = Manifest.permission.CAMERA

        private const val TAG = "FragmentTakePicture"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        private const val ANIMATION_FAST_MILLIS = 50L
        private const val ANIMATION_SLOW_MILLIS = 50L

        private fun createFile(baseFolder: File, format: String, extension: String): File =
            File(baseFolder, SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension)

        fun Context.getOutputDirectory(): File {
            val mediaDir = externalMediaDirs.firstOrNull()?.let { File(it, getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
        }
    }
}