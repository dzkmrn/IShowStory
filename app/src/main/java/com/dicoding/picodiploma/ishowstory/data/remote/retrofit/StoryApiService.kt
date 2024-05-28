package com.dicoding.picodiploma.ishowstory.data.remote.retrofit

import com.dicoding.picodiploma.ishowstory.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface StoryApiService {
    @GET("stories")
    suspend fun getStories(
        @Header("X-Auth-Token") token: String,
    ): Response<StoryResponse>

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("X-Auth-Token") token: String,
        @Path("id") id: String
    ): Response<DetailStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Response<FileUploadResponse>
}