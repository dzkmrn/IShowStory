import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.AuthApiService
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.StoryApiService
import com.dicoding.picodiploma.ishowstory.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


class StoryRepository(
    private val apiStory: StoryApiService,
    private val userPreferences: UserPreferences,
) {

    fun getStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { getStoriesPagingSource() }
        ).flow
    }

    fun getStoriesPagingSource(): PagingSource<Int, ListStoryItem> {
        return StoryPagingSource(apiStory, userPreferences)
    }

    suspend fun uploadImage(token: String, file: File, description: String): Result<FileUploadResponse> {
        return try {
            val filePart = MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody())
            val descriptionPart = description.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiStory.uploadImage("Bearer $token", filePart, descriptionPart)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error("Response body is null")
            } else {
                Result.Error("Failed to upload image: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    suspend fun getStoriesWithLocation(): Result<StoryResponse> {
        return try {
            val token = userPreferences.token.firstOrNull() ?: ""
            val response = apiStory.getStories("Bearer $token")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.Success(body)
                } else {
                    Result.Error("Response body is null")
                }
            } else {
                Result.Error("Failed to fetch stories: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Error occurred: ${e.message}")
        }
    }

    fun getToken(): Flow<String?> {
        return userPreferences.token
    }

    suspend fun logout() {
        userPreferences.clearToken()

    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            apiStory: StoryApiService,
            userPreferences: UserPreferences,
        ): StoryRepository {
            return instance ?: synchronized(this) {
                instance ?: StoryRepository(apiStory, userPreferences).also { instance = it }
            }
        }
    }
}
      