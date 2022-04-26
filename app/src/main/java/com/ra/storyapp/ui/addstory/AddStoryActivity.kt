package com.ra.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.ra.storyapp.R
import com.ra.storyapp.databinding.ActivityAddStoryBinding
import com.ra.storyapp.ui.camera.CameraActivity
import com.ra.storyapp.ui.camera.CameraActivity.Companion.EXTRA_IS_BACK_CAMERA
import com.ra.storyapp.ui.camera.CameraActivity.Companion.EXTRA_PICTURE
import com.ra.storyapp.utils.Resources
import com.ra.storyapp.utils.hideView
import com.ra.storyapp.utils.rotateBitmap
import com.ra.storyapp.utils.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private val viewBinding: ActivityAddStoryBinding by lazy {
        ActivityAddStoryBinding.inflate(layoutInflater)
    }

    private val viewModel: AddStoryViewModel by viewModel()

    private var getFile: File? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        getPermission()
        setupActionBar()
        handleActionView()
        createLocationService()
        createLocationRequest()
        createLocationCallBack()
        startLocationUpdate()
    }

    private fun createLocationService() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun createLocationCallBack() {
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                viewModel.setLocation(locationResult.lastLocation)
            }
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 1000
        }
    }

    private fun startLocationUpdate() {
        checkPermission()
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        updateLocation()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                updateLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                updateLocation()
            }
        }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewBinding.switchUseLocation.isChecked = false
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }
    }

    private fun updateLocation() {
        checkPermission()
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModel.setLocation(it)
            } ?: Toast.makeText(this@AddStoryActivity, getString(R.string.txt_location_not_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun getPermission() {
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this@AddStoryActivity,
                REQUIRED_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }
    }

    private fun handleActionView() = with(viewBinding) {
        btnCamera.setOnClickListener {
            startCameraX()
        }
        btnGallery.setOnClickListener {
            startGallery()
        }
        btnUpload.setOnClickListener {
            uploadData()
        }
        switchUseLocation.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) checkPermission()
        }
    }

    private fun uploadData() {
        val description = viewBinding.edtDescription.text.toString()

        if(description.isEmpty()) {
            viewBinding.edtDescription.error = getString(R.string.txt_empty_column)
            return
        }

        getFile?.let { file ->
            if(viewBinding.switchUseLocation.isChecked) {
                viewModel.getCurrentLocation.observe(this) { location: Location ->
                    viewModel.addNewStory(
                        file,
                        description,
                        location.latitude.toFloat(),
                        location.longitude.toFloat(),
                    )
                }
            } else {
                viewModel.addNewStory(
                    file,
                    description,
                    null,
                    null,
                )
            }
        } ?: Toast.makeText(
                this@AddStoryActivity,
                getString(R.string.txt_empty_picture),
                Toast.LENGTH_LONG
            ).show()

        viewModel.fileUploadResponse.observe(this) { result ->
            when(result) {
                is Resources.Loading -> {
                    viewBinding.progressBar.hideView(false)
                    disableOrEnableView(false)
                }
                is Resources.Success -> {
                    viewBinding.progressBar.hideView(true)
                    disableOrEnableView(true)
                    Toast.makeText(this@AddStoryActivity, getString(R.string.txt_successful), Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resources.Error -> {
                    viewBinding.progressBar.hideView(true)
                    disableOrEnableView(true)
                    Toast.makeText(this@AddStoryActivity, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.title = getString(R.string.txt_new_story)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == CameraActivity.CAMERAX_RESULT) {
            val myFile = it.data?.getSerializableExtra(EXTRA_PICTURE) as File
            val isBackCamera = it.data?.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            getFile = myFile
            viewBinding.imgPhoto.setImageBitmap(result)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this@AddStoryActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if(it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            viewBinding.imgPhoto.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = ACTION_GET_CONTENT
            type = "image/*"
        }
        val chooser = Intent.createChooser(intent, getString(R.string.txt_choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun allPermissionGranted(): Boolean = REQUIRED_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_PERMISSION) {
            if(!allPermissionGranted()) {
                Toast.makeText(
                    this@AddStoryActivity,
                    getString(R.string.txt_not_allowed),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun disableOrEnableView(state: Boolean) = with(viewBinding) {
        edtDescription.isEnabled = state
        btnUpload.isEnabled = state
        btnGallery.isEnabled = state
        btnCamera.isEnabled = state
    }

    companion object {
        private val REQUIRED_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
        )
        private const val REQUEST_CODE_PERMISSION = 10
    }
}