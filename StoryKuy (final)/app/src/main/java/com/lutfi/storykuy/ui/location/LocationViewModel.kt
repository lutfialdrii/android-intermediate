package com.lutfi.storykuy.ui.location

import androidx.lifecycle.ViewModel
import com.lutfi.storykuy.data.StoryRepository

class LocationViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun getStories(token: String) =
        storyRepository.getStories(token)
}