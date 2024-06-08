package com.lutfi.storykuy.data

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.lutfi.storykuy.data.local.DataStoreManager
import com.lutfi.storykuy.data.models.AllStoriesResponse
import com.lutfi.storykuy.data.models.ErrorResponse
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.data.models.LoginResponse
import com.lutfi.storykuy.data.models.LoginResult
import com.lutfi.storykuy.data.remote.retrofit.ApiService
import com.lutfi.storykuy.utils.AppExecutors
import com.lutfi.storykuy.utils.reduceFileImage
import com.lutfi.storykuy.utils.uriToFile
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            try {
                saveLoginResult(successResponse.loginResult!!)
            } catch (e: Exception) {
                emit(ResultState.Error(e.message))
            }
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getStories(token: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesLoc("Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AllStoriesResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiService, token) },
            initialKey = 1
        ).liveData
    }

    // Save login result
    suspend fun saveLoginResult(loginResult: LoginResult) {
        dataStoreManager.saveLoginResult(loginResult)
    }

    // Retrieve login result
    val loginResultFlow: Flow<LoginResult?> = dataStoreManager.loginResultFlow

    suspend fun logout() {
        dataStoreManager.clearData()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun uploadImage(ctx: Context, uri: Uri, desc: String, token: String) = liveData {
        emit(ResultState.Loading)
        val imageFile = uriToFile(uri, ctx).reduceFileImage()
        val desc = desc
        val requestBody = desc.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        try {
            val successResponse =
                apiService.uploadImage(multipartBody, requestBody, "Bearer $token")
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }

    }

    //    singleton
    companion object {
        private const val PAGE_SIZE = 10

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