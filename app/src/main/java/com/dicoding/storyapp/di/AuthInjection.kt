package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.api.AuthConfig
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object AuthInjection {
    fun provideAuth (context: Context) : AuthRepository {
        val userPreference = UserPreference.getInstance(context)
        val user = runBlocking { userPreference.getSession().first()}
        val authApiService = AuthConfig.getApiService(user.token)
        return AuthRepository(authApiService, userPreference)
    }
}