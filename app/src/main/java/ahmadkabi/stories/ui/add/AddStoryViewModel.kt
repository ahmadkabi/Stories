package ahmadkabi.stories.ui.add

import ahmadkabi.stories.core.data.StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.AddStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import ahmadkabi.stories.core.data.source.remote.ApiResponse

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _addStory = MutableLiveData<ApiResponse<AddStoryResponse>>()
    val addStory: LiveData<ApiResponse<AddStoryResponse>>
        get() = _addStory

    fun addStory(file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                _addStory.value = storyRepository.addStory(file, description)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    _addStory.value = ApiResponse.error()
                }
            } catch (ex: Exception) {
                _addStory.value = ApiResponse.error()
            }
        }
    }

}