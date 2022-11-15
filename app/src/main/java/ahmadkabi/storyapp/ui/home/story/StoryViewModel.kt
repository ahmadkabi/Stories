package ahmadkabi.storyapp.ui.home.story

import ahmadkabi.storyapp.data.source.Repository
import ahmadkabi.storyapp.data.source.remote.ApiConfig
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.GetStoriesResponse
import ahmadkabi.storyapp.data.source.remote.model.QuoteResponseItem
import ahmadkabi.storyapp.data.source.remote.model.Story
import ahmadkabi.storyapp.di.Injection
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(repository: Repository) : ViewModel() {

    val stories: LiveData<PagingData<Story>> =
        repository.getStories().cachedIn(viewModelScope)


//    lateinit var token: String
//
//    private val _stories = MutableLiveData<ApiResponse<PagingData<Story>>>()
//    val stories: LiveData<ApiResponse<PagingData<Story>>>
//        get() = _stories
//
//    fun fetchStories() {
//
//        val service = ApiConfig().getApiService().getStories(
//            "Bearer $token",
//        )
//
//        service.enqueue(object : Callback<GetStoriesResponse> {
//            override fun onResponse(
//                call: Call<GetStoriesResponse>,
//                response: Response<GetStoriesResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null && !responseBody.error) {
//                        if (responseBody.listStory.isNotEmpty()) {
//                            _stories.value = ApiResponse.success(responseBody.listStory)
//
//                        } else {
//                            _stories.value = ApiResponse.empty()
//                        }
//                    }
//                } else {
//                    _stories.value = ApiResponse.error()
//                }
//
//            }
//
//            override fun onFailure(call: Call<GetStoriesResponse>, t: Throwable) {
//                _stories.value = ApiResponse.error()
//            }
//        })
//
//    }

}


class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
