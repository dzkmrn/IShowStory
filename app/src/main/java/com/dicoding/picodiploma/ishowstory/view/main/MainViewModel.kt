package com.dicoding.picodiploma.ishowstory.view.main

import StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.remote.response.FileUploadResponse
import com.dicoding.picodiploma.ishowstory.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _storiesResult = MutableLiveData<Result<StoryResponse>>()
    val storiesResult: LiveData<Result<StoryResponse>> = _storiesResult

    fun getStories(): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { repository.getStoriesPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }

    fun getStory(): Flow<PagingData<ListStoryItem>> {
        return repository.getStories().cachedIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }

    fun getToken(): LiveData<String?> {
        return repository.getToken().asLiveData()
    }

    fun uploadImage(file: File, description: String): LiveData<Result<FileUploadResponse>> {
        val result = MutableLiveData<Result<FileUploadResponse>>()

        viewModelScope.launch {
            val token = repository.getToken().first()
            if (token != null) {
                result.postValue(Result.Loading)
                val response = repository.uploadImage(token, file, description)
                result.postValue(response)
            } else {
                result.postValue(Result.Error("Token is null"))
            }
        }

        return result
    }

}