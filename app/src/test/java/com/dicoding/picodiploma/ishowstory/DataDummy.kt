package com.dicoding.picodiploma.ishowstory

import com.dicoding.picodiploma.ishowstory.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.ishowstory.data.remote.response.Story
import com.dicoding.picodiploma.ishowstory.data.remote.response.StoryResponse


object DataDummy {
    fun generateDummyStory(): StoryResponse {
        return StoryResponse(
            error = false,
            message = "success",
            listStory = arrayListOf(
                ListStoryItem(
                    id = "id",
                    name = "name",
                    description = "description",
                    photoUrl = "photoUrl",
                    createdAt = "createdAt",
                    lat = 0.03,
                    lon = 0.03
                )
            )
        )
    }
}
