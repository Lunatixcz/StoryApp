package com.dicoding.storyapp.view.storydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.response.DetailStoryResponse
import kotlinx.coroutines.launch

class DetailViewModel (private val repository: UserRepository): ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _story = MutableLiveData<DetailStoryResponse>()
    val story: LiveData<DetailStoryResponse> = _story

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getStoryDetail(storyId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getStoryDetail(storyId)
                _story.value = response
            } catch (e: Exception) {
                _error.value = DetailStoryResponse(error = true, message = e.message).toString()
            } finally {
                _isLoading.value = false
            }
        }
    }
}