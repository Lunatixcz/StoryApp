package com.dicoding.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginStatus = MutableLiveData<LoginStatus>()
    val loginStatus: LiveData<LoginStatus>
        get() = _loginStatus

    sealed class LoginStatus {
        data class NetworkError(val message: String) : LoginStatus()
        data class Success(val message: String) : LoginStatus()
        data class Error(val message: String): LoginStatus()
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _loginResult.value = response
                if (response.error == false) {
                    val token = response.loginResult?.token
                    if (token != null) {
                        val userModel = UserModel(email, token, true)
                        saveSession(userModel)
                        _loginStatus.value = LoginStatus.Success(response.message ?: "Logged in successfully")
                    } else {
                        _loginStatus.value = LoginStatus.Error("Token is null")
                    }
                } else {
                    _loginStatus.value = LoginStatus.Error(response.message ?: "Failed to log in")
                }
            } catch (e: Exception) {
                val networkErrorMessage = "Network error occurred"
                _loginStatus.value = LoginStatus.NetworkError(e.message ?: networkErrorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
