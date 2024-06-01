package com.lutfi.storykuy.ui.auth

import androidx.lifecycle.ViewModel
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.models.LoginResult

class AuthViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) =
        storyRepository.register(name, email, password)

    fun login(email: String, password: String) =
        storyRepository.login(email, password)

    suspend fun saveLoginResult(loginResult: LoginResult) =
        storyRepository.saveLoginResult(loginResult)

}
