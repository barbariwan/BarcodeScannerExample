package com.devpulsar.barcodescannerexample.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.devpulsar.barcodescannerexample.databinding.FragmentBarcodeScannerBinding

class BarcodeScannerFragment : Fragment() {
    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {  result ->
        if (result.all { it.value }) {
            startCamera()
        } else {
            context?.let { safeContext ->
                Toast.makeText(safeContext, "권한 필요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private var _binding: FragmentBarcodeScannerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAllPermissionsGranted()) {
            startCamera()
        } else {
            cameraPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarcodeScannerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isAllPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { safeContext ->
            ContextCompat.checkSelfPermission(
                safeContext, it) == PackageManager.PERMISSION_GRANTED
        } ?: false
    }

    private fun startCamera() {
        context?.let { safeContext ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
            }, ContextCompat.getMainExecutor(safeContext))
        }
    }

    private fun bindPreview(cameraProviderFuture: ProcessCameraProvider) {
        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(binding.preview.surfaceProvider)
        }
        cameraProviderFuture.unbindAll()
        cameraProviderFuture.bindToLifecycle(
            viewLifecycleOwner,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
        )
    }
}