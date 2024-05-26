package com.lutfi.storykuy.ui.auth

import androidx.lifecycle.ViewModel
import com.lutfi.storykuy.data.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)

    fun login(email: String, password: String) =
        authRepository.login(email, password)
}