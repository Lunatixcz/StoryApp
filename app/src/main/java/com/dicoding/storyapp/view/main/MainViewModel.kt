package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.response.ListStoryItem
import com.dicoding.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStories(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val storyResponse = repository.getStories()
                _stories.value = storyResponse
            } catch (e: Exception) {
                _stories.value = StoryResponse(error = true, message = e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}