package com.lutfi.storykuy.data.remote.retrofit

import com.lutfi.storykuy.data.models.AllStoriesResponse
import com.lutfi.storykuy.data.models.LoginResponse
import com.lutfi.storykuy.data.models.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): AllStoriesResponse
}