package com.lutfi.storykuy.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.models.LoginResult
import kotlinx.coroutines.launch

class AuthViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        storyRepository.register(name, email, password)

    fun login(email: String, password: String) =
        storyRepository.login(email, password)

    fun saveLoginResult(loginResult: LoginResult) {
        viewModelScope.launch {
            storyRepository.saveLoginResult(loginResult)

        }
    }
}