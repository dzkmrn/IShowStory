package com.dicoding.picodiploma.ishowstory.view.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.pref.dataStore
import com.dicoding.picodiploma.ishowstory.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.ishowstory.databinding.ActivityDetailStoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("id") ?: return
        userPreferences = UserPreferences.getInstance(dataStore)

        fetchStoryDetail(storyId)
    }

    private fun fetchStoryDetail(storyId: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val token = withContext(Dispatchers.IO) {
                userPreferences.token.first()
            }
            if (token != null) {
                binding.progressBar.visibility = View.VISIBLE
                val apiService = ApiConfig.getStoryApiService()
                val response = apiService.getStoryDetail("Bearer $token", storyId)
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.GONE
                    val storyDetailResponse = response.body()
                    storyDetailResponse?.let {
                        displayStoryDetails(it)
                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.e("DetailStoryActivity", "Failed to fetch story details: ${response.message()}")
                }
            } else {
                Log.e("DetailStoryActivity", "Token is null")
            }
        }
    }

    private fun displayStoryDetails(storyDetail: DetailStoryResponse) {
        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(storyDetail?.story?.photoUrl)
                .into(ivStoryPhoto)
            tvName.text = storyDetail.story?.name
            tvDescription.text = storyDetail.story?.description
        }
    }
}