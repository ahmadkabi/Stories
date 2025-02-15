package ahmadkabi.stories.ui.map

import ahmadkabi.stories.core.data.StoryRepository
import ahmadkabi.stories.core.data.source.remote.ApiResponse
import ahmadkabi.stories.domain.model.Story
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<ApiResponse<ArrayList<Story>>>()
    val stories: LiveData<ApiResponse<ArrayList<Story>>>
        get() = _stories

    private val markers = mutableListOf<Marker>()
    fun addMarker(marker: Marker){
        markers.add(marker)
    }

    fun showMarkerTitle(tag: String){}

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