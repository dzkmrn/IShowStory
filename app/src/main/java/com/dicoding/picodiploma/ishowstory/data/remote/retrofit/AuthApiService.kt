package com.dicoding.picodiploma.ishowstory.data.remote.retrofit

import com.dicoding.picodiploma.ishowstory.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResponse>
}