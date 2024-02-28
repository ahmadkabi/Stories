package ahmadkabi.stories.ui.login

import ahmadkabi.stories.core.data.StoryRepository
import ahmadkabi.stories.core.data.source.remote.ApiResponse
import ahmadkabi.stories.core.data.source.remote.model.LoginBody
import ahmadkabi.stories.domain.model.UserModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _login = MutableLiveData<ApiResponse<UserModel>>()
    val login: LiveData<ApiResponse<UserModel>>
        get() = _login

    fun login(body: LoginBody) {
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