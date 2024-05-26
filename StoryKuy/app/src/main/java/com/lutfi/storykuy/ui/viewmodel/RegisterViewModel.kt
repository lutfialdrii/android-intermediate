package com.lutfi.storykuy.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lutfi.storykuy.data.AuthRepository

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)
}