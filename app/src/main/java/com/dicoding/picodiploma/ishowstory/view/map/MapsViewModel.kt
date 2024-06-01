package com.dicoding.picodiploma.ishowstory.view.map

import StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.ishowstory.data.Result
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _storiesResult = MutableLiveData<Result<StoryResponse>>()
    val storiesResult: LiveData<Result<StoryResponse>> = _storiesResult

    fun getStoriesWithLocation() {
        _storiesResult.value = Result.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getStoriesWithLocation()
            _storiesResult.postValue(result)
        }
    }
}