package com.lutfi.storykuy.di

import android.content.Context
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.local.DataStoreManager
import com.lutfi.storykuy.data.local.dataStore
import com.lutfi.storykuy.data.remote.retrofit.ApiConfig
import com.lutfi.storykuy.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val dataStoreManager = DataStoreManager.getInstance(context.dataStore)
        return StoryRepository.getInstance(apiService, appExecutors, dataStoreManager)
    }
}