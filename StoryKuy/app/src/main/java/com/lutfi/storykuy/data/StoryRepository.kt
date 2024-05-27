package com.lutfi.storykuy.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.lutfi.storykuy.data.local.DataStoreManager
import com.lutfi.storykuy.data.models.LoginResponse
import com.lutfi.storykuy.data.models.LoginResult
import com.lutfi.storykuy.data.models.RegisterResponse
import com.lutfi.storykuy.data.remote.retrofit.ApiService
import com.lutfi.storykuy.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException


class StoryRepository private constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) {

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    // Save login result
    suspend fun saveLoginResult(loginResult: LoginResult) {
        dataStoreManager.saveLoginResult(loginResult)
    }

    // Retrieve login result
    val loginResultFlow: Flow<LoginResult> = dataStoreManager.loginResultFlow

    suspend fun logout() {
        dataStoreManager.clearData()
    }

    //    singleton
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
            dataStoreManager: DataStoreManager
        ): StoryRepository = instance ?: synchronized(this) {
            instance ?: StoryRepository(apiService, dataStoreManager).also {
                instance = it
            }
        }
    }
}