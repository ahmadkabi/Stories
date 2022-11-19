package ahmadkabi.storyapp.ui.login

import ahmadkabi.storyapp.data.StoryRepository
import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.LoginBody
import ahmadkabi.storyapp.data.source.remote.model.LoginResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _login = MutableLiveData<ApiResponse<LoginResponse>>()
    val login: LiveData<ApiResponse<LoginResponse>>
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