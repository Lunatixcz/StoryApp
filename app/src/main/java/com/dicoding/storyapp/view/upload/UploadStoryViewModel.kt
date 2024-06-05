package com.dicoding.storyapp.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.response.FileUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel (private val repository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun uploadImage(token: String, multipartBody: MultipartBody.Part, description: RequestBody) : FileUploadResponse {
        return withContext(Dispatchers.IO) {
            try {
                val apiService = ApiConfig.getApiService(token)
                val response = apiService.uploadImage(multipartBody, description)
                response
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val storyResponse = repository.getStories()
            } catch (e: Exception) {
            } finally {
                _isLoading.value = true
            }
        }
    }


}