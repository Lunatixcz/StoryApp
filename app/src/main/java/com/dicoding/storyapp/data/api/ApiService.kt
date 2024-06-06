package com.dicoding.storyapp.data.api

import com.dicoding.storyapp.data.response.DetailStoryResponse
import com.dicoding.storyapp.data.response.FileUploadResponse
import com.dicoding.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("stories")
   suspend fun getStories(
       @Query("page") page: Int = 1,
       @Query("size") size: Int = 20
   ): StoryResponse

   @GET("stories")
   suspend fun getStoriesLocation(
       @Query("location") location: Int = 1
   ): StoryResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") storyId: String
    ) : DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : FileUploadResponse
}