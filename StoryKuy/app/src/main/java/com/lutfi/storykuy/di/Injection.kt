package com.lutfi.storykuy.di

import android.content.Context
import com.lutfi.storykuy.data.StoryRepository
import com.lutfi.storykuy.data.local.DataStoreManager
import com.lutfi.storykuy.data.local.dataStore
import com.lutfi.storykuy.data.remote.retrofit.ApiConfig
import com.lutfi.storykuy.utils.AppExecutors
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val appExecutors = AppExecutors()
        val dataStoreManager = DataStoreManager.getInstance(context.dataStore)
        val user = runBlocking { dataStoreManager.loginResultFlow.first() }
        val apiService = ApiConfig.getApiService(user.token!!)
        return StoryRepository.getInstance(apiService, appExecutors, dataStoreManager)
    }
}