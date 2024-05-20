package com.dicoding.picodiploma.mycamera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.mycamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityMainBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            currentImageUri = it
            showImage(it)
        } else {
            Toast.makeText(this, "No media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImage(uri: Uri) {
        Log.d(TAG, "showImage: $uri")
        binding.previewImageView.setImageURI(uri)
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

    private fun startCameraX() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

    private fun uploadImage() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

}
