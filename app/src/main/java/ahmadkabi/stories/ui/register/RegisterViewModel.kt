package ahmadkabi.stories.ui.register

import ahmadkabi.stories.data.StoryRepository
import ahmadkabi.stories.data.source.remote.ApiResponse
import ahmadkabi.stories.data.source.remote.model.RegisterBody
import ahmadkabi.stories.data.source.remote.model.RegisterResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _register = MutableLiveData<ApiResponse<RegisterResponse>>()
    val register: LiveData<ApiResponse<RegisterResponse>>
        get() = _register

    fun register(body: RegisterBody) {
        viewModelScope.launch {
            try {
                _register.value = storyRepository.register(body)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    _register.value = ApiResponse.error()
                }
            } catch (ex: Exception) {
                _register.value = ApiResponse.error()
            }
        }
    }

}