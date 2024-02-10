package ahmadkabi.stories.ui.login

import ahmadkabi.stories.core.data.StoryRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.LoginResponse
import retrofit2.HttpException
import ahmadkabi.stories.core.data.source.remote.ApiResponse

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _login = MutableLiveData<ApiResponse<LoginResponse>>()
    val login: LiveData<ApiResponse<LoginResponse>>
        get() = _login

    fun login(body: model.LoginBody) {
        viewModelScope.launch {
            try {
                _login.value = storyRepository.login(body)
            } catch (httpEx: HttpException) {
                httpEx.response()?.errorBody()?.let {
                    _login.value = ApiResponse.error()
                }
            } catch (ex: Exception) {
                _login.value = ApiResponse.error()
            }
        }
    }

}