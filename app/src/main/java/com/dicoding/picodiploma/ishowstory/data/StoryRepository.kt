import android.util.Log
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.RegisterResponse
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.AuthApiService
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.StoryApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class StoryRepository(
    private val apiStory: StoryApiService,
    private val userPreferences: UserPreferences,
) {
    suspend fun getStories(token: String): Result<StoryResponse> {
        return try {
            val response = apiStory.getStories(token)
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

    fun uploadImage(imageFile: File, description: String) = liveData {
        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiStory.uploadImage(multipartBody, requestBody)
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
            emit(errorResponse.message?.let { Result.Error(it) })
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
      