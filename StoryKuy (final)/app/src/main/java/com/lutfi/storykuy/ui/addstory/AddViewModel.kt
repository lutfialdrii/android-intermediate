package com.lutfi.storykuy.ui.addstory

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.models.LoginResult

class AddViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.Q)
    fun uploadImage(context: Context, uri: Uri, desc: String, token:String) =
        storyRepository.uploadImage(context, uri, desc, token)

    fun getLoginResult(): LiveData<LoginResult?> {
        return storyRepository.loginResultFlow.asLiveData()
    }
}