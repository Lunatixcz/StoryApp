package com.dicoding.storyapp.di

import android.content.Context
import com.dicoding.storyapp.data.repository.UserRepository
import com.dicoding.storyapp.data.api.ApiConfig
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context)

        val user = runBlocking { pref.getSession().first() }

        val apiService = ApiConfig.getApiService(user.token)

        val storyDatabase =StoryDatabase.getDatabase(context)

        return UserRepository(apiService,pref, storyDatabase)
    }
}