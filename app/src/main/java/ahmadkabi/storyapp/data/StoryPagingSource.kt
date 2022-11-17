package ahmadkabi.storyapp.data

import ahmadkabi.storyapp.data.source.remote.ApiService
import ahmadkabi.storyapp.data.source.remote.model.Story
import androidx.paging.PagingSource
import androidx.paging.PagingState

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStory(
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBnMUloQlNqdG5BbUx2MG8iLCJpYXQiOjE2Njg2Njg4OTV9.9C15ZefODKK3ePOKheVtolEvBfiyeIL4_VDfDtTV99E",
                page,
                params.loadSize
            )

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}