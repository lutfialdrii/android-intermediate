package com.lutfi.storykuy.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.di.Injection
import com.lutfi.storykuy.ui.auth.AuthViewModel
import com.lutfi.storykuy.ui.main.MainViewModel

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class : " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also {
            instance = it
        }

    }
}