package ahmadkabi.storyapp.ui.map

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.GetStoriesResponse
import ahmadkabi.storyapp.data.source.remote.model.Story
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<ApiResponse<ArrayList<Story>>>()
    val stories: LiveData<ApiResponse<ArrayList<Story>>>
        get() = _stories


    fun getMappedStories() {
        viewModelScope.launch {
            try {
                _stories.value = storyRepository.getStoriesWithLocation()
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    _stories.value = ApiResponse.error()
                }
            } catch (ex: Exception) {
                _stories.value = ApiResponse.error()
            }
        }
    }
//
//    fun getMappedStories() {
//
//        val service = storyRepository.getMappedStories()
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
//                            setStory(ApiResponse.success(responseBody.listStory))
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

    fun setStory(result: ApiResponse<ArrayList<Story>>){
        _stories.value = result
    }

}