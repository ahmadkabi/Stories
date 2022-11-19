package ahmadkabi.storyapp.ui.map

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.Story
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

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

}