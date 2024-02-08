package ahmadkabi.stories.ui.map

import ahmadkabi.stories.data.StoryRepository
import ahmadkabi.stories.data.source.remote.ApiResponse
import ahmadkabi.stories.domain.model.Story
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<ApiResponse<ArrayList<ahmadkabi.stories.domain.model.Story>>>()
    val stories: LiveData<ApiResponse<ArrayList<ahmadkabi.stories.domain.model.Story>>>
        get() = _stories

    fun getMappedStories() {
        viewModelScope.launch {
            try {
                _stories.value = storyRepository.getMappedStories()
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