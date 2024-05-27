package com.lutfi.storykuy.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.models.LoginResult

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getLoginResult(): LiveData<LoginResult> {
        return storyRepository.loginResultFlow.asLiveData()
    }
}