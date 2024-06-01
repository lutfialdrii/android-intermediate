package com.lutfi.storykuy.ui.addstory

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.lutfi.storykuy.data.StoryRepository

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun uploadImage(context: Context, uri: Uri, desc: String) =
        storyRepository.uploadImage(context, uri, desc)
}