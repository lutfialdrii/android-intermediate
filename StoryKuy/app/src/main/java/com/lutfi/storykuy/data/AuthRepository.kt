package com.lutfi.storykuy.data

import com.lutfi.storykuy.data.models.RegisterResponse
import com.lutfi.storykuy.data.remote.retrofit.ApiService
import com.lutfi.storykuy.utils.AppExecutors
import retrofit2.Response


class AuthRepository private constructor(
    private val apiService: ApiService,
) {
    suspend fun register(name: String, email: String, password: String) : Response<RegisterResponse> {
        return apiService.register(name, email, password)
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