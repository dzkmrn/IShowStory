import android.util.Log
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.AuthApiService
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.StoryApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getAuthApiService(token: String): AuthApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(AuthApiService::class.java)
    }

    fun getStoryApiService(token: String): StoryApiService {
        var tmp = ""
        tmp = token
        Log.d("ApiConfig1", "Creating StoryApiService with token: $token")
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            Log.d("ApiConfig2", "Inside interceptor with token: $tmp")
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $tmp")
                .build()
            chain.proceed(requestHeaders)
        }

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(StoryApiService::class.java)
    }

}