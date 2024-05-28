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
        Log.d("Injectionauth1", "Retrieved token: $token")
        val authApiService = ApiConfig.getAuthApiService(token ?: "")
        Log.d("Injectionauth2", "Creating StoryApiService with token: $token")
        return AuthRepository.getInstance(authApiService, userPreferences)
    }
}

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val dataStore = context.dataStore
        val userPreferences = UserPreferences.getInstance(dataStore)
        val token = runBlocking { userPreferences.token.first() }
        Log.d("Injection1", "Retrieved token: $token")
        val storyApiService = ApiConfig.getStoryApiService(token ?: "")
        Log.d("Injection2", "Creating StoryApiService with token: $token")
        return StoryRepository.getInstance(storyApiService, userPreferences)
    }
}
