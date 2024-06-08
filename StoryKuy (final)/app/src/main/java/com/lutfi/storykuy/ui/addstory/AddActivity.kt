package com.lutfi.storykuy.ui.addstory

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lutfi.storykuy.R
import com.lutfi.storykuy.data.ResultState
import com.lutfi.storykuy.databinding.ActivityAddBinding
import com.lutfi.storykuy.ui.ViewModelFactory
import com.lutfi.storykuy.ui.auth.LoginActivity
import com.lutfi.storykuy.ui.main.MainActivity
import com.lutfi.storykuy.utils.getImageUri

class AddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<AddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        private val TAG = AddActivity::class.java.simpleName
        private const val REQUIRED_PERMISSION = android.Manifest.permission.CAMERA
    }

    //    Launcher Permission
    private val reqPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    //    Check Permission
    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //        Req Permission
        if (!allPermissionGranted()) {
            reqPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnCamera.setOnClickListener { startCam() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { upload() }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun upload() {
        val desc = binding.edAddDescription.text.toString()
        var isEmptyFields = false
        if (desc.isEmpty()) {
            isEmptyFields = true
            binding.edAddDescription.error = "Tidak Boleh Kosong!"
        }
        if (currentImageUri != null && !isEmptyFields) {
            viewModel.getLoginResult().observe(this) {
                if (it == null) {
                    moveToLogin()
                } else {
                    viewModel.uploadImage(this, currentImageUri!!, desc, it.token!!)
                        .observe(this) { result ->
                            if (result != null) {
                                when (result) {
                                    is ResultState.Loading -> {
                                        showLoading(true)
                                    }
                                    is ResultState.Error -> {
                                        showToast(result.error)
                                        showLoading(false)
                                    }
                                    is ResultState.Success -> {
                                        showToast(result.data.message)
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        showLoading(false)
                                    }
                                }
                            }
                        }
                }
            }

        } else {
            showToast("Lengkapi data terlebih dahulu!")
        }
    }

    private fun startCam() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            currentImageUri = it
            showImage()
        } else {
            Toast.makeText(this, "No media selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        if (it) {
            showImage()
        }
    }

    private fun moveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun showImage() {
        Log.d(TAG, "showImage: $currentImageUri")
        if (currentImageUri != null) binding.ivStory.setImageURI(currentImageUri) else Toast.makeText(
            this,
            "Something went wrong!",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showToast(string: String?) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(b: Boolean) {
        binding.progressIndicator.visibility = if (b) View.VISIBLE else View.GONE
    }


}