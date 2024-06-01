import android.content.Context
import android.util.Log
import com.dicoding.picodiploma.ishowstory.data.AuthRepository
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object InjectionAuth {
    fun provideRepository(context: Context): AuthRepository {
        val dataStore = context.dataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val token = runBlocking { userPreferences.token.first() }
        val authApiService = ApiConfig.getAuthApiService(token ?: "")
        return AuthRepository.getInstance(authApiService, userPreferences)
    }
}

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val dataStore = context.dataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val storyApiService = ApiConfig.getStoryApiService()
        return StoryRepository.getInstance(storyApiService, userPreferences)
    }
}
