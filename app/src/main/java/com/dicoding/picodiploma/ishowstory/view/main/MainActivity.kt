package com.dicoding.picodiploma.ishowstory.view.main

import ViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.ishowstory.R
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import com.dicoding.picodiploma.ishowstory.data.pref.dataStore
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import com.dicoding.picodiploma.ishowstory.databinding.ActivityMainBinding
import com.dicoding.picodiploma.ishowstory.view.welcome.WelcomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.coroutines.flow.collect


class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val userPreferences = UserPreferences.getInstance(dataStore)
        val token = runBlocking { userPreferences.token.first() }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentTime: String = sdf.format(Date())

        if (token != null) {
            Log.d("tokenmain", "$currentTime $token")
            observeStories(token)
        }

        setupAction()
        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAction() {
        binding.fabAddstory.setOnClickListener {
            val intent = Intent(this@MainActivity, PostStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
    }

    private fun observeStories(token: String) {
        viewModel.getStories(token).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val stories = result.data.listStory ?: emptyList()
                    binding.rvStories.adapter = StoryAdapter(stories)
                }
                is Result.Error -> {
                    Log.e("StoryActivity", "Error fetching stories: ${result.error}")
                    viewModel.logout()
                }
            }
        }
    }
}
