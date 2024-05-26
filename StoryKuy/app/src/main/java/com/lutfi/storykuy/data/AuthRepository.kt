package com.lutfi.storykuy.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.lutfi.storykuy.data.models.LoginResponse
import com.lutfi.storykuy.data.models.RegisterResponse
import com.lutfi.storykuy.data.remote.retrofit.ApiService
import com.lutfi.storykuy.utils.AppExecutors
import retrofit2.HttpException


class AuthRepository private constructor(
    private val apiService: ApiService,
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

    //    singleton
    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(apiService).also {
                instance = it
            }
        }
    }
}