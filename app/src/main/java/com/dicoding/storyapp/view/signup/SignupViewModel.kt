package com.dicoding.storyapp.view.signup

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.response.ErrorResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SignupViewModel (private val userRepository: AuthRepository) : ViewModel() {

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus>
        get() = _registrationStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class RegistrationStatus {
        data object Loading : RegistrationStatus()
        data class Success(val message: String) : RegistrationStatus()
        data class Error(val message: String): RegistrationStatus()
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            _registrationStatus.value = RegistrationStatus.Loading
            try {
                val response = userRepository.register(name, email, password)
                if (!response.error) {
                    _registrationStatus.value = RegistrationStatus.Success(response.message ?: "Account Created Successfully")
                } else {
                    _registrationStatus.value = RegistrationStatus.Error(response.message ?: "Failed to Create User Account")
                }
            } catch (e: retrofit2.HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _registrationStatus.value = errorMessage?.let { RegistrationStatus.Error(it) }
            } catch (e: Exception) {
                _registrationStatus.value = RegistrationStatus.Error(e.message ?: "Something went wrong during registration")
            } finally {
                _isLoading.value = false
            }
        }
    }
}