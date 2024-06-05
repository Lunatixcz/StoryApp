package com.dicoding.storyapp.data.repository

import com.dicoding.storyapp.data.api.AuthService
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.response.LoginResponse
import com.dicoding.storyapp.data.response.RegisterResponse
import retrofit2.HttpException

class AuthRepository(private val authService: AuthService, private val userPreference: UserPreference) {

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return authService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = authService.login(email, password)
            if (!response.error!!) {
                response.loginResult?.token?.let { token ->
                    userPreference.saveSession(UserModel(email, token, true))
                }
            }
            response
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> LoginResponse(error = true, message = "Invalid email or password")
                else -> LoginResponse(error = true, message = "Failed to log in: ${e.message()}")
            }
        } catch (e: Exception) {
            LoginResponse(error = true, message = "Network error: ${e.message}")
        }
    }

    suspend fun saveSession(userModel: UserModel) {
        userPreference.saveSession(userModel)
    }

    companion object {
        fun getInstance(authService: AuthService, userPreference: UserPreference) = AuthRepository(authService, userPreference)
    }
}
