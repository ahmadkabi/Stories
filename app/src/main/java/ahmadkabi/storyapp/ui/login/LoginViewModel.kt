package ahmadkabi.storyapp.ui.login

import ahmadkabi.storyapp.data.source.remote.ApiResponse
import ahmadkabi.storyapp.data.source.remote.model.LoginBody
import ahmadkabi.storyapp.data.source.remote.model.LoginResponse
import ahmadkabi.storyapp.network.ApiConfig
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    var body = MutableLiveData<LoginBody>()

    val login =
        Transformations.switchMap(body) {
            val result = MutableLiveData<ApiResponse<LoginResponse>>()

            val service = ApiConfig().getApiService().login(it)

            service.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            result.value = ApiResponse.success(responseBody)
                        } else {
                            result.value = ApiResponse.success(null)
                        }
                    } else {
                        result.value = ApiResponse.success(null)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    result.value = ApiResponse.error()
                }
            })

            result

        }

}