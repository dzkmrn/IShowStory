package com.dicoding.picodiploma.ishowstory.view.main

import StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.remote.response.LoginResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _storiesResult = MutableLiveData<Result<StoryResponse>>()
    val storiesResult: LiveData<Result<StoryResponse>> = _storiesResult

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

    fun getStories(token: String): LiveData<Result<StoryResponse>> {
        viewModelScope.launch {
            _storiesResult.value = Result.Loading
            val result = repository.getStories(token)
            _storiesResult.value = result
        }
        return storiesResult
    }

    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)

}