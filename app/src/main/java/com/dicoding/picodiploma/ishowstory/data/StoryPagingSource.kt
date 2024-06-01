import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.ishowstory.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.ishowstory.data.remote.retrofit.StoryApiService
import com.dicoding.picodiploma.ishowstory.data.pref.UserPreferences
import kotlinx.coroutines.flow.first

class StoryPagingSource(
    private val apiService: StoryApiService,
    private val userPreferences: UserPreferences
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = userPreferences.token.first() ?: ""
            val response = apiService.getStories("Bearer $token", position, params.loadSize)
            val responseData = response.body()?.listStory ?: emptyList()

            LoadResult.Page(
                data = responseData.filterNotNull(), // Ensure no null items are passed
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
