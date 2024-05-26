package com.lutfi.storykuy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lutfi.storykuy.data.AuthRepository
import com.lutfi.storykuy.di.Injection
import com.lutfi.storykuy.ui.viewmodel.RegisterViewModel

class ViewModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class : " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository())
        }.also {
            instance = it
        }
    }
}