package com.dicoding.storyapp.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.repository.AuthRepository
import com.dicoding.storyapp.di.AuthInjection
import com.dicoding.storyapp.view.login.LoginViewModel
import com.dicoding.storyapp.view.signup.SignupViewModel

class AuthViewModelFactory (private val repository: AuthRepository) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown Viewmodel Class : " + modelClass.name)
        }
    }

    companion object {
        fun getInstance (context: Context) = AuthViewModelFactory(AuthInjection.provideAuth(context))
    }
}