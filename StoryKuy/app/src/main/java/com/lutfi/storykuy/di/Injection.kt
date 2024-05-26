package com.lutfi.storykuy.di

import com.lutfi.storykuy.data.AuthRepository
import com.lutfi.storykuy.data.remote.retrofit.ApiConfig
import com.lutfi.storykuy.utils.AppExecutors

object Injection {
    fun provideRepository() : AuthRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        return AuthRepository.getInstance(apiService, appExecutors)
    }
}