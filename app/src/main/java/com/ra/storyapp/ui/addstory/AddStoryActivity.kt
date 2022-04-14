package com.ra.storyapp.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        getPermission()
        setupActionBar()
        handleActionView()
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
    }

    private fun uploadData() {
        val description = viewBinding.edtDescription.text.toString()

        if(description.isEmpty()) {
            viewBinding.edtDescription.error = getString(R.string.txt_empty_column)
            return
        }

        getFile?.let {
            viewModel.addNewStory(it, description)
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
        private val REQUIRED_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}