package com.dicoding.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MapsViewModel (private val userRepository: UserRepository): ViewModel(){
    
    private val _stories = MutableLiveData<StoryResponse>()
    val stories: LiveData<StoryResponse> = _stories

    fun getStoriesLocation(){
        viewModelScope.launch {
            val response=  userRepository.getStoriesLocation()
            _stories.postValue(response)
        }
    }
    
}