package com.dicoding.picodiploma.ishowstory.data

import android.util.Log
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.AuthApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiAuth: AuthApiService,
    private val userPreferences: UserPreferences
) {
    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiAuth.register(name, email, password)
            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    Result.Success(registerResponse)
                } else {
                    Result.Error("Empty response body")
                }
            } else {
                Result.Error("Failed to register: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("Register", "Registration error", e) // Log the exception message or stack trace
            Result.Error("Register error: ${e.message}")
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiAuth.login(email, password)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null && loginResponse.loginResult != null) {
                    val token = loginResponse.loginResult.token
                    if (token != null) {
                        userPreferences.saveToken(token)
                        Result.Success(loginResponse)
                    } else {
                        Result.Error("Token not found in login response")
                    }
                } else {
                    Result.Error("Empty response body or loginResult")
                }
            } else {
                Result.Error("Failed to login: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Login Error")
        }
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(apiAuth: AuthApiService, userPreferences: UserPreferences): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository(apiAuth, userPreferences).also { instance = it }
            }
        }
    }

}