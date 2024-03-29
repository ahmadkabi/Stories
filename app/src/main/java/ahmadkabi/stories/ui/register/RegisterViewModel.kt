package ahmadkabi.stories.ui.register

import ahmadkabi.stories.core.data.StoryRepository
import ahmadkabi.stories.core.data.source.remote.ApiResponse
import ahmadkabi.stories.core.data.source.remote.model.RegisterBody
import ahmadkabi.stories.domain.model.Register
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _register = MutableLiveData<ApiResponse<Register>>()
    val register: LiveData<ApiResponse<Register>>
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