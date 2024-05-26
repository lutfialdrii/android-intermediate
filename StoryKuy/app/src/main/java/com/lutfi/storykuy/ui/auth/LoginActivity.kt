package com.lutfi.storykuy.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lutfi.storykuy.data.ResultState
import com.lutfi.storykuy.databinding.ActivityLoginBinding
import com.lutfi.storykuy.ui.MainActivity
import com.lutfi.storykuy.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Error -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }

                        is ResultState.Success -> {
                            Toast.makeText(this, (result.data.message), Toast.LENGTH_SHORT).show()
                            showLoading(false)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}