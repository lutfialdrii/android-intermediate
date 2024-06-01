package com.lutfi.storykuy.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.models.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getLoginResult(): LiveData<LoginResult?> {
        return storyRepository.loginResultFlow.asLiveData()
    }


    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getStories(token: String) =
        storyRepository.getStories(token)
}