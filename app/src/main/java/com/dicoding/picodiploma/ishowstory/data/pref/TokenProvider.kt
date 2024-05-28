package com.dicoding.picodiploma.ishowstory.data.pref

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class TokenProvider {
    suspend fun getToken(userPreferences: UserPreferences): String? {
        return userPreferences.token.firstOrNull()
    }
}